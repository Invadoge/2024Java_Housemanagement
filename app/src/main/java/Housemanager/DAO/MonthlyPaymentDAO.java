package Housemanager.DAO;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Housemanager.DTO.MonthlyPaymentDTO;
import Housemanager.entities.Apartment;
import Housemanager.entities.MonthlyPayment;

public class MonthlyPaymentDAO extends BaseDAO {
    public MonthlyPaymentDAO() {
        super();
    }

    public MonthlyPaymentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    private BigDecimal calculatePreviousOutstanding(Apartment apartment) {
        List<MonthlyPayment> payments = apartment.getMonthlyPayments();

        if (payments.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // Get the most recent payment
        MonthlyPayment mostRecentPayment = payments.get(payments.size() - 1);

        return mostRecentPayment.getOutstandingBalance();
    }

    private Apartment fetchApartment(Long buildingId, Short apartmentNumber) {
        return getSession()
                .createQuery("FROM Apartment WHERE buildingId = :buildingId AND apartmentNumber = :apartmentNumber",
                        Apartment.class)
                .setParameter("buildingId", buildingId)
                .setParameter("apartmentNumber", apartmentNumber)
                .uniqueResult();
    }

    public void addPayment(MonthlyPayment monthlyPayment) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();

            Apartment apartment = fetchApartment(monthlyPayment.getBuildingId(), monthlyPayment.getApartmentNumber());
            BuildingDAO buildingDAO = new BuildingDAO();
            BigDecimal[] taxes = buildingDAO.getTaxesById(monthlyPayment.getBuildingId());

            if (taxes == null || taxes.length < 2) {
                System.err.println("Error retrieving taxes for building ID: " + monthlyPayment.getBuildingId());
                return;
            }

            BigDecimal petTax = taxes[0];
            BigDecimal elevatorTax = taxes[1];

            // Calculate Owed Tax
            ApartmentResidentsDAO apartmentResidentsDAO = new ApartmentResidentsDAO();
            BigDecimal owedTax = apartmentResidentsDAO.calculateResidentsCosts(
                    monthlyPayment.getBuildingId(),
                    monthlyPayment.getApartmentNumber(),
                    petTax,
                    elevatorTax);

            BigDecimal previousOutstanding = calculatePreviousOutstanding(apartment);

            owedTax = owedTax.subtract(monthlyPayment.getAmount());
            monthlyPayment.setOutstandingBalance(owedTax.add(previousOutstanding));

            apartment.getMonthlyPayments().add(monthlyPayment);

            monthlyPayment = session.merge(monthlyPayment);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveOrUpdate(Object entity) {
        if (!(entity instanceof MonthlyPayment)) {
            throw new IllegalArgumentException("MonthlyPaymentDAO::Invalid entity type!");
        }

        addPayment((MonthlyPayment) entity);
    }

    @Override
    public void removeById(String[] ids) {
        if (ids.length != 1) {
            throw new IllegalArgumentException("Exactly one id is required!");
        }

        try (Session session = getSession()) {
            Transaction transaction = session.beginTransaction();

            Long mpId = Long.valueOf(ids[0]);
            MonthlyPayment paymentToRemove = session.get(MonthlyPayment.class, mpId);

            if (paymentToRemove != null) {
                session.remove(paymentToRemove);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BigDecimal getProfitByBuildingId(Long buildingId){
        BigDecimal sum = BigDecimal.ZERO;
        try(Session session = getSession()){
            String hql = "SELECT mp.amount FROM MonthlyPayment mp " +
                        "WHERE mp.buildingId = :buildingId";
            Query<BigDecimal> query = session.createQuery(hql, BigDecimal.class);
            query.setParameter("buildingId", buildingId);
            List<BigDecimal> payments = query.list();

            for(BigDecimal payment : payments){
                sum = sum.add(payment);
            }
            
        }
        return sum;
    }

    public List<MonthlyPaymentDTO> getAllMonthlyPaymentsWithEmployeeInfo() {
    try (Session session = getSession()) {
        String queryString = "SELECT NEW " +
                " MonthlyPaymentDTO(bm.companyName, bm.employeeName, mp.buildingId, mp.apartmentNumber, mp.paymentMonth, mp.amount) " +
                "FROM MonthlyPayment mp " +
                "JOIN Building b ON mp.buildingId = b.id " +
                "JOIN BuildingManagement bm ON b.managedByEmployee = bm.employeeId";
                
        Query<MonthlyPaymentDTO> query = session.createQuery(queryString, MonthlyPaymentDTO.class);

        return query.getResultList();
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
}
