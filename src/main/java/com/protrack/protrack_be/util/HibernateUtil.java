package com.protrack.protrack_be.util;

import jakarta.persistence.EntityManager;
import org.hibernate.Filter;
import org.hibernate.Session;

public class HibernateUtil {

    private HibernateUtil() {
        // disable constructing
    }

    public static void enableDeletedFilter(EntityManager entityManager) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.getEnabledFilter("deletedFilter");
        if (filter == null) {
            session.enableFilter("deletedFilter").setParameter("isDeleted", false);
        }
    }
}
