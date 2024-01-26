package Housemanager.DAO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Housemanager.DTO.ApartmentResidentsDTO;
import Housemanager.entities.ApartmentResidents;
import Housemanager.entities.Enums.ResidentType;

public class ApartmentResidentsDAO extends BaseDAO {
    public ApartmentResidentsDAO() {
        super();
    }

    public ApartmentResidentsDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public BigDecimal calculateResidentsCosts(Long buildingId, Short apartmentId, BigDecimal pricePerPerson,
            BigDecimal pricePerPet) {
        if (pricePerPerson == null ||
                pricePerPerson.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "ApartmentResidentsDAO::Price per person must be a positive non-null value.");
        } else if (pricePerPet == null ||
                pricePerPet.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "ApartmentResidentsDAO::Price per pet must be a positive non-null value.");
        }

        BigDecimal sum = BigDecimal.ZERO;

        try (Session session = getSession()) {
            String hqlElevatorUser = "SELECT COUNT(ar) FROM ApartmentResidents ar " +
                    "WHERE ar.apartmentNumber = :apartmentId " +
                    "AND ar.buildingId = :buildingId " +
                    "AND ar.residentType = :residentType";

            Query<Long> queryElevatorUser = session.createQuery(hqlElevatorUser, Long.class);
            queryElevatorUser.setParameter("apartmentId", apartmentId);
            queryElevatorUser.setParameter("buildingId", buildingId);
            queryElevatorUser.setParameter("residentType", ResidentType.ELEVATOR_USER);

            Long elevatorUserCount = queryElevatorUser.uniqueResult();

            String hqlPet = "SELECT COUNT(ar) FROM ApartmentResidents ar " +
                    "WHERE ar.apartmentNumber = :apartmentId " +
                    "AND ar.buildingId = :buildingId " +
                    "AND ar.residentType = :residentType";

            Query<Long> queryPet = session.createQuery(hqlPet, Long.class);
            queryPet.setParameter("apartmentId", apartmentId);
            queryPet.setParameter("buildingId", buildingId);
            queryPet.setParameter("residentType", ResidentType.PET);

            Long petCount = queryPet.uniqueResult();

            sum = BigDecimal.valueOf(elevatorUserCount).multiply(pricePerPerson)
                    .add(BigDecimal.valueOf(petCount).multiply(pricePerPet));
        }

        return sum;
    }

    @Override
    public void removeById(String[] ids) {
        if (ids.length != 1) {
            throw new IllegalArgumentException("Exactly one id is required!");
        }

        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();

            Long residentId = Long.valueOf(ids[0]);
            ApartmentResidents entityToRemove = session.get(ApartmentResidents.class, residentId);

            if (entityToRemove != null) {
                session.remove(entityToRemove);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ApartmentResidentsDTO> getResidentsDTOByBuildingId(Long buildingId, String sortName, String sortAge) {
        try (Session session = getSession()) {
            String queryString = "SELECT NEW ApartmentResidentsDTO(a.buildingId, a.apartmentNumber, a.residentName, a.residentAge, a.residentType) "
                    +
                    "FROM ApartmentResidents a " +
                    "WHERE a.buildingId = :buildingId";

            boolean orderByAdded = false;

            if ("Ascending".equals(sortName)) {
                queryString += " ORDER BY a.residentName " + "ASC";
                orderByAdded = true;
            } else if ("Descending".equals(sortName)) {
                queryString += " ORDER BY a.residentName " + "DESC";
                orderByAdded = true;
            }

            if ("Ascending".equals(sortAge)) {
                queryString += orderByAdded ? ", " : " ORDER BY ";
                queryString += "a.residentAge " + "ASC";
            } else if ("Descending".equals(sortAge)) {
                queryString += orderByAdded ? ", " : " ORDER BY ";
                queryString += "a.residentAge " + "DESC";
            }

            Query<ApartmentResidentsDTO> query = session.createQuery(queryString, ApartmentResidentsDTO.class);
            query.setParameter("buildingId", buildingId);

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Collections.emptyList(); // Return an empty list in case of an exception
    }

    public List<ApartmentResidents> getResidentsByBuildingId(Long buildingId) {
        try (Session session = getSession()) {
            return session
                    .createQuery("FROM ApartmentResidents WHERE buildingId = :buildingId", ApartmentResidents.class)
                    .setParameter("buildingId", buildingId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list in case of an exception
        }
    }

}
