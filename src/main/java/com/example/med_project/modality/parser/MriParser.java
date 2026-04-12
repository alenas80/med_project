package com.example.med_project.modality.parser;

import com.example.med_project.model.IngestContext;
import com.example.med_project.modality.Modality;
import com.example.med_project.util.ImageUtils;
import org.springframework.stereotype.Component;

@Component
public class MriParser implements ModalityParser {

    @Override
    public boolean supports(Modality modality) {
        return modality == Modality.MRI;
    }

    @Override
    public IngestContext parse(IngestContext context) {
        // Заглушка под реальный NIfTI/DICOM parser.
        // Здесь позже будет:
        // 1. чтение volume
        // 2. извлечение representative slices
        // 3. преобразование в PNG bytes

        byte[] fakeSlice = ImageUtils.placeholderPng();
        context.imagesPng().add(fakeSlice);

        context.putMeta("parsed", true);
        context.putMeta("sequence", "MRI");
        context.putMeta("sourceFile", context.file().getOriginalFilename());

        return context;
    }
}
