package Game;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.util.Random;

/**
 * A JavaFX application that creates a battleship game interface.
 */
public class Battleship extends Application {

    private int boxSize = 5; // Initial size of the box
    private GridPane gridPane1;
    private GridPane gridPane2;
    private Label sizeLabel;
    private Label colorLabel;
    private Label languageLabel;
    private Label designButton;
    private Label placeShipButton;
    private ColorPicker colorPicker;
    private boolean placingShip = false;
    private int shipSize = 1;

    /**
     * The entry point for the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {

        HBox root = new HBox(); // Use HBox to hold the elements horizontally
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50); // Add spacing between the elements

        // Create the first box
        gridPane1 = createSquareButtonBox();
        root.getChildren().add(gridPane1);

        // Create the second box
        gridPane2 = createSquareButtonBox();
        root.getChildren().add(gridPane2);


        // Create ComboBox for box size
        ComboBox<Integer> sizeComboBox = new ComboBox<>();
        sizeComboBox.getItems().addAll(5, 10, 15, 20);
        sizeComboBox.setValue(boxSize);
        sizeComboBox.setOnAction(e -> {
            boxSize = sizeComboBox.getValue();
            updateBoxSize();
        });

        // Create ColorPicker for customizing the right box color
        colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> changeBoxColor(colorPicker.getValue()));

        // Create ComboBox and label for language selection
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "French");
        languageComboBox.setValue("English");
        languageComboBox.setOnAction(e -> setLanguage(languageComboBox.getValue()));

        Label languageLabel = new Label("Language:");

        // Add ComboBox and label to the scene
        HBox sizeBox = new HBox(10);
        sizeBox.setAlignment(Pos.CENTER);
        sizeLabel = new Label("Box Size:");
        sizeBox.getChildren().addAll(sizeLabel, sizeComboBox);

        HBox colorBox = new HBox(10);
        colorBox.setAlignment(Pos.CENTER);
        colorLabel = new Label("Box Color:");
        colorBox.getChildren().addAll(colorLabel, colorPicker);

        HBox languageBox = new HBox( 10);
        languageBox.setAlignment(Pos.CENTER);
        languageBox.getChildren().addAll(languageLabel, languageComboBox);

        VBox controlBox = new VBox(10);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.getChildren().addAll(sizeBox, colorBox, languageBox);

        Button designButton = new Button("Design");
        designButton.setOnAction(e -> designShips());

        Button placeShipButton = new Button("Place Ship");
        placeShipButton.setOnAction(e -> {
            placingShip = !placingShip;
            shipSize = 1;
            if (placingShip) {
                placeShipButton.setText("Cancel Placement");
            } else {
                placeShipButton.setText("Place Ship");
            }
        });

 
        VBox vbox= new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(controlBox, designButton, placeShipButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(root);
        borderPane.setRight(vbox);
        BorderPane.setMargin(root, new Insets(10));
        BorderPane.setMargin(vbox, new Insets(10));

        Scene scene = new Scene(borderPane);
        primaryStage.setTitle("Battleship Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    /**
     * Creates a grid pane with square buttons.
     *
     * @return the grid pane
     */
    private GridPane createSquareButtonBox() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        for (int i = 0; i < boxSize; i++) {
            for (int j = 0; j < boxSize; j++) {
                Button button = new Button();
                button.setPrefSize(50, 50);
                button.setOnAction(e -> handleButtonClick(button));
                gridPane.add(button, i, j);
            }
        }

