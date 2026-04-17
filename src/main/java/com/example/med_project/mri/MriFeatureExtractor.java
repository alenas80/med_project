package com.example.med_project.mri;

public interface MriFeatureExtractor {
    String extract(float[][] slice, int sliceIndex);
}