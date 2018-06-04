package com.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gateway.domain.Reservation;
import com.gateway.dto.ReservationUpdateRequest;
import com.gateway.integration.ReservationRestClient;

// use ReservationRestClient to sent HTTP request
@Controller
public class CheckInController {

	private static final Logger logger = LoggerFactory.getLogger(CheckInController.class);
	private ReservationRestClient reservationRestClient;

	@Autowired
	private CheckInController(ReservationRestClient reservationRestClient) {
		this.reservationRestClient = reservationRestClient;
	}

	@RequestMapping("/checkin")
	public String showCheckinPage() {
		return "views/checkinView";
	}

	@RequestMapping(value = "/startCheckIn", method = RequestMethod.POST)
	public String startCheckIn(@RequestParam("reservationId") Long reservationId, ModelMap modelMap) {
		try {
			logger.info("Start checkin in controller for id {} using rest api in CheckInController", reservationId);
			// rest client authentication problem
			Reservation reservation = this.reservationRestClient.findReservation(reservationId);
			logger.info("Start check in reservation: {}", reservation.toString());
			if (reservation.isCheckedIn()) {
				modelMap.addAttribute("error", "This reservation has already been checked in.");
				return "views/checkinView";
			}
			modelMap.addAttribute("reservation", reservation);
			return "views/checkinDetailView";
		} catch (NullPointerException e) {
			modelMap.addAttribute("error", "Your reservation does not exist, try again.");
			logger.info("Reservation for id: {} does not exist.", reservationId);
			return "views/checkinView";
		}
	}

	@RequestMapping(value = "/confirmCheckIn", method = RequestMethod.POST)
	public String confirmCheckIn(@RequestParam("reservationId") Long reservationId,
			@RequestParam("numberOfBags") int numberOfBags, ModelMap modelMap) {
		ReservationUpdateRequest reservationUpdateRequest = new ReservationUpdateRequest();
		reservationUpdateRequest.setId(reservationId);
		reservationUpdateRequest.setCheckedIn(true);
		reservationUpdateRequest.setNumberOfBags(numberOfBags);
		Reservation updatedReservation = this.reservationRestClient.updateReservation(reservationUpdateRequest);
		logger.info("Updated reservation: {}", updatedReservation.toString());
		modelMap.addAttribute("reservation", updatedReservation);
		modelMap.addAttribute("msg", "You have checked in successfully, " + numberOfBags + " bags added.");
		return "views/reservationConfirmationView";

	}
}
