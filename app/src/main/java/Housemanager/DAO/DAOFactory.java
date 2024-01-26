package Housemanager.DAO;

import Housemanager.entities.Apartment;
import Housemanager.entities.ApartmentResidents;
import Housemanager.entities.Building;
import Housemanager.entities.BuildingManagement;
import Housemanager.entities.MonthlyPayment;

public class DAOFactory {
    public static BaseDAO getDao(Class<?> entityClass) {
        // Implement logic to determine and return the appropriate DAO
        // You can have a mapping of entity class to DAO
        if (entityClass.equals(Apartment.class)) {
            return new ApartmentDAO();
        } else if (entityClass.equals(Building.class)) {
            return new BuildingDAO();
        } else if (entityClass.equals(BuildingManagement.class)){
            return new BuildingManagementDAO();
        }else if (entityClass.equals(ApartmentResidents.class)){
            return new ApartmentResidentsDAO();
        }else if (entityClass.equals(MonthlyPayment.class)){
            return new MonthlyPaymentDAO();
        }

        // Return a default DAO or throw an exception if needed
        throw new UnsupportedOperationException("No DAO found for entity: " + entityClass.getName());
    }
}
