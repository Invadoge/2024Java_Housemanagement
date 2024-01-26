package Housemanager.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InfoMenu extends Stage {
    //#region CONSTRUCTOR
    public InfoMenu() {
        setTitle("House Manager - Information Menu");

        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setPadding(new Insets(20));

        ComboBox<String> entityComboBox = new ComboBox<>();
        entityComboBox.setPromptText("Select Entity");
        ObservableList<String> entityOptions = FXCollections.observableArrayList("Apartment",
                "ApartmentResidents",
                "Building",
                "BuildingManagement",
                "MonthlyPayment");
        entityComboBox.setItems(entityOptions);


        GridPane swappingGrid = new GridPane();
        // Add a listener to the entityComboBox selection
        entityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateSwappingGridContent(newValue, swappingGrid);
        });

        // Create buttons and layout for AddMenu
        Button backButton = new Button("Back");
        // Set button actions
        backButton.setOnAction(e -> close());

        mainGrid.add(entityComboBox, 0, 0);
        mainGrid.add(swappingGrid, 0, 1);
        mainGrid.add(backButton, 0, 2);
        // Create layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(mainGrid);

        // Set scene
        Scene scene = new Scene(layout, 500, 300);
        setScene(scene);
    }

    //#endregion CONSTRUCTOR
    

    private void updateSwappingGridContent(String selectedEntity, GridPane swappingGrid) {
        // Clear the current content of swappingGrid
        swappingGrid.getChildren().clear();

        // Add new content based on the selected entity
        if ("Building".equals(selectedEntity)) {
            InfoMenuHelper.addBuildingContent(swappingGrid);
        } else if ("Apartment".equals(selectedEntity)) {
            InfoMenuHelper.addApartmentContent(swappingGrid);
        } else if ("ApartmentResidents".equals(selectedEntity)) {
            InfoMenuHelper.addApartmentResidentsContent(swappingGrid);
        } else if ("BuildingManagement".equals(selectedEntity)) {
            InfoMenuHelper.addBuildingManagementContent(swappingGrid);
        } else if ("MonthlyPayment".equals(selectedEntity)) {
            InfoMenuHelper.addMonthlyPaymentContent(swappingGrid);
        }
    }
    
}
