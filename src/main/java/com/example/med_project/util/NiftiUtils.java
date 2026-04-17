package com.example.med_project.util;

import java.io.*;
import java.nio.*;
import java.util.zip.GZIPInputStream;

public final class NiftiUtils {

    private NiftiUtils() {}

    public static class NiftiVolume {
        public final int dimX;
        public final int dimY;
        public final int dimZ;
        public final float[] data;

        public NiftiVolume(int x, int y, int z, float[] data) {
            this.dimX = x;
            this.dimY = y;
            this.dimZ = z;
            this.data = data;
        }

        public float get(int x, int y, int z) {
            return data[z * dimX * dimY + y * dimX + x];
        }
    }

    public static NiftiVolume readNiftiGz(File file) throws IOException {
        try (InputStream is = new GZIPInputStream(new FileInputStream(file))) {

            byte[] bytes = is.readAllBytes();
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.position(40);
            buffer.getShort(); // dim count
            int dimX = buffer.getShort();
            int dimY = buffer.getShort();
            int dimZ = buffer.getShort();

            buffer.position(70);
            short datatype = buffer.getShort();

            buffer.position(108);
            int offset = (int) buffer.getFloat();
            buffer.position(offset);

            int total = dimX * dimY * dimZ;
            float[] data = new float[total];

            if (datatype == 16) { // float32
                for (int i = 0; i < total; i++) {
                    data[i] = buffer.getFloat();
                }
            } else if (datatype == 4) { // int16
                for (int i = 0; i < total; i++) {
                    data[i] = buffer.getShort();
                }
            } else {
                throw new UnsupportedOperationException("Unsupported datatype: " + datatype);
            }

            return new NiftiVolume(dimX, dimY, dimZ, data);
        }
    }

    public static float[][] extractSlice(NiftiVolume volume, int z) {
        float[][] slice = new float[volume.dimY][volume.dimX];

        for (int y = 0; y < volume.dimY; y++) {
            for (int x = 0; x < volume.dimX; x++) {
                slice[y][x] = volume.get(x, y, z);
            }
        }

        return slice;
    }
}