        return gridPane;
    }
    
    /**
     * Handles the button click event.
     *
     * @param button the clicked button
     */
    private void handleButtonClick(Button button) {
        if (placingShip) {
            if (shipSize <= boxSize) {
                button.setStyle("-fx-background-color: black");
                shipSize++;
            }
        }
    }

    private void updateBoxSize() {
        updateBoxSize(gridPane1, boxSize);
        updateBoxSize(gridPane2, boxSize);
    }

    private void updateBoxSize(GridPane gridPane, int size) {
        gridPane.getChildren().clear(); // Clear the existing buttons and labels

        // Add labels to the first row and column
        gridPane.addRow(0, createLabels(size));
        gridPane.addColumn(0, createLabels(size));

        // Add buttons to the grid pane
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button button = createSquareButton();

                // Apply custom style to the button
                button.setStyle("-fx-background-color: #CCCCCC; -fx-font-size: 12px;");
                int row = i;
                int col = j;
                button.setOnAction(e -> {
                    if (placingShip) {
                        placeShip(row, col);
                    } else {
                        String message = "Button at (" + row + ", " + col + ") was pressed";
                        System.out.println(message);
                    }
                });

                gridPane.add(button, j + 1, i + 1); // Add buttons to the grid pane
            }
        }
    }

    /**
     * Creates an array of labels for the button box.
     *
     * @param size the size of the button box
     * @return an array of labels
     */
    private Label[] createLabels(int size) {
        Label[] labels = new Label[size];
        for (int i = 0; i < size; i++) {
            Label label = createCenteredLabel(Character.toString((char) ('A' + i)));
            labels[i] = label;
        }
        return labels;
    }

    /**
     * Creates a square button.
     *
     * @return the newly created Button
     */
    private Button createSquareButton() {
        Button button = new Button();
        button.setPrefSize(50, 50); // Change this to adjust the size of each button
        return button;
    }

    /**
     * Creates a centered label with the given text.
     *
     * @param text the text of the label
     * @return the newly created Label
     */
    private Label createCenteredLabel(String text) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setPrefSize(50, 50); // Change this to adjust the size of each label
        return label;
    }

    /**
     * Changes the color of the buttons in the second box.
     *
     * @param color the new color
     */
    private void changeBoxColor(Color color) {
        BackgroundFill fill = new BackgroundFill(color, null, null);
        Background background = new Background(fill);
        for (Node button : gridPane2.getChildren()) {
            ((Region) button).setBackground(background);
        }
    }
    /**
     * Sets the language of the game interface.
     *
     * @param language the selected language ("English" or "French")
     */
    private void setLanguage(String language) {
        switch (language) {
            case "English":
                sizeLabel.setText("Box Size:");
                colorLabel.setText("Box Color:");
                languageLabel.setText("Language:");
                designButton.setText("Design");
                placeShipButton.setText("Place Ship");
                break;
            case "French":
                sizeLabel.setText("Taille de la boîte:");
                colorLabel.setText("Couleur de la boîte:");
                languageLabel.setText("Langue:");
                designButton.setText("Conception");
                placeShipButton.setText("Placer le navire");
                break;
            default:
                break;
        }
    }
    
 
    /**
     * Designates the ship placements on the button box.
     */
    private void designShips() {
        Random random = new Random();
            int dimension = boxSize;

            for (int i = 1; i <= dimension; i++) {
                for (int j = 1; j < dimension - i + 1; j++) {
                    int randRow = random.nextInt(dimension) + 1;
                    int randCol = random.nextInt(dimension) + 1;
                    createRandomBoat(i, randRow, randCol);
                }
            }
        }
        

    /**
     * Designates a cell as part of the user's ship placement.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
    private void placeShip(int row, int col) {
        GridPane gridPane = gridPane2; // Use the second grid pane for ship placement

        // Check if the selected cell is already occupied
        if (gridPane.getChildren().stream()
                .filter(node -> GridPane.getRowIndex(node) == row + 1 && GridPane.getColumnIndex(node) == col + 1 && !(node instanceof Button))
                .findFirst()
                .isPresent()) {
            System.out.println("Cannot place ship here. Cell is already occupied.");
            return;
        }

        // Create a new ship button
        Button shipButton = createSquareButton();
        shipButton.setStyle("-fx-background-color: blue; -fx-font-size: 12px;");
        shipButton.setOnAction(e -> {
            gridPane.getChildren().remove(shipButton);
        });

        // Add the ship button to the grid pane
        gridPane.add(shipButton, col + 1, row + 1);

        // Increment the ship size
        shipSize++;

        // Check if the ship placement is complete
        if (shipSize > boxSize) {
            placingShip = false;
            shipSize = 1;
        }
    }

    private void createRandomBoat(int boatSize, int row, int col) {
        for (int i = 0; i < boatSize; i++) {
            Button button = (Button) gridPane1.getChildren().get((col + i) * (boxSize + 1) + row + 1);
            button.setStyle("-fx-background-color: #0000FF; -fx-font-size: 12px;");
        }
    }


    
    /**
     * The main method that launches the JavaFX application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}