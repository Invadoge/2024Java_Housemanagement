package Housemanager.UI;

import java.math.BigDecimal;

import javax.annotation.Nonnegative;

import Housemanager.DAO.BaseDAO;
import Housemanager.DAO.DAOFactory;
import Housemanager.entities.ApartmentResidents;
import Housemanager.entities.Enums.ResidentType;
import jakarta.validation.constraints.Positive;
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

public class AddMenu extends Stage {

    public AddMenu() {
        setTitle("House Manager - Add Menu");

        GridPane mainGrid = new GridPane();
        mainGrid.setVgap(10);
        mainGrid.setHgap(10);
        mainGrid.setAlignment(Pos.CENTER);
        mainGrid.setPadding(new Insets(20));

        // Create a ComboBox for entity selection
        ComboBox<String> entityComboBox = new ComboBox<>();
        entityComboBox.setPromptText("Select Entity");
        ObservableList<String> entityOptions = FXCollections.observableArrayList("Apartment",
                "ApartmentResidents",
                "Building",
                "BuildingManagement",
                "MonthlyPayment");
        entityComboBox.setItems(entityOptions);

        mainGrid.add(entityComboBox, 0, 0);

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20));

        mainGrid.add(grid, 0, 1);
        entityComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            grid.getChildren().clear(); // Clear previous fields

            // Get the selected entity class
            Class<?> entityClass = UIHelper.getEntityClass(newValue);

            if (entityClass != null) {
                // Create labels and text fields dynamically based on entity attributes
                int row = 0;
                for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
                    // Filter based on DisplayField tag
                    if (field.isAnnotationPresent(DisplayField.class)) {
                        Label label = new Label(field.getName() + ":");
                        if (entityClass.equals(ApartmentResidents.class) && field.getName().equals("residentType")) {
                            ComboBox<ResidentType> residentTypeComboBox = new ComboBox<>();
                            residentTypeComboBox.getItems().addAll(ResidentType.values());
                            grid.add(label, 0, row);
                            grid.add(residentTypeComboBox, 1, row);
                        } else {
                            TextField textField = new TextField();
                            grid.add(label, 0, row);
                            grid.add(textField, 1, row);
                        }
                        row++;
                    }
                }
            }
        });

        // #region Buttons
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> addEntity(entityComboBox, grid));

        // Create buttons and layout for AddMenu
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> close());
        // #endregion Buttons

        mainGrid.add(addButton, 0, 2);
        mainGrid.add(backButton, 0, 3);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(mainGrid);

        // Set scene
        Scene scene = new Scene(layout, 400, 500);
        setScene(scene);
    }

    private void addEntity(ComboBox<String> entityComboBox, GridPane grid) {
        String selectedEntity = entityComboBox.getSelectionModel().getSelectedItem();

        if (selectedEntity != null) {
            Class<?> entityClass = UIHelper.getEntityClass(selectedEntity);

            Object entity = createEntityFromInput(entityClass, grid);

            BaseDAO dao = DAOFactory.getDao(entityClass);
            dao.saveOrUpdate(entity);
        }
        close();
    }

    private Object createEntityFromInput(Class<?> entityClass, GridPane grid) {
        try {
            Object entity = entityClass.getDeclaredConstructor().newInstance();

            int row = 0;

            for (java.lang.reflect.Field field : entityClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(DisplayField.class)) {
                    Object value;
                    Node inputNode = grid.getChildren().get(row * 2 + 1);

                    if (inputNode instanceof TextField) {
                        String input = ((TextField) inputNode).getText();
                        value = convertToFieldType(field.getType(), input);

                        // Additional validation for @Positive annotation
                        if (field.isAnnotationPresent(Positive.class)
                                || field.isAnnotationPresent(Nonnegative.class)
                                        && value instanceof Number) {

                            if (((Number) value).doubleValue() < 0) {
                                throw new IllegalArgumentException(
                                        "Field " + field.getName() + " must be a positive value.");
                            }
                        }
                    } else if (inputNode instanceof ComboBox) {
                        // Assuming ComboBox is used only for ResidentType
                        value = ((ComboBox<?>) inputNode).getValue();
                    } else {
                        value = null;
                    }

                    field.setAccessible(true);
                    field.set(entity, value);

                    row++;
                }
            }

            return entity;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object convertToFieldType(Class<?> fieldType, String input) {
        // Implement logic to convert String input to the specified fieldType
        // For simplicity, assuming the input is valid and of the correct type
        if (fieldType.equals(String.class)) {
            return input;
        } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
            return Integer.parseInt(input);
        } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
            return Long.parseLong(input);
        } else if (fieldType.equals(BigDecimal.class)) {
            return new BigDecimal(input);
        } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
            return Short.parseShort(input);
        } else if (fieldType.equals(ResidentType.class)) {
            // Handle enums
            return Enum.valueOf(ResidentType.class, input);
        } else {
            // Handle other data types as needed
            return null;
        }
    }
}