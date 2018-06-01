package com.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gateway.domain.Flight;
import com.gateway.domain.Reservation;
import com.gateway.dto.ReservationRequest;
import com.gateway.repository.FlightRepository;
import com.gateway.service.ReservationService;

@Controller
public class ReservationController {

	private final static Logger logger = LoggerFactory.getLogger(ReservationController.class);
	private FlightRepository flightRepository;
	private ReservationService reservationService;

	@Autowired
	private ReservationController(FlightRepository flightRepository, ReservationService reservationService) {
		this.flightRepository = flightRepository;
		this.reservationService = reservationService;
	}

	@RequestMapping("/showReservation")
	public String showReservationPage(@RequestParam("flightId") Long flightId, ModelMap modelMap) {
		Flight flight = this.flightRepository.findById(flightId).get();
		logger.info("Flight {} for reservation {}", flight, flightId);
		modelMap.addAttribute("flight", flight);
		return "views/reservationView";
	}

	@RequestMapping(value = "/completeReservation", method = RequestMethod.POST)
	public String completeReservation(ReservationRequest reservationRequest, ModelMap modelMap) {
		Reservation reservation = this.reservationService.bookFlight(reservationRequest);
		logger.info("Reservation complete: {}", reservation);
		modelMap.addAttribute("msg",
				"Reservation created successfully and your confirmation number is " + reservation.getId() + ".");
		modelMap.addAttribute("reservation", reservation);
		return "views/reservationConfirmationView";
	}
}
