package Housemanager.DAO;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Housemanager.DTO.BuildingDTO;
import Housemanager.entities.Building;
import Housemanager.entities.BuildingManagement;

public class BuildingDAO extends BaseDAO {

    public BuildingDAO() {
        super();
    }

    public BuildingDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void addBuilding(Building building) {
        Transaction transaction = null;
        try (Session session = getSession()) {

            transaction = session.beginTransaction();

            // Find the employee with the least managed buildings
            BuildingManagementDAO buildingManagementDAO = new BuildingManagementDAO();
            BuildingManagement leastBusyEmployee = buildingManagementDAO.findEmployeeWithLeastManagedBuildings();
            building.setManagedByEmployee(leastBusyEmployee.getEmployeeId());
            leastBusyEmployee.incrementBuildings();

            // If there are no employees, it will throw an error and go to catch block
            leastBusyEmployee.getBuildings().add(building);

            // Usually merge should handle persistent instances as well
            // Writing the if guard for good practice
            if (!session.contains(leastBusyEmployee))
                session.merge(leastBusyEmployee);

            session.persist(building);

            transaction.commit();

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    public void removeBuilding(long buildingId) {
        Transaction transaction = null;
        try (Session session = getSession()) {

            transaction = session.beginTransaction();

            Building buildingToRemove = session.get(Building.class, buildingId);

            buildingToRemove.getManagedBy().decrementBuildings();
            if (buildingToRemove != null) {
                session.remove(buildingToRemove);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void saveOrUpdate(Object entity) {
        if (!(entity instanceof Building)) {
            throw new IllegalArgumentException("BuildingDAO::Invalid entity type!");
        }

        Building building = (Building) entity;
        addBuilding(building);
    }

    @Override
    public void removeById(String[] ids) {
        if (ids.length != 1) {
            throw new IllegalArgumentException("Exactly one id is required!");
        }

        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();

            Long buildingId = Long.valueOf(ids[0]);
            Building entityToRemove = session.get(Building.class, buildingId);

            if (entityToRemove != null) {
                session.remove(entityToRemove);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BigDecimal[] getTaxesById(Long buildingId) {
        try (Session session = getSession()) {
            String queryString = "SELECT b.petTax, b.elevatorTax FROM Building b WHERE b.id = :buildingId";
            Query<Object[]> query = session.createQuery(queryString, Object[].class);
            query.setParameter("buildingId", buildingId);

            Object[] result = query.uniqueResult();

            if (result != null) {
                return new BigDecimal[] { (BigDecimal) result[0], (BigDecimal) result[1] };
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<BuildingDTO> getAllBuildingDTOsWithEmployeeNames() {
        try (Session session = getSession()) {
            String queryString = "SELECT NEW BuildingDTO(bm.employeeName, b.id, b.city, b.buildingAddress) " +
                    "FROM Building b " +
                    "JOIN b.managedBy bm";

            Query<BuildingDTO> query = session.createQuery(queryString, BuildingDTO.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
