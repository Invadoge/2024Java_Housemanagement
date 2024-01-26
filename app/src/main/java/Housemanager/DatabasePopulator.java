package Housemanager;

import java.math.BigDecimal;
import java.time.LocalDate;

import Housemanager.DAO.ApartmentDAO;
import Housemanager.DAO.ApartmentResidentsDAO;
import Housemanager.DAO.BuildingDAO;
import Housemanager.DAO.BuildingManagementDAO;
import Housemanager.DAO.MonthlyPaymentDAO;
import Housemanager.entities.Apartment;
import Housemanager.entities.ApartmentResidents;
import Housemanager.entities.Building;
import Housemanager.entities.BuildingManagement;
import Housemanager.entities.MonthlyPayment;
import Housemanager.entities.Enums.ResidentType;

public class DatabasePopulator {
    public static void populate() {
        // #region BuildingManagement
        BuildingManagementDAO bmd = new BuildingManagementDAO();
        if (!bmd.hasElements("BuildingManagement")) {
            BuildingManagement[] buildingManagements = {
                    new BuildingManagement("Georgi Kanchev", "NeverLand"),
                    new BuildingManagement("John Doe", "City Towers"),
                    new BuildingManagement("Alice Johnson", "Urban Residences"),
                    new BuildingManagement("Bob Smith", "Skyline Estates")
            };

            for (BuildingManagement buildingManagement : buildingManagements) {
                bmd.saveOrUpdate(buildingManagement);
            }
        }
        // #endregion BuildingManagement

        // #region Building
        BuildingDAO bd = new BuildingDAO();
        if (!bd.hasElements("Building")) {
            Building[] buildings = {
                    new Building("Cherkovna", "Sofia", BigDecimal.valueOf(1), BigDecimal.valueOf(2)),
                    new Building("Broadway", "New York", BigDecimal.valueOf(3), BigDecimal.valueOf(4)),
                    new Building("High Street", "London", BigDecimal.valueOf(5), BigDecimal.valueOf(6)),
                    new Building("Passeig de Gracia", "Barcelona", BigDecimal.valueOf(7), BigDecimal.valueOf(8))
            };

            for (Building building : buildings) {
                bd.saveOrUpdate(building);
            }
        }
        // #endregion Building

        // #region Apartment
        ApartmentDAO ad = new ApartmentDAO();
        if (!ad.hasElements("Apartment")) {
            Apartment[] apartments = {
                    new Apartment((long) 1, (short) 1, (short) 1, BigDecimal.valueOf(50), "Georgi Kanchev"),
                    new Apartment((long) 1, (short) 2, (short) 1, BigDecimal.valueOf(70), "John Doe"),
                    new Apartment((long) 2, (short) 1, (short) 1, BigDecimal.valueOf(60), "Alice Johnson"),
                    new Apartment((long) 2, (short) 2, (short) 1, BigDecimal.valueOf(80), "Bob Smith")
            };

            for (Apartment apartment : apartments) {
                ad.saveOrUpdate(apartment);
            }
        }
        // #endregion Apartment

        // #region ApartmentResidents
        ApartmentResidentsDAO ard = new ApartmentResidentsDAO();
        if (!ard.hasElements("ApartmentResidents")) {
            ApartmentResidents[] residents = {
                    new ApartmentResidents((long) 1, (short) 1, "Klara", (short) 4, ResidentType.PET),
                    new ApartmentResidents((long) 1, (short) 1, "Georgi", (short) 43, ResidentType.ELEVATOR_USER),
                    new ApartmentResidents((long) 1, (short) 2, "John", (short) 25, ResidentType.REGULAR),
                    new ApartmentResidents((long) 1, (short) 2, "Alice", (short) 30, ResidentType.CHILD)
            };

            for (ApartmentResidents resident : residents) {
                ard.saveOrUpdate(resident);
            }
        }
        // #endregion ApartmentResidents

        // #region MonthlyPayment
        MonthlyPaymentDAO mpd = new MonthlyPaymentDAO();
        if (!mpd.hasElements("MonthlyPayment")) {
            MonthlyPayment[] monthlyPayments = {
                    new MonthlyPayment((long) 1, (short) 1, "Georgi", LocalDate.of(2023, 11, 15),
                            BigDecimal.valueOf(0)),
                    new MonthlyPayment((long) 1, (short) 1, "Georgi", LocalDate.of(2023, 12, 20),
                            BigDecimal.valueOf(7)),
                    new MonthlyPayment((long) 1, (short) 1, "Georgi", LocalDate.of(2024, 01, 20),
                            BigDecimal.valueOf(6)),
                    new MonthlyPayment((long) 1, (short) 2, "John", LocalDate.of(2024, 01, 25),
                            BigDecimal.valueOf(8))
            };

            for (MonthlyPayment monthlyPayment : monthlyPayments) {
                mpd.saveOrUpdate(monthlyPayment);
            }
        }
        // #endregion MonthlyPayment
    }
}
