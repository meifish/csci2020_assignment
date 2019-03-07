<<<<<<< HEAD
<<<<<<< HEAD
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.Random;

public class CircleTrack extends Application {

    Random random = new Random();

    private int findX(int y, double radius){
        /** findX() will return x coordinate, provided 'y coordinate' and the 'radius'.
         * @params int y: given y coordinate.
         * @params int radius: given radius of a circle
         * @params centerX: the x coordinate of the center of the circle.
         * @return int x: the corresponding x coordinate.
         *
         * It is found by the right triangle theory:
         *    hypotenus ^ 2 = leg_1 ^ 2 + leg_2 ^ 2
         *
         * In a circle, the radius is the hypotenus, and the x and y lengths are the two legs.
         * Thus, given the radius (hypotenus) and one leg (y coordinate), we can find the other leg (x coordinate.)
         */

        int x;

        // 1. Right triangle theorem to find the x length.
        int x_length = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow((double)Math.abs(y), 2));

        // 2. Find coordinate x:
        //    x can be to the left or right of the center of circle.
        //    e.g., y = -50, radius = 100, center = (0,0), then we can find (x,y) to be either (50, -50) or (-50, -50).
        x = (random.nextInt(2) == 1)? -x_length : x_length;

        return x;
    }


    StringBinding getAngleProperty(Circle[] points, int centerPointIndex){

        /**
         * @params Circle[] points: the array of three circles objects, which are the red dots to drag.
         * @return String angle: the calculated angle, by line p0->p1, and line p0->p2.
         */

        DoubleProperty points_X[] = new DoubleProperty[points.length];
        DoubleProperty points_Y[] = new DoubleProperty[points.length];
        for (int i = 0; i < points.length; i++) {
            points_X[i] = points[i].centerXProperty();
            points_Y[i] = points[i].centerYProperty();
        }

        if (centerPointIndex != 0){
            DoubleProperty tempX = points_X[0];
            DoubleProperty tempY = points_Y[0];
            points_X[0] = points[centerPointIndex].centerXProperty();
            points_Y[0] = points[centerPointIndex].centerYProperty();
            points_X[centerPointIndex] = tempX;
            points_Y[centerPointIndex] = tempY;
        }
        // Lengths of the triangle (Euclidean distance):
        // Opposite side: Distance between p2 and p3


        //angle_string = String.format("%.1f", angle);
        StringBinding angleBinding = new StringBinding() {

            {
                for (int i=0; i<points_X.length; i++){
                    super.bind(points_X[i]);
                    super.bind(points_Y[i]);
                }
            }

            @Override
            protected String computeValue() {

                double a = Math.sqrt(Math.pow(points_X[1].getValue()-points_X[2].getValue(), 2) +
                        Math.pow(points_Y[1].getValue()-points_Y[2].getValue(), 2));
                // Neighbor Side 1: Distance between p1 and p2
                double b = Math.sqrt(Math.pow(points_X[0].getValue()-points_X[1].getValue(), 2) +
                        Math.pow(points_Y[0].getValue()-points_Y[1].getValue(), 2));
                // Neighbor Side 2: Distance between p1 and p3
                double c = Math.sqrt(Math.pow(points_X[0].getValue()-points_X[2].getValue(), 2) +
                        Math.pow(points_Y[0].getValue()-points_Y[2].getValue(), 2));

                double angle = Math.toDegrees(Math.acos((a*a - b*b - c*c) / (-2*b*c)));

                return String.format("%.1f", angle);
            }
        };

        return angleBinding;
    }



    double radius = 100;
    double label_radius = 85;
    Pane pane = new Pane();
    Scene scene = new Scene(pane, 400, 400);
    DoubleBinding HalfSceneWidth = scene.widthProperty().divide(2);
    DoubleBinding HalfSceneHeight = scene.heightProperty().divide(2);

    Circle points[] = {new Circle(), new Circle(), new Circle()};

    Label angleOne = new Label();
    Label angleTwo = new Label();
    Label angleThree = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception{

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a circle
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.centerXProperty().bind(scene.widthProperty().divide(2));
        circle.centerYProperty().bind(scene.heightProperty().divide(2));
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create 3 random small circle (i.e., red points to drag) on the main circle's circumference.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 1. Find coordinates of the 3 points.
        //         Pick random y and then find its corresponding x which makes it falls on the circle's circumference.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        double y_coordinate[] = new double[3];
        double x_coordinate[] = new double[3];

        for (int i = 0; i < y_coordinate.length; i++) {

            // y range is from the top to the bottom of the circle: [0, 2r]
            int rangeY = (int) (2 * radius);

            // y coordinate ranges from [-radius, radius].
            int y = random.nextInt(rangeY+1) - (int)radius;

            y_coordinate[i] = (double)y;
            //System.out.println("y_cor:" + y_coordinate[i]);

            x_coordinate[i] = (double)findX(y, radius);
            //System.out.println("x_cor:" + x_coordinate[i]);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 2. Creates 3 small circles (points) based on the coordinate.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (int i = 0; i < points.length; i++) {
            points[i].setStroke(Color.BLACK);
            points[i].setFill(Color.RED);
            points[i].setRadius(5);
            points[i].centerXProperty().bind(HalfSceneWidth.add(x_coordinate[i]));
            points[i].centerYProperty().bind(HalfSceneHeight.add(y_coordinate[i]));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 3. Creates 3 lines that link each pair of points.
        Line lineOne = new Line();
        Line lineTwo = new Line();
        Line lineThree = new Line();

        lineOne.startXProperty().bind(points[0].centerXProperty());
        lineOne.startYProperty().bind(points[0].centerYProperty());
        lineOne.endXProperty().bind(points[1].centerXProperty());
        lineOne.endYProperty().bind(points[1].centerYProperty());

        lineTwo.startXProperty().bind(points[1].centerXProperty());
        lineTwo.startYProperty().bind(points[1].centerYProperty());
        lineTwo.endXProperty().bind(points[2].centerXProperty());
        lineTwo.endYProperty().bind(points[2].centerYProperty());

        lineThree.startXProperty().bind(points[2].centerXProperty());
        lineThree.startYProperty().bind(points[2].centerYProperty());
        lineThree.endXProperty().bind(points[0].centerXProperty());
        lineThree.endYProperty().bind(points[0].centerYProperty());


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4. Creates labels whose position would be bound to an invisible smaller inner circle.
        //         i.e., If point_1 has (x, y) coordinate, then
        //                  x = cosTheta * r   => cosTheta = x/r
        //                  y = sinTheta * r   => sinTheta = y/r
        //               If the label_1 is following a smaller inner circle, whose center aligns to the circle,
        //                  and the label has (x', y') coordinate, then
        //                  x' = cosTheta * r'  = x/r * r'
        //                  y' = sinTheta * r'  = y/r * r'
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Binding label's text.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        angleOne.textProperty().bind(getAngleProperty(points, 0));
        angleTwo.textProperty().bind(getAngleProperty(points, 1));
        angleThree.textProperty().bind(getAngleProperty(points, 2));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Binding label's position.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        angleOne.layoutXProperty().bind(
                points[0].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleOne.widthProperty().divide(2)));
        angleOne.layoutYProperty().bind(
                points[0].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleOne.heightProperty().divide(2)));

        angleTwo.layoutXProperty().bind(
                points[1].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleTwo.widthProperty().divide(2)));
        angleTwo.layoutYProperty().bind(
                points[1].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleTwo.heightProperty().divide(2)));

        angleThree.layoutXProperty().bind(
                points[2].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleThree.widthProperty().divide(2)));
        angleThree.layoutYProperty().bind(
                points[2].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleThree.heightProperty().divide(2)));


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4. Set Mouse Drag.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        DragHandler drag_handler = new DragHandler();
        points[0].setOnMouseDragged(drag_handler);
        points[1].setOnMouseDragged(drag_handler);
        points[2].setOnMouseDragged(drag_handler);


        pane.getChildren().addAll(angleOne, angleTwo, angleThree, circle, lineOne, lineTwo, lineThree, points[0], points[1], points[2]);

        primaryStage.setTitle("Circle Drag");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    class DragHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent e) {
            // Provided the mouse location (x,y), we can imagine the cursor landed on an invisible outer circle, which
            // is aligned to the center pane. When dragging, the cursor starts at pressing on one of the red dot, and
            // starts to drag in all directions: Up/Down/Right/Left, and not necessarily strictly following the circle
            // circumference. Thus, the first step is to find the corresponding cosTheta vs. sinTheta w.r.t (x,y) - the
            // cursor position. Then apply the cosTheta vs. sinTheta to the main circle to find the corresponding
            // (x', y') sitting on the circle's circumference, to be the dot's position.
            // That is to say, (x, y) and (x', y') share the same radian but different radius.

            double mouseX = e.getX() - HalfSceneWidth.getValue();
            double mouseY = e.getY() - HalfSceneHeight.getValue();
            double disToCenter = Math.sqrt(Math.pow(mouseX, 2) + Math.pow(mouseY, 2));
            double cosTheta = mouseX / disToCenter;
            double sinTheta = mouseY / disToCenter;

            DoubleProperty mouseXProperty =new SimpleDoubleProperty(cosTheta * radius);
            DoubleProperty mouseYProperty =new SimpleDoubleProperty(sinTheta * radius);

            if (!(e.getSource() instanceof Circle)){
                System.out.println("Wrong Source");
            }

            Circle source = (Circle) e.getSource(); // source: points[i]
            source.centerXProperty().bind(HalfSceneWidth.add(mouseXProperty));
            source.centerYProperty().bind(HalfSceneHeight.add(mouseYProperty));

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
=======
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.Random;

public class CircleTrack extends Application {

    Random random = new Random();

    private int findX(int y, double radius){
        /** findX() will return x coordinate, provided 'y coordinate' and the 'radius'.
         * @params int y: given y coordinate.
         * @params int radius: given radius of a circle
         * @params centerX: the x coordinate of the center of the circle.
         * @return int x: the corresponding x coordinate.
         *
         * It is found by the right triangle theory:
         *    hypotenus ^ 2 = leg_1 ^ 2 + leg_2 ^ 2
         *
         * In a circle, the radius is the hypotenus, and the x and y lengths are the two legs.
         * Thus, given the radius (hypotenus) and one leg (y coordinate), we can find the other leg (x coordinate.)
         */

        int x;

        // 1. Right triangle theorem to find the x length.
        int x_length = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow((double)Math.abs(y), 2));

        // 2. Find coordinate x:
        //    x can be to the left or right of the center of circle.
        //    e.g., y = -50, radius = 100, center = (0,0), then we can find (x,y) to be either (50, -50) or (-50, -50).
        x = (random.nextInt(2) == 1)? -x_length : x_length;

        return x;
    }


    StringBinding getAngleProperty(Circle[] points, int centerPointIndex){

        /**
         * @params Circle[] points: the array of three circles objects, which are the red dots to drag.
         * @return String angle: the calculated angle, by line p0->p1, and line p0->p2.
         */

        DoubleProperty points_X[] = new DoubleProperty[points.length];
        DoubleProperty points_Y[] = new DoubleProperty[points.length];
        for (int i = 0; i < points.length; i++) {
            points_X[i] = points[i].centerXProperty();
            points_Y[i] = points[i].centerYProperty();
        }

        if (centerPointIndex != 0){
            DoubleProperty tempX = points_X[0];
            DoubleProperty tempY = points_Y[0];
            points_X[0] = points[centerPointIndex].centerXProperty();
            points_Y[0] = points[centerPointIndex].centerYProperty();
            points_X[centerPointIndex] = tempX;
            points_Y[centerPointIndex] = tempY;
        }
        // Lengths of the triangle (Euclidean distance):
        // Opposite side: Distance between p2 and p3


        //angle_string = String.format("%.1f", angle);
        StringBinding angleBinding = new StringBinding() {

            {
                for (int i=0; i<points_X.length; i++){
                    super.bind(points_X[i]);
                    super.bind(points_Y[i]);
                }
            }

            @Override
            protected String computeValue() {

                double a = Math.sqrt(Math.pow(points_X[1].getValue()-points_X[2].getValue(), 2) +
                        Math.pow(points_Y[1].getValue()-points_Y[2].getValue(), 2));
                // Neighbor Side 1: Distance between p1 and p2
                double b = Math.sqrt(Math.pow(points_X[0].getValue()-points_X[1].getValue(), 2) +
                        Math.pow(points_Y[0].getValue()-points_Y[1].getValue(), 2));
                // Neighbor Side 2: Distance between p1 and p3
                double c = Math.sqrt(Math.pow(points_X[0].getValue()-points_X[2].getValue(), 2) +
                        Math.pow(points_Y[0].getValue()-points_Y[2].getValue(), 2));

                double angle = Math.toDegrees(Math.acos((a*a - b*b - c*c) / (-2*b*c)));

                return String.format("%.1f", angle);
            }
        };

        return angleBinding;
    }



    double radius = 100;
    double label_radius = 85;
    Pane pane = new Pane();
    Scene scene = new Scene(pane, 400, 400);
    DoubleBinding HalfSceneWidth = scene.widthProperty().divide(2);
    DoubleBinding HalfSceneHeight = scene.heightProperty().divide(2);

    Circle points[] = {new Circle(), new Circle(), new Circle()};

    Label angleOne = new Label();
    Label angleTwo = new Label();
    Label angleThree = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception{

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a circle
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.centerXProperty().bind(scene.widthProperty().divide(2));
        circle.centerYProperty().bind(scene.heightProperty().divide(2));
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create 3 random small circle (i.e., red points to drag) on the main circle's circumference.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 1. Find coordinates of the 3 points.
        //         Pick random y and then find its corresponding x which makes it falls on the circle's circumference.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        double y_coordinate[] = new double[3];
        double x_coordinate[] = new double[3];

        for (int i = 0; i < y_coordinate.length; i++) {

            // y range is from the top to the bottom of the circle: [0, 2r]
            int rangeY = (int) (2 * radius);

            // y coordinate ranges from [-radius, radius].
            int y = random.nextInt(rangeY+1) - (int)radius;

            y_coordinate[i] = (double)y;
            //System.out.println("y_cor:" + y_coordinate[i]);

            x_coordinate[i] = (double)findX(y, radius);
            //System.out.println("x_cor:" + x_coordinate[i]);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 2. Creates 3 small circles (points) based on the coordinate.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (int i = 0; i < points.length; i++) {
            points[i].setStroke(Color.BLACK);
            points[i].setFill(Color.RED);
            points[i].setRadius(5);
            points[i].centerXProperty().bind(HalfSceneWidth.add(x_coordinate[i]));
            points[i].centerYProperty().bind(HalfSceneHeight.add(y_coordinate[i]));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 3. Creates 3 lines that link each pair of points.
        Line lineOne = new Line();
        Line lineTwo = new Line();
        Line lineThree = new Line();

        lineOne.startXProperty().bind(points[0].centerXProperty());
        lineOne.startYProperty().bind(points[0].centerYProperty());
        lineOne.endXProperty().bind(points[1].centerXProperty());
        lineOne.endYProperty().bind(points[1].centerYProperty());

        lineTwo.startXProperty().bind(points[1].centerXProperty());
        lineTwo.startYProperty().bind(points[1].centerYProperty());
        lineTwo.endXProperty().bind(points[2].centerXProperty());
        lineTwo.endYProperty().bind(points[2].centerYProperty());

        lineThree.startXProperty().bind(points[2].centerXProperty());
        lineThree.startYProperty().bind(points[2].centerYProperty());
        lineThree.endXProperty().bind(points[0].centerXProperty());
        lineThree.endYProperty().bind(points[0].centerYProperty());


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4. Creates labels whose position would be bound to an invisible smaller inner circle.
        //         i.e., If point_1 has (x, y) coordinate, then
        //                  x = cosTheta * r   => cosTheta = x/r
        //                  y = sinTheta * r   => sinTheta = y/r
        //               If the label_1 is following a smaller inner circle, whose center aligns to the circle,
        //                  and the label has (x', y') coordinate, then
        //                  x' = cosTheta * r'  = x/r * r'
        //                  y' = sinTheta * r'  = y/r * r'
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Binding label's text.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        angleOne.textProperty().bind(getAngleProperty(points, 0));
        angleTwo.textProperty().bind(getAngleProperty(points, 1));
        angleThree.textProperty().bind(getAngleProperty(points, 2));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Binding label's position.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        angleOne.layoutXProperty().bind(
                points[0].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleOne.widthProperty().divide(2)));
        angleOne.layoutYProperty().bind(
                points[0].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleOne.heightProperty().divide(2)));

        angleTwo.layoutXProperty().bind(
                points[1].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleTwo.widthProperty().divide(2)));
        angleTwo.layoutYProperty().bind(
                points[1].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleTwo.heightProperty().divide(2)));

        angleThree.layoutXProperty().bind(
                points[2].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleThree.widthProperty().divide(2)));
        angleThree.layoutYProperty().bind(
                points[2].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleThree.heightProperty().divide(2)));


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4. Set Mouse Drag.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        DragHandler drag_handler = new DragHandler();
        points[0].setOnMouseDragged(drag_handler);
        points[1].setOnMouseDragged(drag_handler);
        points[2].setOnMouseDragged(drag_handler);


        pane.getChildren().addAll(angleOne, angleTwo, angleThree, circle, lineOne, lineTwo, lineThree, points[0], points[1], points[2]);

        primaryStage.setTitle("Circle Drag");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    class DragHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent e) {
            // Provided the mouse location (x,y), we can imagine the cursor landed on an invisible outer circle, which
            // is aligned to the center pane. When dragging, the cursor starts at pressing on one of the red dot, and
            // starts to drag in all directions: Up/Down/Right/Left, and not necessarily strictly following the circle
            // circumference. Thus, the first step is to find the corresponding cosTheta vs. sinTheta w.r.t (x,y) - the
            // cursor position. Then apply the cosTheta vs. sinTheta to the main circle to find the corresponding
            // (x', y') sitting on the circle's circumference, to be the dot's position.
            // That is to say, (x, y) and (x', y') share the same radian but different radius.

            double mouseX = e.getX() - HalfSceneWidth.getValue();
            double mouseY = e.getY() - HalfSceneHeight.getValue();
            double disToCenter = Math.sqrt(Math.pow(mouseX, 2) + Math.pow(mouseY, 2));
            double cosTheta = mouseX / disToCenter;
            double sinTheta = mouseY / disToCenter;

            DoubleProperty mouseXProperty =new SimpleDoubleProperty(cosTheta * radius);
            DoubleProperty mouseYProperty =new SimpleDoubleProperty(sinTheta * radius);

            if (!(e.getSource() instanceof Circle)){
                System.out.println("Wrong Source");
            }

            Circle source = (Circle) e.getSource(); // source: points[i]
            source.centerXProperty().bind(HalfSceneWidth.add(mouseXProperty));
            source.centerYProperty().bind(HalfSceneHeight.add(mouseYProperty));

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
>>>>>>> e086f85376e27b7f5a3b309755c116e8daf10d44
=======
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.Random;

public class CircleTrack extends Application {

    Random random = new Random();

    private int findX(int y, double radius){
        /** findX() will return x coordinate, provided 'y coordinate' and the 'radius'.
         * @params int y: given y coordinate.
         * @params int radius: given radius of a circle
         * @params centerX: the x coordinate of the center of the circle.
         * @return int x: the corresponding x coordinate.
         *
         * It is found by the right triangle theory:
         *    hypotenus ^ 2 = leg_1 ^ 2 + leg_2 ^ 2
         *
         * In a circle, the radius is the hypotenus, and the x and y lengths are the two legs.
         * Thus, given the radius (hypotenus) and one leg (y coordinate), we can find the other leg (x coordinate.)
         */

        int x;

        // 1. Right triangle theorem to find the x length.
        int x_length = (int) Math.sqrt(Math.pow(radius, 2) - Math.pow((double)Math.abs(y), 2));

        // 2. Find coordinate x:
        //    x can be to the left or right of the center of circle.
        //    e.g., y = -50, radius = 100, center = (0,0), then we can find (x,y) to be either (50, -50) or (-50, -50).
        x = (random.nextInt(2) == 1)? -x_length : x_length;

        return x;
    }


    StringBinding getAngleProperty(Circle[] points, int centerPointIndex){

        /**
         * @params Circle[] points: the array of three circles objects, which are the red dots to drag.
         * @return String angle: the calculated angle, by line p0->p1, and line p0->p2.
         */

        DoubleProperty points_X[] = new DoubleProperty[points.length];
        DoubleProperty points_Y[] = new DoubleProperty[points.length];
        for (int i = 0; i < points.length; i++) {
            points_X[i] = points[i].centerXProperty();
            points_Y[i] = points[i].centerYProperty();
        }

        if (centerPointIndex != 0){
            DoubleProperty tempX = points_X[0];
            DoubleProperty tempY = points_Y[0];
            points_X[0] = points[centerPointIndex].centerXProperty();
            points_Y[0] = points[centerPointIndex].centerYProperty();
            points_X[centerPointIndex] = tempX;
            points_Y[centerPointIndex] = tempY;
        }
        // Lengths of the triangle (Euclidean distance):
        // Opposite side: Distance between p2 and p3


        //angle_string = String.format("%.1f", angle);
        StringBinding angleBinding = new StringBinding() {

            {
                for (int i=0; i<points_X.length; i++){
                    super.bind(points_X[i]);
                    super.bind(points_Y[i]);
                }
            }

            @Override
            protected String computeValue() {

                double a = Math.sqrt(Math.pow(points_X[1].getValue()-points_X[2].getValue(), 2) +
                        Math.pow(points_Y[1].getValue()-points_Y[2].getValue(), 2));
                // Neighbor Side 1: Distance between p1 and p2
                double b = Math.sqrt(Math.pow(points_X[0].getValue()-points_X[1].getValue(), 2) +
                        Math.pow(points_Y[0].getValue()-points_Y[1].getValue(), 2));
                // Neighbor Side 2: Distance between p1 and p3
                double c = Math.sqrt(Math.pow(points_X[0].getValue()-points_X[2].getValue(), 2) +
                        Math.pow(points_Y[0].getValue()-points_Y[2].getValue(), 2));

                double angle = Math.toDegrees(Math.acos((a*a - b*b - c*c) / (-2*b*c)));

                return String.format("%.1f", angle);
            }
        };

        return angleBinding;
    }



    double radius = 100;
    double label_radius = 85;
    Pane pane = new Pane();
    Scene scene = new Scene(pane, 400, 400);
    DoubleBinding HalfSceneWidth = scene.widthProperty().divide(2);
    DoubleBinding HalfSceneHeight = scene.heightProperty().divide(2);

    Circle points[] = {new Circle(), new Circle(), new Circle()};

    Label angleOne = new Label();
    Label angleTwo = new Label();
    Label angleThree = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception{

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create a circle
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Circle circle = new Circle();
        circle.setRadius(radius);
        circle.centerXProperty().bind(scene.widthProperty().divide(2));
        circle.centerYProperty().bind(scene.heightProperty().divide(2));
        circle.setFill(Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);



        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Create 3 random small circle (i.e., red points to drag) on the main circle's circumference.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 1. Find coordinates of the 3 points.
        //         Pick random y and then find its corresponding x which makes it falls on the circle's circumference.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        double y_coordinate[] = new double[3];
        double x_coordinate[] = new double[3];

        for (int i = 0; i < y_coordinate.length; i++) {

            // y range is from the top to the bottom of the circle: [0, 2r]
            int rangeY = (int) (2 * radius);

            // y coordinate ranges from [-radius, radius].
            int y = random.nextInt(rangeY+1) - (int)radius;

            y_coordinate[i] = (double)y;
            //System.out.println("y_cor:" + y_coordinate[i]);

            x_coordinate[i] = (double)findX(y, radius);
            //System.out.println("x_cor:" + x_coordinate[i]);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 2. Creates 3 small circles (points) based on the coordinate.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        for (int i = 0; i < points.length; i++) {
            points[i].setStroke(Color.BLACK);
            points[i].setFill(Color.RED);
            points[i].setRadius(5);
            points[i].centerXProperty().bind(HalfSceneWidth.add(x_coordinate[i]));
            points[i].centerYProperty().bind(HalfSceneHeight.add(y_coordinate[i]));
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 3. Creates 3 lines that link each pair of points.
        Line lineOne = new Line();
        Line lineTwo = new Line();
        Line lineThree = new Line();

        lineOne.startXProperty().bind(points[0].centerXProperty());
        lineOne.startYProperty().bind(points[0].centerYProperty());
        lineOne.endXProperty().bind(points[1].centerXProperty());
        lineOne.endYProperty().bind(points[1].centerYProperty());

        lineTwo.startXProperty().bind(points[1].centerXProperty());
        lineTwo.startYProperty().bind(points[1].centerYProperty());
        lineTwo.endXProperty().bind(points[2].centerXProperty());
        lineTwo.endYProperty().bind(points[2].centerYProperty());

        lineThree.startXProperty().bind(points[2].centerXProperty());
        lineThree.startYProperty().bind(points[2].centerYProperty());
        lineThree.endXProperty().bind(points[0].centerXProperty());
        lineThree.endYProperty().bind(points[0].centerYProperty());


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4. Creates labels whose position would be bound to an invisible smaller inner circle.
        //         i.e., If point_1 has (x, y) coordinate, then
        //                  x = cosTheta * r   => cosTheta = x/r
        //                  y = sinTheta * r   => sinTheta = y/r
        //               If the label_1 is following a smaller inner circle, whose center aligns to the circle,
        //                  and the label has (x', y') coordinate, then
        //                  x' = cosTheta * r'  = x/r * r'
        //                  y' = sinTheta * r'  = y/r * r'
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Binding label's text.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        angleOne.textProperty().bind(getAngleProperty(points, 0));
        angleTwo.textProperty().bind(getAngleProperty(points, 1));
        angleThree.textProperty().bind(getAngleProperty(points, 2));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Binding label's position.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        angleOne.layoutXProperty().bind(
                points[0].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleOne.widthProperty().divide(2)));
        angleOne.layoutYProperty().bind(
                points[0].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleOne.heightProperty().divide(2)));

        angleTwo.layoutXProperty().bind(
                points[1].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleTwo.widthProperty().divide(2)));
        angleTwo.layoutYProperty().bind(
                points[1].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleTwo.heightProperty().divide(2)));

        angleThree.layoutXProperty().bind(
                points[2].centerXProperty().subtract(HalfSceneWidth).divide(radius).multiply(label_radius).add(HalfSceneWidth).subtract(angleThree.widthProperty().divide(2)));
        angleThree.layoutYProperty().bind(
                points[2].centerYProperty().subtract(HalfSceneHeight).divide(radius).multiply(label_radius).add(HalfSceneHeight).subtract(angleThree.heightProperty().divide(2)));


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Step 4. Set Mouse Drag.
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        DragHandler drag_handler = new DragHandler();
        points[0].setOnMouseDragged(drag_handler);
        points[1].setOnMouseDragged(drag_handler);
        points[2].setOnMouseDragged(drag_handler);


        pane.getChildren().addAll(angleOne, angleTwo, angleThree, circle, lineOne, lineTwo, lineThree, points[0], points[1], points[2]);

        primaryStage.setTitle("Circle Drag");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    class DragHandler implements EventHandler<MouseEvent>{
        @Override
        public void handle(MouseEvent e) {
            // Provided the mouse location (x,y), we can imagine the cursor landed on an invisible outer circle, which
            // is aligned to the center pane. When dragging, the cursor starts at pressing on one of the red dot, and
            // starts to drag in all directions: Up/Down/Right/Left, and not necessarily strictly following the circle
            // circumference. Thus, the first step is to find the corresponding cosTheta vs. sinTheta w.r.t (x,y) - the
            // cursor position. Then apply the cosTheta vs. sinTheta to the main circle to find the corresponding
            // (x', y') sitting on the circle's circumference, to be the dot's position.
            // That is to say, (x, y) and (x', y') share the same radian but different radius.

            double mouseX = e.getX() - HalfSceneWidth.getValue();
            double mouseY = e.getY() - HalfSceneHeight.getValue();
            double disToCenter = Math.sqrt(Math.pow(mouseX, 2) + Math.pow(mouseY, 2));
            double cosTheta = mouseX / disToCenter;
            double sinTheta = mouseY / disToCenter;

            DoubleProperty mouseXProperty =new SimpleDoubleProperty(cosTheta * radius);
            DoubleProperty mouseYProperty =new SimpleDoubleProperty(sinTheta * radius);

            if (!(e.getSource() instanceof Circle)){
                System.out.println("Wrong Source");
            }

            Circle source = (Circle) e.getSource(); // source: points[i]
            source.centerXProperty().bind(HalfSceneWidth.add(mouseXProperty));
            source.centerYProperty().bind(HalfSceneHeight.add(mouseYProperty));

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
>>>>>>> e086f85376e27b7f5a3b309755c116e8daf10d44
}