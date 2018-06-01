package com.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gateway.domain.Flight;
import com.gateway.domain.Passenger;
import com.gateway.domain.Reservation;
import com.gateway.dto.ReservationRequest;
import com.gateway.repository.FlightRepository;
import com.gateway.repository.PassengerRepository;
import com.gateway.repository.ReservationRepository;
import com.gateway.util.EmailUtil;
import com.gateway.util.PDFGenerator;

@Service
public class ReservationServiceImpl implements ReservationService {

	private final static Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

	@Value("${app.itinerary.dirpath}")
	private String ITINERARY_DIR;

	private FlightRepository flightRepository;
	private PassengerRepository passengerRepository;
	private ReservationRepository reservationRepository;
	private PDFGenerator pdfGenerator;
	private EmailUtil emailUtil;

	@Autowired
	private ReservationServiceImpl(FlightRepository flightRepository, PassengerRepository passengerRepository,
			ReservationRepository reservationRepository, PDFGenerator pdfGenerator, EmailUtil emailUtil) {
		this.flightRepository = flightRepository;
		this.passengerRepository = passengerRepository;
		this.reservationRepository = reservationRepository;
		this.pdfGenerator = pdfGenerator;
		this.emailUtil = emailUtil;
	}

	@Override
//	@Transactional // roll back when error, service layer & business logic
	public Reservation bookFlight(ReservationRequest reservationRequest) {
		// API: make payment here using payment info
		Long flightId = reservationRequest.getFlightId();
		Flight flight = this.flightRepository.findById(flightId).get();
		logger.info("Reservation flight is: {}", flight);
		// create passenger
		Passenger passenger = new Passenger();
		passenger.setFirstName(reservationRequest.getPassengerFirstName());
		passenger.setLastName(reservationRequest.getPassengerLastName());
		passenger.setEmail(reservationRequest.getPassengerEmail());
		passenger.setPhone(reservationRequest.getPassengerPhone());
		Passenger savedPassenger = this.passengerRepository.save(passenger);
		logger.info("New passenger created: {}", savedPassenger.toString());
		// create reservation
		Reservation reservation = new Reservation();
		reservation.setFlight(flight);
		reservation.setPassenger(savedPassenger);
		reservation.setCheckedIn(false);

		Reservation savedReservation = this.reservationRepository.save(reservation);
		logger.info("New reservation created: {}", reservation.toString());

		// generate pdf
		String filePath = ITINERARY_DIR + savedReservation.getId() + ".pdf";
		logger.info("Save reservation file to {} and send email to {}", filePath, passenger.getEmail());
		this.pdfGenerator.generateItinerary(savedReservation, filePath);
		// send email
		emailUtil.sendItinerary(passenger.getEmail(), filePath);
		return savedReservation;
	}

}
