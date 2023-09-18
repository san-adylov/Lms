package com.example.lms.repository;

import com.example.lms.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> getUserByEmail(String email);

}
