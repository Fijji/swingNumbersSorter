package com.gransoft;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class BarcodeGenerator {


        public static void main(String[] args) throws Exception {
            String data = "1234567890";
            String path = "barcode.png";

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(data, BarcodeFormat.CODE_128, 300, 100);

            Path outputPath = FileSystems.getDefault().getPath(path);
            MatrixToImageWriter.writeToPath(matrix, "PNG", outputPath);

            System.out.println("Barcode generated: " + path);
        }
    }


