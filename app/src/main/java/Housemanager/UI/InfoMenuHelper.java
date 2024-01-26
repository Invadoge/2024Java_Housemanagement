package Housemanager.UI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import Housemanager.DAO.ApartmentDAO;
import Housemanager.DAO.ApartmentResidentsDAO;
import Housemanager.DAO.BuildingDAO;
import Housemanager.DAO.BuildingManagementDAO;
import Housemanager.DAO.MonthlyPaymentDAO;
import Housemanager.DTO.ApartmentDTO;
import Housemanager.DTO.ApartmentResidentsDTO;
import Housemanager.DTO.BuildingDTO;
import Housemanager.DTO.EmployeeDTO;
import Housemanager.DTO.MonthlyPaymentDTO;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class InfoMenuHelper {

    // #region ADDCONTENT
    public static void addApartmentContent(GridPane swappingGrid) {
        // Create a TextField for entering the building ID
        TextField buildingIdTextField = new TextField();
        buildingIdTextField.setPromptText("Enter Building ID");
        swappingGrid.add(buildingIdTextField, 0, 0);

        // Create a Button to get apartments by ID
        Button getApartmentsButton = new Button("Get Apartments");
        getApartmentsButton.setOnAction(e -> getApartmentsById(buildingIdTextField.getText()));
        swappingGrid.add(getApartmentsButton, 0, 1);
    }

    public static void addBuildingContent(GridPane swappingGrid) {
        Button getBuildingsButton = new Button("Get Buildings with Employee Names");
        getBuildingsButton.setOnAction(e -> getBuildingsWithEmployeeNames());

        swappingGrid.add(getBuildingsButton, 0, 3);
    }

    public static void addApartmentResidentsContent(GridPane swappingGrid) {
        // Create a TextField for entering the building ID
        TextField buildingIdTextField = new TextField();
        buildingIdTextField.setPromptText("Enter Building ID");
        swappingGrid.add(buildingIdTextField, 0, 0);

        // Create two ComboBoxes for sorting options
        ComboBox<String> sortByNameComboBox = new ComboBox<>(
                FXCollections.observableArrayList("Do not", "Ascending", "Descending"));
        sortByNameComboBox.setPromptText("Sort By Name");
        ComboBox<String> sortByAgeComboBox = new ComboBox<>(
                FXCollections.observableArrayList("Do not", "Ascending", "Descending"));
        sortByAgeComboBox.setPromptText("Sort By Age");

        // Create a Button to get apartment residents based on building ID
        Button getResidentsButton = new Button("Get Apartment Residents");
        getResidentsButton.setOnAction(e -> getApartmentResidents(buildingIdTextField.getText(),
                sortByNameComboBox.getValue(), sortByAgeComboBox.getValue()));

        // Create labels for ComboBoxes
        Label sortByNameLabel = new Label("Sort by Name:");
        Label sortByAgeLabel = new Label("Sort by Age:");

        // Create an HBox for ComboBoxes and labels
        HBox sortOptionsBox = new HBox(10);
        sortOptionsBox.getChildren().addAll(sortByNameLabel, sortByNameComboBox, sortByAgeLabel, sortByAgeComboBox);

        // Add elements to the grid
        swappingGrid.add(sortOptionsBox, 0, 1);
        swappingGrid.add(getResidentsButton, 0, 2);
    }

    public static void addBuildingManagementContent(GridPane swappingGrid) {
        // Create two ComboBoxes for sorting options
        ComboBox<String> sortByNameComboBox = new ComboBox<>(
                FXCollections.observableArrayList("Do not", "Ascending", "Descending"));
        sortByNameComboBox.setPromptText("Sort By Name");
        ComboBox<String> sortByProfitComboBox = new ComboBox<>(
                FXCollections.observableArrayList("Do not", "Ascending", "Descending"));
        sortByProfitComboBox.setPromptText("Sort By Profit");

        // Create a Button to get apartment residents based on building ID
        Button getResidentsButton = new Button("Get Employees");
        getResidentsButton.setOnAction(e -> getEmployees(
                sortByNameComboBox.getValue(), sortByProfitComboBox.getValue()));

        // Create labels for ComboBoxes
        Label sortByNameLabel = new Label("Sort by Name:");
        Label sortByProfitLabel = new Label("Sort by profit:");

        // Create an HBox for ComboBoxes and labels
        HBox sortOptionsBox = new HBox(10);
        sortOptionsBox.getChildren().addAll(sortByNameLabel, sortByNameComboBox, sortByProfitLabel,
                sortByProfitComboBox);

        // Add elements to the grid
        swappingGrid.add(sortOptionsBox, 0, 1);
        swappingGrid.add(getResidentsButton, 0, 2);
    }

    public static void addMonthlyPaymentContent(GridPane swappingGrid) {
        Button saveMonthlyPaymentsButton = new Button("Save Monthly Payments");
        saveMonthlyPaymentsButton.setOnAction(e -> saveMonthlyPaymentsToFile());
        swappingGrid.add(saveMonthlyPaymentsButton, 0, 3);
    }
    // #endregion ADDCONTENT

    // #region BUTTON FUNCTIONS
    private static void getApartmentsById(String buildingId) {
        try {
            Long id = Long.parseLong(buildingId);
            ApartmentDAO apartmentDAO = new ApartmentDAO();
            List<ApartmentDTO> apartments = apartmentDAO.getApartmentsByBuildingId(id);

            DTOViewer.displayDTOs(apartments);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Building ID format");
        }
    }

    private static void getApartmentResidents(String buildingId, String sortName, String sortAge) {
        try {
            Long id = Long.parseLong(buildingId);

            ApartmentResidentsDAO apartmentResidentsDAO = new ApartmentResidentsDAO();

            List<ApartmentResidentsDTO> residents = apartmentResidentsDAO.getResidentsDTOByBuildingId(id, sortName,
                    sortAge);

            DTOViewer.displayDTOs(residents);

        } catch (NumberFormatException e) {
            System.err.println("Invalid Building ID format");
        }
    }

    private static void getEmployees(String sortByName, String sortByProfit) {
        try {

            BuildingManagementDAO buildingManagementDAO = new BuildingManagementDAO();

            List<EmployeeDTO> employees = buildingManagementDAO.getAllEmployees(sortByName, sortByProfit);

            DTOViewer.displayDTOs(employees);
        } catch (NumberFormatException e) {
            System.err.println("Invalid Building ID format");
        }
    }

    private static void saveMonthlyPaymentsToFile() {
        MonthlyPaymentDAO monthlyPaymentDAO = new MonthlyPaymentDAO();
        List<MonthlyPaymentDTO> monthlyPayments = monthlyPaymentDAO.getAllMonthlyPaymentsWithEmployeeInfo();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("monthly_payments.txt", false))) {
            for (MonthlyPaymentDTO payment : monthlyPayments) {
                writer.write(payment.toString());
                writer.newLine();
            }
            System.out.println("Monthly Payments saved to monthly_payments.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getBuildingsWithEmployeeNames() {
        BuildingDAO buildingDAO = new BuildingDAO();
        List<BuildingDTO> buildingDTOs = buildingDAO.getAllBuildingDTOsWithEmployeeNames();
        DTOViewer.displayDTOs(buildingDTOs);
    }
    // #endregion BUTTON FUNCTIONS
}
