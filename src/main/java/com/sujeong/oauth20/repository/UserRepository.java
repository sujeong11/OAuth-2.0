package com.sujeong.oauth20.repository;

import com.sujeong.oauth20.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUsername(String userName);
}
