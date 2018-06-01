package com.gateway.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gateway.domain.Reservation;
import com.gateway.dto.ReservationUpdateRequest;
import com.gateway.repository.ReservationRepository;

@RestController
public class ReservationRestController {
	private static final Logger logger = LoggerFactory.getLogger(ReservationRestController.class);
	private ReservationRepository reservationRepository;

	@Autowired
	private ReservationRestController(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	@RequestMapping("/reservations/{id}")
	public Reservation findReservation(@PathVariable("id") Long id) {
		try {
			Reservation reservation = this.reservationRepository.findById(id).get();
			logger.info("Reservation detail: {}", reservation.toString());
			return reservation;
		} catch (NoSuchElementException e) {
			logger.info("No reservation found for id: {}", id);
			return null;
		}
	}

	@RequestMapping("/reservations")
	public Reservation updateReservation(@RequestBody ReservationUpdateRequest reservationUpdateRequest) {
		try {
			Reservation reservation = this.reservationRepository.findById(reservationUpdateRequest.getId()).get();
			logger.info("Reservation to be updated: {}", reservation);
			reservation.setNumberOfBags(reservationUpdateRequest.getNumberOfBags());
			reservation.setCheckedIn(reservationUpdateRequest.getCheckedIn());
			Reservation updatedReservation = this.reservationRepository.save(reservation);
			logger.info("Reservation after update: {}", updatedReservation);
			return updatedReservation;
		} catch (NoSuchElementException e) {
			logger.info("No reservation found for id: {}", reservationUpdateRequest.getId());
			return null;
		}
	}
}
