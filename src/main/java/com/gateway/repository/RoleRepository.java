package com.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gateway.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByName(String name);

}
