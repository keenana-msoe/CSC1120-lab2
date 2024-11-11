/*
 * Course: CSC 1120
 * Term: Spring 2024
 * Assignment: Lab 2
 * Name: Andrew Keenan
 * Created: 1-24-24
*/
package keenana;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Calculates the mean and median of the Images
 * plus reads incoming images and writes to files form Image objects
 */
public class MeanImageMedian {
    
    /**
     * Maximum color value
     */
    public static final int MAX_COLOR = 255;

    /**
     * Calculates the median of all the images passed to the method.
     * <br />
     * Each pixel in the output image consists is calculated as the median
     * red, green, and blue components of the input images at the same location.
     * @param inputImages Images to be used as input
     * @return An image containing the median color value for each pixel in the input images
     *
     * @throws IllegalArgumentException Thrown if inputImages or any element of inputImages is null,
     * the length of the array is less than two, or  if any of the input images differ in size.
     */
    public static Image calculateMedianImage(Image[] inputImages) {
        int length = inputImages.length;
        double x = inputImages[0].getWidth();
        double y = inputImages[0].getHeight();
        boolean same = true;
        for (Image i : inputImages){
            if (i.getWidth() != x) {
                same = false;
            }
            if (i.getHeight() != y){
                same = false;
            }
        }
        WritableImage median = new WritableImage((int)x, (int)y);
        PixelWriter writer = median.getPixelWriter();
        if (same) {
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    int pixel;
                    int[] asum = new int[length];
                    int[] rsum = new int[length];
                    int[] gsum = new int[length];
                    int[] bsum = new int[length];
                    for (int k = 0; k < length; k++) {
                        PixelReader reader = inputImages[k].getPixelReader();
                        int argb = reader.getArgb(j, i);
                        asum[k] = argbToAlpha(argb);
                        rsum[k] = argbToRed(argb);
                        gsum[k] = argbToGreen(argb);
                        bsum[k] = argbToBlue(argb);
                    }
                    Arrays.sort(asum);
                    Arrays.sort(rsum);
                    Arrays.sort(gsum);
                    Arrays.sort(bsum);
                    int alpha;
                    int red;
                    int green;
                    int blue;

                    if (length % 2 == 0){
                        alpha = (asum[length / 2] + asum[(length / 2)- 1]) / 2;
                        red = (rsum[length / 2] + rsum[(length / 2)- 1]) / 2;
                        green = (gsum[length / 2] + gsum[(length / 2)- 1]) / 2;
                        blue = (bsum[length / 2] + bsum[(length / 2)- 1]) / 2;
                    } else {
                        int division = length / 2;
                        alpha = asum[division];
                        red = rsum[division];
                        green = gsum[division];
                        blue = bsum[division];
                    }
                    pixel = argbToInt(alpha, red, green, blue);
                    writer.setArgb(j, i, pixel);
                }
            }
        }
        return median;
    }

    /**
     * Calculates the mean of all the images passed to the method.
     * <br />
     * Each pixel in the output image consists is calculated as the average of the
     * red, green, and blue components of the input images at the same location.
     * @param inputImages Images to be used as input
     * @return An image containing the mean color value for each pixel in the input images
     *
     * @throws IllegalArgumentException Thrown if inputImages or any element of inputImages is null,
     * the length of the array is less than two, or  if any of the input images differ in size.
     */
    public static Image calculateMeanImage(Image[] inputImages) {
        int length = inputImages.length;
        double x = inputImages[0].getWidth();
        double y = inputImages[0].getHeight();
        boolean same = true;
        for (Image i : inputImages){
            if (i.getWidth() != x) {
                same = false;
            }
            if (i.getHeight() != y){
                same = false;
            }
        }
        WritableImage mean = new WritableImage((int)x, (int)y);
        PixelWriter writer = mean.getPixelWriter();
        if (same) {
            for (int i = 0; i < y; i++) {
                for (int j = 0; j < x; j++) {
                    int pixel;
                    int asum = 0;
                    int rsum = 0;
                    int gsum = 0;
                    int bsum = 0;
                    for (Image inputImage : inputImages) {
                        PixelReader reader = inputImage.getPixelReader();
                        int argb = reader.getArgb(j, i);
                        asum += argbToAlpha(argb);
                        rsum += argbToRed(argb);
                        gsum += argbToGreen(argb);
                        bsum += argbToBlue(argb);
                    }
                    asum = asum / length;
                    rsum = rsum / length;
                    gsum = gsum / length;
                    bsum = bsum / length;
                    pixel = argbToInt(asum, rsum, gsum, bsum);
                    writer.setArgb(j, i, pixel);
                }
            }
        }
        return mean;
    }

    /**
     * Reads an image in PPM format. The method only supports the plain PPM
     * (P3) format with 24-bit color
     * and does not support comments in the image file.
     * @param imagePath the path to the image to be read
     * @return An image object containing the image read from the file.
     *
     * @throws IllegalArgumentException Thrown if imagePath is null.
     * @throws IOException Thrown if the image format is invalid or there was
     * trouble reading the file.
     */
    public static Image readImage(Path imagePath) throws IOException, IllegalArgumentException {
        File file = imagePath.toFile();
        FileInputStream input = new FileInputStream(file);
        Image copy;
        if(imagePath.toString().endsWith(".ppm")){
            copy = readPPMImage(imagePath);
        } else {
            copy = new Image(input);
        }
        input.close();
        return copy;
    }
    private static Image readPPMImage(Path imagePath) throws IOException {
        Scanner in = new Scanner(imagePath);
        WritableImage copy;
        if (in.nextLine().equals("P3")) {
            int x = in.nextInt();
            int y = in.nextInt();
            copy = new WritableImage(x, y);
            PixelWriter writer = copy.getPixelWriter();
            int alpha = in.nextInt();
            if (alpha == MAX_COLOR){
                for (int i = 0; i < y; i++) {
                    for (int j = 0; j < x; j++) {
                        int red = in.nextInt();
                        int green = in.nextInt();
                        int blue = in.nextInt();
                        int pixel = argbToInt(alpha, red, green, blue);
                        writer.setArgb(j, i, pixel);
                    }
                }
            } else {
                throw new IOException();
            }
        } else {
            throw new IOException();
        }
        return copy;
    }

    /**
     * Writes an image in PPM format. The method only supports the plain PPM (P3)
     * format with 24-bit color
     * and does not support comments in the image file.
     * @param imagePath the path to where the file should be written
     * @param image the image containing the pixels to be written to the file
     *
     * @throws IllegalArgumentException Thrown if imagePath is null.
     * @throws IOException Thrown if the image format is invalid or there was trouble
     * reading the file.
     */
    public static void writeImage(Path imagePath, Image image) throws IOException,
            IllegalArgumentException {
        File file = imagePath.toFile();
        if (imagePath.endsWith(".ppm")){
            writePPMImage(imagePath, image);
        } else if (imagePath.endsWith(".png")){
            RenderedImage image2 = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(image2, ".png", file);
        }
    }
    private static void writePPMImage(Path imagePath, Image image) throws IOException {
        PixelReader reader = image.getPixelReader();
        File output = imagePath.toFile();
        PrintWriter writer = new PrintWriter(output);
        double x = image.getWidth();
        double y = image.getHeight();
        writer.write("P3\n"+(int)x+" "+(int)y+"\n"+MAX_COLOR);
        for (int i = 0; i < y; i++){
            writer.write("\n");
            for (int j = 0; j < x; j++){
                int argb = reader.getArgb(j, i);
                int red = argbToRed(argb);
                int green = argbToGreen(argb);
                int blue = argbToBlue(argb);
                writer.write(red+" "+green+" "+blue+" ");
            }
        }
        writer.close();
    }

    /**
     * Extract 8-bit Alpha value of color from 32-bit representation of the color in the format
     * described by the INT_ARGB PixelFormat type.
     * @param argb the 32-bit representation of the color
     * @return the 8-bit Alpha value of the color.
     */
    private static int argbToAlpha(int argb) {
        final int bitShift = 24;
        return argb >> bitShift;
    }

    /**
     * Extract 8-bit Red value of color from 32-bit representation of the color in the format
     * described by the INT_ARGB PixelFormat type.
     * @param argb the 32-bit representation of the color
     * @return the 8-bit Red value of the color.
     */
    private static int argbToRed(int argb) {
        final int bitShift = 16;
        final int mask = 0xff;
        return (argb >> bitShift) & mask;
    }

    /**
     * Extract 8-bit Green value of color from 32-bit representation of the color in the format
     * described by the INT_ARGB PixelFormat type.
     * @param argb the 32-bit representation of the color
     * @return the 8-bit Green value of the color.
     */
    private static int argbToGreen(int argb) {
        final int bitShift = 8;
        final int mask = 0xff;
        return (argb >> bitShift) & mask;
    }

    /**
     * Extract 8-bit Blue value of color from 32-bit representation of the color in the format
     * described by the INT_ARGB PixelFormat type.
     * @param argb the 32-bit representation of the color
     * @return the 8-bit Blue value of the color.
     */
    private static int argbToBlue(int argb) {
        final int bitShift = 0;
        final int mask = 0xff;
        return (argb >> bitShift) & mask;
    }

    /**
     * Converts argb components into a single int that represents the argb value of a color.
     * @param a the 8-bit Alpha channel value of the color
     * @param r the 8-bit Red channel value of the color
     * @param g the 8-bit Green channel value of the color
     * @param b the 8-bit Blue channel value of the color
     * @return a 32-bit representation of the color in the format described by the
     * INT_ARGB PixelFormat type.
     */
    private static int argbToInt(int a, int r, int g, int b) {
        final int alphaShift = 24;
        final int redShift = 16;
        final int greenShift = 8;
        final int mask = 0xff;
        return a << alphaShift | ((r & mask) << redShift) | (g & mask) << greenShift | b & mask;
    }
}



