package Housemanager.UI;

import java.util.ArrayList;
import java.util.List;

import Housemanager.DAO.BaseDAO;
import Housemanager.DAO.DAOFactory;
import jakarta.persistence.Id;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RemoveMenu extends Stage {

    public RemoveMenu() {
        setTitle("House Manager - Remove Menu");

        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setPadding(new Insets(20));

        // Create ComboBox for entity selection
        ComboBox<String> entityComboBox = new ComboBox<>();
        entityComboBox.setPromptText("Select Entity");
        ObservableList<String> entityOptions = FXCollections.observableArrayList(
                "Apartment",
                "ApartmentResidents",
                "Building",
                "BuildingManagement",
                "MonthlyPayment");
        entityComboBox.setItems(entityOptions);
        mainGrid.add(entityComboBox, 0, 0);
        GridPane grid = new GridPane();
        entityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            grid.getChildren().clear(); // Clear previous fields

            // Get the selected entity class
            Class<?> entityClass = UIHelper.getEntityClass(newValue);

            if (entityClass != null) {
                // Create labels and text fields dynamically based on entity attributes
                int row = 0;
                for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
                    // Filter based on Id tag
                    if (field.isAnnotationPresent(Id.class)) {
                        Label label = new Label(field.getName() + ":");
                        TextField textField = new TextField();
                        grid.add(label, 0, row);
                        grid.add(textField, 1, row);
                        row++;
                    }
                }
            }
        });
        mainGrid.add(grid, 0,2);

        // Create Remove button
        Button removeButton = new Button("Remove");
        mainGrid.add(removeButton, 0, 3);
        removeButton.setOnAction(e -> removeEntity(entityComboBox, grid));

        // Create Back Button
        Button backButton = new Button("Back");
        mainGrid.add(backButton, 0, 4);
        backButton.setOnAction(e -> close());

        // Create layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(mainGrid);

        // Set scene
        Scene scene = new Scene(layout, 300, 200);
        setScene(scene);
    }

    private String[] createIdList(GridPane grid) {
        List<String> idList = new ArrayList<>();
    
        for (Node node : grid.getChildren()) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                String id = textField.getText().trim();
                if (!id.isEmpty()) {
                    idList.add(id);
                }
            }
        }
    
        return idList.toArray(new String[0]);
    }
    
    private void removeEntity(ComboBox<String> entityComboBox, GridPane grid) {
        String selectedEntity = entityComboBox.getSelectionModel().getSelectedItem();

        if (selectedEntity != null) {
            Class<?> entityClass = UIHelper.getEntityClass(selectedEntity);

            String idList[] = createIdList(grid);
            BaseDAO dao = DAOFactory.getDao(entityClass);
            dao.removeById(idList);
        }
        close();
    }
}