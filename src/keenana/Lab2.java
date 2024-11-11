/*
 * Course: CSC 1120
 * Term: Spring 2024
 * Assignment: Lab 2
 * Name: Andrew Keenan
 * Created: 1-24-24
 */
package keenana;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * the driver class for lab 2
 */
public class Lab2 extends Application {
    public static void main(String[] args) {
        Lab2.main(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        if(getParameters().getRaw().size() < 3) {
            stage.close();
        }
        String op = getParameters().getRaw().get(0);
        String output = getParameters().getRaw().get(1);
        List<String> inputs = getParameters().getRaw().subList(2, getParameters().getRaw().size());
        Image[] images = new Image[inputs.size()];

        for (int i = 0; i < images.length; i++){
            Path inputPath = new File(inputs.get(i)).toPath();
            images[i] = MeanImageMedian.readImage(inputPath);
        }
        Image iOutput;
        if(op.equals("mean")){
            iOutput = MeanImageMedian.calculateMeanImage(images);
        } else {
            iOutput = MeanImageMedian.calculateMedianImage(images);
        }
        MeanImageMedian.writeImage(Paths.get(output), iOutput);

        ImageView iv = new ImageView(iOutput);
        HBox hb = new HBox(iv);
        stage.setScene(new Scene(hb));
        stage.setTitle("Lab 2");
        stage.show();
    }
}
