package com.flicker.user.user.infrastructure;

import com.flicker.user.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public User findByUserId(String username);
}