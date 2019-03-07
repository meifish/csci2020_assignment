<<<<<<< HEAD
<<<<<<< HEAD
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.util.Scanner;

import static javafx.scene.control.ContentDisplay.TOP;


public class ReadTextAndCount extends Application{



    @Override
    public void start(Stage primaryStage) throws Exception{

        String fileName = "";

        // Rectangle part
        HBox hbox = new HBox();
        hbox.setPrefHeight(600);
        hbox.setAlignment(Pos.BOTTOM_LEFT);

        // File name input field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5,5,5,5));
        paneForTextField.setStyle("-fx-border-color: black");
        Label label = new Label("File Name:");
        paneForTextField.setLeft(label);
        paneForTextField.setMargin(label, new Insets(5,5,5,5));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BASELINE_LEFT);
        tf.setPrefColumnCount(30);
        tf.setOnAction(e->actionPerformed(e, tf, hbox));
        paneForTextField.setCenter(tf);

        Button bt = new Button("View");
        bt.setOnAction(e->actionPerformed(e, tf, hbox));
        paneForTextField.setRight(bt);


        GridPane gridPane = new GridPane();
        gridPane.add(hbox, 0,0);
        gridPane.add(paneForTextField, 0,1);

        Scene scene = new Scene(gridPane, 800, 400);
        primaryStage.setTitle("Count Characters");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void actionPerformed(ActionEvent e, TextField textfield, HBox hbox) {
        hbox.getChildren().clear();

        String fileName = textfield.getText();
        //textfield.clear();

        File sourceFile = new File(fileName);
        FileInputStream fileStream;
        try
        {
            fileStream = new FileInputStream(sourceFile);

        }
        catch (Exception err)
        {
            System.out.println("No such file. Please type again.");
            return;
        }

        int[] wordCount = new int[26];

        String words;

        Scanner input = new Scanner(fileStream);

        while (input.hasNext()){
            words = input.next();
            words = words.toUpperCase();


            for (int i=0; i<words.length(); i++){
                int char_index = (int) words.charAt(i);
                if (char_index>64 && char_index<91){
                    wordCount[char_index-65]++;
                }
            }
        }

        Rectangle recs[] = new Rectangle[26];
        int height_base = 10;
        int width_base = 10;

        Label labels[] = new Label[26];
        for (int i=0; i<26; i++){
            // Rectangle
            recs[i] = new Rectangle(width_base, (double) wordCount[i] * height_base);
            recs[i].setFill(Color.WHITE);
            recs[i].setStroke(Color.BLACK);

            // Label
            char letter = (char)(65+i);
            String s = Character.toString(letter);
            labels[i] = new Label(s);
            labels[i].setGraphic(recs[i]);
            labels[i].setContentDisplay(TOP);
            hbox.getChildren().add(labels[i]);

            System.out.println(s + ":" + wordCount[i]);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
=======
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.util.Scanner;

import static javafx.scene.control.ContentDisplay.TOP;


public class ReadTextAndCount extends Application{



    @Override
    public void start(Stage primaryStage) throws Exception{

        String fileName = "";

        // Rectangle part
        HBox hbox = new HBox();
        hbox.setPrefHeight(600);
        hbox.setAlignment(Pos.BOTTOM_LEFT);

        // File name input field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5,5,5,5));
        paneForTextField.setStyle("-fx-border-color: black");
        Label label = new Label("File Name:");
        paneForTextField.setLeft(label);
        paneForTextField.setMargin(label, new Insets(5,5,5,5));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BASELINE_LEFT);
        tf.setPrefColumnCount(30);
        tf.setOnAction(e->actionPerformed(e, tf, hbox));
        paneForTextField.setCenter(tf);

        Button bt = new Button("View");
        bt.setOnAction(e->actionPerformed(e, tf, hbox));
        paneForTextField.setRight(bt);


        GridPane gridPane = new GridPane();
        gridPane.add(hbox, 0,0);
        gridPane.add(paneForTextField, 0,1);

        Scene scene = new Scene(gridPane, 800, 400);
        primaryStage.setTitle("Count Characters");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void actionPerformed(ActionEvent e, TextField textfield, HBox hbox) {
        hbox.getChildren().clear();

        String fileName = textfield.getText();
        //textfield.clear();

        File sourceFile = new File(fileName);
        FileInputStream fileStream;
        try
        {
            fileStream = new FileInputStream(sourceFile);

        }
        catch (Exception err)
        {
            System.out.println("No such file. Please type again.");
            return;
        }

        int[] wordCount = new int[26];

        String words;

        Scanner input = new Scanner(fileStream);

        while (input.hasNext()){
            words = input.next();
            words = words.toUpperCase();


            for (int i=0; i<words.length(); i++){
                int char_index = (int) words.charAt(i);
                if (char_index>64 && char_index<91){
                    wordCount[char_index-65]++;
                }
            }
        }

        Rectangle recs[] = new Rectangle[26];
        int height_base = 10;
        int width_base = 10;

        Label labels[] = new Label[26];
        for (int i=0; i<26; i++){
            // Rectangle
            recs[i] = new Rectangle(width_base, (double) wordCount[i] * height_base);
            recs[i].setFill(Color.WHITE);
            recs[i].setStroke(Color.BLACK);

            // Label
            char letter = (char)(65+i);
            String s = Character.toString(letter);
            labels[i] = new Label(s);
            labels[i].setGraphic(recs[i]);
            labels[i].setContentDisplay(TOP);
            hbox.getChildren().add(labels[i]);

            System.out.println(s + ":" + wordCount[i]);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
>>>>>>> e086f85376e27b7f5a3b309755c116e8daf10d44
=======
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.util.Scanner;

import static javafx.scene.control.ContentDisplay.TOP;


public class ReadTextAndCount extends Application{



    @Override
    public void start(Stage primaryStage) throws Exception{

        String fileName = "";

        // Rectangle part
        HBox hbox = new HBox();
        hbox.setPrefHeight(600);
        hbox.setAlignment(Pos.BOTTOM_LEFT);

        // File name input field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5,5,5,5));
        paneForTextField.setStyle("-fx-border-color: black");
        Label label = new Label("File Name:");
        paneForTextField.setLeft(label);
        paneForTextField.setMargin(label, new Insets(5,5,5,5));

        TextField tf = new TextField();
        tf.setAlignment(Pos.BASELINE_LEFT);
        tf.setPrefColumnCount(30);
        tf.setOnAction(e->actionPerformed(e, tf, hbox));
        paneForTextField.setCenter(tf);

        Button bt = new Button("View");
        bt.setOnAction(e->actionPerformed(e, tf, hbox));
        paneForTextField.setRight(bt);


        GridPane gridPane = new GridPane();
        gridPane.add(hbox, 0,0);
        gridPane.add(paneForTextField, 0,1);

        Scene scene = new Scene(gridPane, 800, 400);
        primaryStage.setTitle("Count Characters");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void actionPerformed(ActionEvent e, TextField textfield, HBox hbox) {
        hbox.getChildren().clear();

        String fileName = textfield.getText();
        //textfield.clear();

        File sourceFile = new File(fileName);
        FileInputStream fileStream;
        try
        {
            fileStream = new FileInputStream(sourceFile);

        }
        catch (Exception err)
        {
            System.out.println("No such file. Please type again.");
            return;
        }

        int[] wordCount = new int[26];

        String words;

        Scanner input = new Scanner(fileStream);

        while (input.hasNext()){
            words = input.next();
            words = words.toUpperCase();


            for (int i=0; i<words.length(); i++){
                int char_index = (int) words.charAt(i);
                if (char_index>64 && char_index<91){
                    wordCount[char_index-65]++;
                }
            }
        }

        Rectangle recs[] = new Rectangle[26];
        int height_base = 10;
        int width_base = 10;

        Label labels[] = new Label[26];
        for (int i=0; i<26; i++){
            // Rectangle
            recs[i] = new Rectangle(width_base, (double) wordCount[i] * height_base);
            recs[i].setFill(Color.WHITE);
            recs[i].setStroke(Color.BLACK);

            // Label
            char letter = (char)(65+i);
            String s = Character.toString(letter);
            labels[i] = new Label(s);
            labels[i].setGraphic(recs[i]);
            labels[i].setContentDisplay(TOP);
            hbox.getChildren().add(labels[i]);

            System.out.println(s + ":" + wordCount[i]);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
>>>>>>> e086f85376e27b7f5a3b309755c116e8daf10d44
}