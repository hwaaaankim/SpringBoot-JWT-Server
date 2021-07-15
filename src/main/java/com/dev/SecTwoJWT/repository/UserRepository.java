package com.dev.SecTwoJWT.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.SecTwoJWT.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
}
