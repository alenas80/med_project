package com.example.med_project.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

public final class ImageUtils {

    private ImageUtils() {
    }

    public static byte[] placeholderPng() {
        try {
            BufferedImage image = new BufferedImage(224, 224, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 224, 224);
            g.setColor(Color.WHITE);
            g.drawString("MRI PLACEHOLDER", 40, 112);
            g.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create placeholder PNG", e);
        }
    }
}