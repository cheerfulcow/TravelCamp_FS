package com.example.travelcamp.repositories;

import com.example.travelcamp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByLogin(String login);

    Optional<User> findByRole(String role);
    Optional<User> findByEmail(String email);

}

