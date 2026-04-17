package com.example.med_project.modality.parser;

import com.example.med_project.modality.Modality;
import com.example.med_project.model.IngestContext;
import com.example.med_project.mri.MriFeatureExtractor;
import com.example.med_project.mri.MriImageConverter;
import com.example.med_project.util.NiftiUtils;
import com.example.med_project.util.NiftiUtils.NiftiVolume;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;

@Component
public class MriParser implements ModalityParser {

    @Autowired
    private MriFeatureExtractor featureExtractor;

    @Override
    public boolean supports(Modality modality) {
        return modality == Modality.MRI;
    }

    @Override
    public IngestContext parse(IngestContext context) {

        MultipartFile file = context.file();

        try {
            File temp = File.createTempFile("mri", ".nii.gz");
            file.transferTo(temp);

            NiftiVolume volume = NiftiUtils.readNiftiGz(temp);

            for (int z = 0; z < volume.dimZ; z++) {

                float[][] slice = NiftiUtils.extractSlice(volume, z);

                // PNG
                byte[] png = MriImageConverter.floatSliceToPng(slice);

                Path dir = Paths.get("slices");
                Path file_slice = dir.resolve("slice_" + z + ".png");

                Files.write(file_slice, png);
                context.imagesPng().add(png);

                // TEXT (для embeddings)
                String text = featureExtractor.extract(slice, z);
                context.textChunks().add(text);
            }

            context.putMeta("parsed", true);
            context.putMeta("modality", "MRI");
            context.putMeta("slices", volume.dimZ);
            context.putMeta("dimensions",
                    volume.dimX + "x" + volume.dimY + "x" + volume.dimZ);


        } catch (Exception e) {
            throw new RuntimeException("MRI parsing failed", e);
        }

        return context;
    }
}