package com.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gateway.domain.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
