package com.hoteldb.spring.repository;

import com.hoteldb.spring.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUsernameAndDeletedFalse(String username);

    Optional<UserEntity> findByIdAndDeletedFalse(Integer id);

    List<UserEntity> findAllByDeletedFalseOrderByIdAsc();

    boolean existsByUsernameAndDeletedFalse(String username);
}
