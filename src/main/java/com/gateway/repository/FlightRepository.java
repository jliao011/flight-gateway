package com.gateway.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gateway.domain.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {

	@Query("from Flight where departureCity=:departureCity or arrivalCity=:arrivalCity or dateOfDeparture=:dateOfDeparture")
	List<Flight> findFlights(@Param("departureCity") String from, @Param("arrivalCity") String string,
			@Param("dateOfDeparture") Date departureDate);

}
