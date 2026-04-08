package com.hoteldb.labs.jpa.service;

import com.hoteldb.labs.jpa.entity.UserEntity;
import com.hoteldb.labs.jpa.entity.UserRole;
import com.hoteldb.labs.jpa.JpaFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public UserService() {
        this("hotelPU");
    }

    public UserService(String persistenceUnitName) {
        emf = JpaFactory.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    public UserEntity register(String username, String password, UserRole role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is required");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password is required");
        }

        UserEntity user = new UserEntity(username.trim(), password, role == null ? UserRole.USER : role);

        em.getTransaction().begin();
        try {
            em.persist(user);
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    public Optional<UserEntity> findByUsernameAndPassword(String username, String password) {
        if (username == null || password == null) {
            return Optional.empty();
        }
        String u = username.trim();
        if (u.isEmpty() || password.isBlank()) {
            return Optional.empty();
        }

        TypedQuery<UserEntity> q = em.createQuery(
                "SELECT u FROM UserEntity u WHERE u.username = :username AND u.password = :password",
                UserEntity.class
        );
        q.setParameter("username", u);
        q.setParameter("password", password);

        List<UserEntity> users = q.setMaxResults(1).getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public Optional<UserEntity> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        String u = username.trim();
        if (u.isEmpty()) {
            return Optional.empty();
        }

        TypedQuery<UserEntity> q = em.createQuery(
                "SELECT u FROM UserEntity u WHERE u.username = :username",
                UserEntity.class
        );
        q.setParameter("username", u);

        List<UserEntity> users = q.setMaxResults(1).getResultList();
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}

