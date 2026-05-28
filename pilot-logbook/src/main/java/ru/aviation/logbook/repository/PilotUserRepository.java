package ru.aviation.logbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.aviation.logbook.domain.PilotUser;
import ru.aviation.logbook.domain.UserRole;

import java.util.List;
import java.util.Optional;

public interface PilotUserRepository extends JpaRepository<PilotUser, Long> {

    Optional<PilotUser> findByUsernameAndActiveTrue(String username);

    Optional<PilotUser> findByUsername(String username);

    boolean existsByUsername(String username);

    List<PilotUser> findByRoleAndActiveTrueOrderByFullNameAsc(UserRole role);

    @Query("""
            SELECT p FROM PilotUser p
            WHERE p.role = ru.aviation.logbook.domain.UserRole.PILOT
            ORDER BY p.fullName ASC
            """)
    List<PilotUser> findAllPilotsForChief();
}
