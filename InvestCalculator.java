import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class InvestCalculator extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        GridPane gridpane = new GridPane();
        gridpane.setPadding(new Insets(10,5,10,5));
        gridpane.setVgap(5);
        gridpane.setHgap(10);

        // Contents in the gridpane. (4 sets of text input box and its title.)
        Label investAmount_label = new Label("Investment Amount");
        TextField investAmount_box = new TextField();
        Label years_label = new Label("Years");
        TextField years_box = new TextField();
        Label annualRate_label = new Label("Annual Interest Rate");
        TextField annualRate_box = new TextField();
        Label futureValue_label = new Label("Future Value");
        TextField futureValue_box = new TextField();
        futureValue_box.setDisable(true);
        Button bt_calculate = new Button("Calculate");

        // Adds the content nodes into the gridpane with its responding position.
        gridpane.add(investAmount_label, 0, 0);
        gridpane.add(investAmount_box, 1, 0);
        gridpane.add(years_label, 0, 1);
        gridpane.add(years_box, 1, 1);
        gridpane.add(annualRate_label, 0, 2);
        gridpane.add(annualRate_box, 1, 2);
        gridpane.add(futureValue_label, 0, 3);
        gridpane.add(futureValue_box, 1, 3);
        gridpane.add(bt_calculate, 1, 4);

        GridPane.setHalignment(bt_calculate, HPos.RIGHT);

        // Set button action
        bt_calculate.setOnAction(e -> {

            float amount = Float.parseFloat(investAmount_box.getText());
            float year = Float.parseFloat(years_box.getText());
            float annualRate = Float.parseFloat(annualRate_box.getText());
            float futureRate = amount * (float) Math.pow((1 + (annualRate / 12.0f) * 0.01), year * 12);
            String strFloat = String.format("%.2f", futureRate);
            futureValue_box.setText(strFloat);
        });

        primaryStage.setTitle("Invest Calculator");
        primaryStage.setScene(new Scene(gridpane));
        primaryStage.show();
    }


}

