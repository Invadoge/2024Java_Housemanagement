package Housemanager.DAO;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Housemanager.DTO.EmployeeDTO;
import Housemanager.entities.BuildingManagement;

public class BuildingManagementDAO extends BaseDAO {

    public BuildingManagementDAO() {
        super();
    }

    public BuildingManagementDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public BuildingManagement findEmployeeWithLeastManagedBuildings() {
        BuildingManagement result = null;
        try (Session session = getSession()) {
            String hql = "SELECT bm FROM BuildingManagement bm " +
                    "ORDER BY bm.managedBuildings ASC";

            Query<BuildingManagement> query = session.createQuery(hql, BuildingManagement.class);
            query.setMaxResults(1); // Retrieve only the first result.

            result = query.uniqueResult();
        }

        return result;
    }

    @Override
    public void removeById(String[] ids) {
        if (ids.length != 1) {
            throw new IllegalArgumentException("Exactly one id is required!");
        }

        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();

            Long managementId = Long.valueOf(ids[0]);
            BuildingManagement entityToRemove = session.get(BuildingManagement.class, managementId);

            if (entityToRemove != null) {
                session.remove(entityToRemove);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<EmployeeDTO> getAllEmployees(String sortName, String sortProfits) {
        try (Session session = getSession()) {
            String queryString = "SELECT NEW " +
                    " EmployeeDTO(bm.employeeId, bm.employeeName, bm.companyName, SUM(mp.amount)) " +
                    "FROM BuildingManagement bm " +
                    "JOIN bm.buildings b " +
                    "LEFT JOIN MonthlyPayment mp ON b.id = mp.buildingId " +
                    "GROUP BY bm.employeeId, bm.employeeName, bm.companyName";
    
            boolean orderByAdded = false;
    
            if ("Ascending".equals(sortName)) {
                queryString += " ORDER BY bm.employeeName " + "ASC";
                orderByAdded = true;
            } else if ("Descending".equals(sortName)) {
                queryString += " ORDER BY bm.employeeName " + "DESC";
                orderByAdded = true;
            }
    
            if ("Ascending".equals(sortProfits)) {
                queryString += orderByAdded ? ", " : " ORDER BY ";
                queryString += "SUM(mp.amount) " + "ASC";
            } else if ("Descending".equals(sortProfits)) {
                queryString += orderByAdded ? ", " : " ORDER BY ";
                queryString += "SUM(mp.amount) " + "DESC";
            }
    
            Query<EmployeeDTO> query = session.createQuery(queryString, EmployeeDTO.class);
    
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}