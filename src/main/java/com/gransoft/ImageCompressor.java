package com.gransoft;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class ImageCompressor {

        public static void main(String[] args) throws IOException {
            File input = new File("barcode.png");
            BufferedImage originalImage = ImageIO.read(input);

            int targetWidth = 300;
            int targetHeight = 200;

            Image scaledInstance = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = resized.createGraphics();
            g2d.drawImage(scaledInstance, 0, 0, null);
            g2d.dispose();

            File output = new File("resized.jpg");
            ImageIO.write(resized, "jpg", output);

            System.out.println("Image resized and saved as resized.jpg");
        }
}
