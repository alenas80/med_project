//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.med_project.modality.parser;

import com.example.med_project.modality.Modality;
import com.example.med_project.model.IngestContext;
import com.example.med_project.util.ImageUtils;
import org.springframework.stereotype.Component;

@Component
public class MriParser implements ModalityParser {
    public boolean supports(Modality modality) {
        return modality == Modality.MRI;
    }

    public IngestContext parse(IngestContext context) {
        byte[] fakeSlice = ImageUtils.placeholderPng();
        context.imagesPng().add(fakeSlice);
        context.putMeta("parsed", true);
        context.putMeta("sequence", "MRI");
        context.putMeta("sourceFile", context.file().getOriginalFilename());
        return context;
    }
}
