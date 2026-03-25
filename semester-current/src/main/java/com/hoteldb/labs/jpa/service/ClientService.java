package com.hoteldb.labs.jpa.service;

import com.hoteldb.labs.jpa.entity.ClientEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Сервис для работы с клиентами через JPA
 */
public class ClientService {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public ClientService() {
        this("hotelPU");
    }

    public ClientService(String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    /**
     * Добавить нового клиента
     */
    public ClientEntity create(ClientEntity client) {
        em.getTransaction().begin();
        try {
            em.persist(client);
            em.getTransaction().commit();
            return client;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Найти клиента по ID
     */
    public ClientEntity findById(Integer id) {
        return em.find(ClientEntity.class, id);
    }

    /**
     * Получить всех клиентов
     */
    public List<ClientEntity> findAll() {
        TypedQuery<ClientEntity> query = em.createQuery("SELECT c FROM ClientEntity c", ClientEntity.class);
        return query.getResultList();
    }

    /**
     * Обновить информацию о клиенте
     */
    public ClientEntity update(ClientEntity client) {
        em.getTransaction().begin();
        try {
            ClientEntity updated = em.merge(client);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Удалить клиента по ID
     */
    public boolean delete(Integer id) {
        em.getTransaction().begin();
        try {
            ClientEntity client = em.find(ClientEntity.class, id);
            if (client != null) {
                em.remove(client);
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

