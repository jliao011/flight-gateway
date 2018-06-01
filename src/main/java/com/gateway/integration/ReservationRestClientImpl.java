package com.gateway.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.gateway.domain.Reservation;
import com.gateway.dto.ReservationUpdateRequest;

@Component
public class ReservationRestClientImpl implements ReservationRestClient {

	private static final Logger logger = LoggerFactory.getLogger(ReservationRestClientImpl.class);

	@Value("${app.host.address}")
	private String HOST;
	private String RESERVATION_REST_URL = "/reservations/";

	@Override
	public Reservation findReservation(Long id) {
		// rest template need to add security authentication
		RestTemplate restTemplate = new RestTemplate();
		logger.info("Find reservation using url: {}", RESERVATION_REST_URL + id);
//		try {
		// has authentication problem here
		Reservation reservation = restTemplate.getForObject(HOST + RESERVATION_REST_URL + id, Reservation.class);
		logger.info("Reservation found: {}", reservation.toString());
		return reservation;
//		} catch (Exception e) {
//			logger.error("Rest error find reservation: {}", e.getMessage());
//			return null;
//		}
	}

	@Override
	public Reservation updateReservation(ReservationUpdateRequest reservationUpdateRequest) {
		logger.info("Reservation update request: {}", reservationUpdateRequest.toString());
		RestTemplate restTemplate = new RestTemplate();
		Reservation reservation = restTemplate.postForObject(HOST + RESERVATION_REST_URL, reservationUpdateRequest,
				Reservation.class);
		logger.info("After update request reservation is: {}", reservation.toString());
		return reservation;
	}

}
