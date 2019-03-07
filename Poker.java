import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Poker extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        // Step 1: Create Image() objects for 54 cards.
        Image cardPool[] = new Image[54];
        for (int i = 1; i < cardPool.length+1; i++) {
            cardPool[i-1] = new Image("file:img/" + i + ".png");
        }

        // Step 2: Generate 3 ImageViews that contains random pick Image from CardPool
        Random rand = new Random();

        ImageView img_cardOne = new ImageView(cardPool[rand.nextInt(54)]);
        ImageView img_cardTwo = new ImageView(cardPool[rand.nextInt(54)]);
        ImageView img_cardThree = new ImageView(cardPool[rand.nextInt(54)]);

        // Step 3: Add 3 ImageViews to corresponding Label.
        Label cardOne = new Label("", img_cardOne);
        Label cardTwo = new Label("", img_cardTwo);
        Label cardThree = new Label("",img_cardThree);

        // Step 4: Add Labels to a Horizontal pane.
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(5,5,5,5));
        hbox.setSpacing(5);
        hbox.getChildren().addAll(cardOne, cardTwo, cardThree);

        // Step 5: Add to Stage.
        primaryStage.setTitle("Poker Display");
        primaryStage.setScene(new Scene(hbox));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
