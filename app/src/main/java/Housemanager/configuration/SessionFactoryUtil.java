package Housemanager.configuration;

import Housemanager.entities.Apartment;
import Housemanager.entities.ApartmentResidents;
import Housemanager.entities.Building;
import Housemanager.entities.BuildingManagement;
import Housemanager.entities.MonthlyPayment;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(Apartment.class);
            configuration.addAnnotatedClass(ApartmentResidents.class);
            configuration.addAnnotatedClass(Building.class);
            configuration.addAnnotatedClass(BuildingManagement.class);
            configuration.addAnnotatedClass(MonthlyPayment.class);
            ServiceRegistry serviceRegistry
                    = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
}

