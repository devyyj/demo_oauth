package com.example.demo_oauth.security.repository;

import com.example.demo_oauth.security.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MyUserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findBySocialId(String id);
}
