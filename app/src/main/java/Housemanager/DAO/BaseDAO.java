package Housemanager.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import Housemanager.configuration.SessionFactoryUtil;

public abstract class BaseDAO {

    private final SessionFactory sessionFactory;

    protected BaseDAO() {
        this.sessionFactory = SessionFactoryUtil.getSessionFactory();
    }

    protected BaseDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    protected Session getSession() {
        return sessionFactory.openSession();
    }

    public void saveOrUpdate(Object entity) {
        Transaction transaction = null;
        try (Session session = getSession()) {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void removeById(String[] id) {
        throw new UnsupportedOperationException("Remove operation not supported for BaseDAO");
    }

    //Vulnerable to SQL injection
    public boolean hasElements(String entityName) {
        try (Session session = getSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM " + entityName, Long.class);
            Long count = query.uniqueResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Handle the exception appropriately based on your application's needs
        }
    }
}
