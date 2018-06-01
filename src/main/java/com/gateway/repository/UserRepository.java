package com.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gateway.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
