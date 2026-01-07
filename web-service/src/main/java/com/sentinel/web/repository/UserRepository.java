package com.sentinel.web.repository;

import com.sentinel.web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPin(String pin);
}