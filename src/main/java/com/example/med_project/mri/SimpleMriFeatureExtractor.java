package com.example.med_project.mri;

import org.springframework.stereotype.Component;

@Component
public class SimpleMriFeatureExtractor implements MriFeatureExtractor {

    @Override
    public String extract(float[][] slice, int z) {

        float sum = 0;
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        int count = 0;

        for (float[] row : slice) {
            for (float v : row) {
                sum += v;
                min = Math.min(min, v);
                max = Math.max(max, v);
                count++;
            }
        }

        float mean = sum / count;

        return String.format(
                "MRI slice %d: mean=%.4f, min=%.4f, max=%.4f",
                z, mean, min, max
        );
    }
}