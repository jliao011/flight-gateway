package com.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gateway.domain.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {

}
