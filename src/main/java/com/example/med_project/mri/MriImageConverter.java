package com.example.med_project.mri;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public class MriImageConverter {
    public static byte[] floatSliceToPng(float[][] slice) {
        int h = slice.length;
        int w = slice[0].length;

        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;

        for (float[] row : slice) {
            for (float v : row) {
                min = Math.min(min, v);
                max = Math.max(max, v);
            }
        }

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float norm = (slice[y][x] - min) / (max - min + 1e-5f);
                int gray = (int) (norm * 255);

                int rgb = (gray << 16) | (gray << 8) | gray;
                img.setRGB(x, y, rgb);
            }
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "PNG", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
