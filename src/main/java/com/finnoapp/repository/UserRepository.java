package com.finnoapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finnoapp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUserName(String userName);

    public Optional<User> findByEmail(String email);

}
