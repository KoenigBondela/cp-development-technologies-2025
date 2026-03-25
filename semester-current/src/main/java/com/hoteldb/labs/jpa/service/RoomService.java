package com.hoteldb.labs.jpa.service;

import com.hoteldb.labs.jpa.entity.RoomEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Сервис для работы с номерами через JPA
 */
public class RoomService {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public RoomService() {
        this("hotelPU");
    }

    public RoomService(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    /**
     * Добавить новый номер
     */
    public RoomEntity create(RoomEntity room) {
        em.getTransaction().begin();
        try {
            em.persist(room);
            em.getTransaction().commit();
            return room;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Найти номер по ID
     */
    public RoomEntity findById(Integer id) {
        return em.find(RoomEntity.class, id);
    }

    /**
     * Получить все номера
     */
    public List<RoomEntity> findAll() {
        TypedQuery<RoomEntity> query = em.createQuery("SELECT r FROM RoomEntity r", RoomEntity.class);
        return query.getResultList();
    }

    /**
     * Обновить информацию о номере
     */
    public RoomEntity update(RoomEntity room) {
        em.getTransaction().begin();
        try {
            RoomEntity updated = em.merge(room);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Удалить номер по ID
     */
    public boolean delete(Integer id) {
        em.getTransaction().begin();
        try {
            RoomEntity room = em.find(RoomEntity.class, id);
            if (room != null) {
                em.remove(room);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
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

