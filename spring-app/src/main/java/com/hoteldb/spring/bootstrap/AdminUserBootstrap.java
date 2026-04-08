package com.hoteldb.spring.bootstrap;

import com.hoteldb.spring.domain.UserEntity;
import com.hoteldb.spring.domain.UserRole;
import com.hoteldb.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserBootstrap.class);

    private final UserRepository userRepository;

    public AdminUserBootstrap(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(UserRole.ADMIN);
        userRepository.save(admin);
        log.warn("Created default admin user: admin / admin");
    }
}
