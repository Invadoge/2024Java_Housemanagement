/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package Housemanager;

import Housemanager.UI.AddMenu;
import Housemanager.UI.InfoMenu;
import Housemanager.UI.RemoveMenu;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {

    private AddMenu addMenu;
    private RemoveMenu removeMenu;
    private InfoMenu infoMenu;
    public static void main(String[] args) {
        DatabasePopulator.populate();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("House Manager - Main Menu");

        // Create menus
        addMenu = new AddMenu();
        removeMenu = new RemoveMenu();
        infoMenu = new InfoMenu();

        //Create grid
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        // Create buttons
        Button addButton = new Button("Add...");
        Button removeButton = new Button("Remove");
        Button infoButton = new Button("Info");
        Button quitButton = new Button("Quit");

        grid.addColumn(0, addButton, removeButton, infoButton, quitButton);

        // Set button actions
        addButton.setOnAction(e -> openAddMenu());
        removeButton.setOnAction(e -> openRemoveMenu());
        infoButton.setOnAction(e -> openInfoMenu());
        quitButton.setOnAction(e -> primaryStage.close());

        // Create layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(grid);

        // Set scene
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void openAddMenu() {
        addMenu.show();
    }

    private void openRemoveMenu() {
        removeMenu.show();
    }

    private void openInfoMenu() {
        infoMenu.show();
    }

}
