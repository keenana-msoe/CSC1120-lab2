/*
* Course: CSC1120
* Term: Spring 2024
* Assignment: Lab 2
* Creator: Andrew Keenan
* Date: 1-24-24
 */
package keenana;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * This is the main entry point for the program and contains the code for the program to run
 * with the helper methods in the MeanImageMedian class
 */
public class Lab1 {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        boolean valid = false;
        String input1 = "null";
        String outputFile;
        //choosing mean or median
        while (!valid) {
            System.out.println("Mean or Median?");
            input1 = scan.next();
            if(input1.equals("mean") || input1.equals("median")){
                valid = true;
            }
        }
        // output file
        System.out.println("What is the name of the file you wish to output to?");
        outputFile = scan.next();
        //input files
        System.out.println("How many input files are there?");
        int numFiles = scan.nextInt();
        String[] inputs = new String[numFiles];
        for(int i = 0; i < numFiles; i++){
            System.out.println("Enter the location of the input image");
            inputs[i] = scan.next();
        }
        Image[] list = new Image[numFiles];
        // reading the input files
        for (int i = 0; i < numFiles; i++) {
            Path inputPath = new File(inputs[i]).toPath();
            try {
                list[i] = MeanImageMedian.readImage(inputPath);
            } catch (IOException e) {
                System.out.println("Error in file IO when reading input files");
            }
        }
        //creating output image and output file
        Image output;
        if(input1.equals("mean")){
            output = MeanImageMedian.calculateMeanImage(list);
        } else {
            output = MeanImageMedian.calculateMedianImage(list);
        }
        try{
            Path outputPath = new File(outputFile).toPath();
            MeanImageMedian.writeImage(outputPath, output);
        } catch (IOException e){
            System.out.println("Error in file IO when writing to output file");
        }
    }
}
