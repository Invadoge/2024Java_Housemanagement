package Housemanager.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Housemanager.DTO.ApartmentDTO;
import Housemanager.entities.Apartment;

public class ApartmentDAO extends BaseDAO{

    public ApartmentDAO() {
        super();
    }

    public ApartmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public void removeById(String[] ids) {
        if (ids.length != 2) {
            throw new IllegalArgumentException("Exactly two IDs (apartmentNumber and buildingId) are required.");
        }

        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();

            Short apartmentNumber = Short.valueOf(ids[0]);
            Long buildingId = Long.valueOf(ids[1]);

            Apartment apartmentToRemove = session.createQuery(
                "FROM Apartment WHERE apartmentNumber = :apartmentNumber AND buildingId = :buildingId", Apartment.class)
                .setParameter("apartmentNumber", apartmentNumber)
                .setParameter("buildingId", buildingId)
                .uniqueResult();
            
            if (apartmentToRemove != null) {
                session.remove(apartmentToRemove);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ApartmentDTO> getApartmentsByBuildingId(Long buildingId) {
        try (Session session = getSession()) {
            String hql = "SELECT NEW ApartmentDTO(a.buildingId, a.apartmentNumber, a.apartmentFloor, a.apartmentArea, a.apartmentOwner) " +
                         "FROM Apartment a " +
                         "WHERE a.buildingId = :buildingId";

            Query<ApartmentDTO> query = session.createQuery(hql, ApartmentDTO.class);
            query.setParameter("buildingId", buildingId);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
