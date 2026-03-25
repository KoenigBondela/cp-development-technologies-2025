package com.hoteldb.labs.jpa.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;

/**
 * Сервис для получения универсального отношения (LEFT JOIN таблиц rooms и clients)
 */
public class UniversalRelationService {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public UniversalRelationService() {
        this("hotelPU");
    }

    public UniversalRelationService(String persistenceUnitName) {
        // Создаем фабрику и менеджер сущностей
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    /**
     * Получить универсальное отношение (LEFT JOIN таблиц rooms и clients)
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getUniversalRelation() {
        String jpql = "SELECT r.id, r.roomNumber, r.roomType, r.pricePerNight, r.isAvailable, " +
                "c.id, c.firstName, c.lastName, c.email, c.phone, c.checkInDate, c.checkOutDate " +
                "FROM RoomEntity r LEFT JOIN ClientEntity c WITH r.id = c.roomId " +
                "ORDER BY r.id, c.id";
        Query query = em.createQuery(jpql);
        return query.getResultList();
    }

    /**
     * Закрыть соединения
     */
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

