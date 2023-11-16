package com.app.lms.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.app.lms.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getUserByEmail(String email);

    boolean existsByEmail(String email);


}