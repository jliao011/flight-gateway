package com.gateway.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gateway.domain.Flight;
import com.gateway.domain.Passenger;
import com.gateway.domain.Reservation;
import com.gateway.domain.Role;
import com.gateway.domain.User;
import com.gateway.repository.FlightRepository;
import com.gateway.repository.PassengerRepository;
import com.gateway.repository.ReservationRepository;
import com.gateway.repository.RoleRepository;
import com.gateway.repository.UserRepository;

@Service
public class DataLoader {

	private final static Logger logger = LoggerFactory.getLogger(DataLoader.class);

	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private PassengerRepository passengerRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private final SimpleDateFormat datefmt = new SimpleDateFormat("MM/dd/yyyy");
	private final SimpleDateFormat dtfmt = new SimpleDateFormat("MM/dd/yyyy hh:mm");

	@PostConstruct
	public void databaseInit() {

		this.createRoles();
		User admin = this.createAdmin();
		User user = this.createUser();
		try {
			this.createFlights();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Reservation reservation = this.createReservation();
	}

	@Transactional
	private void createRoles() {
		Role adminRole = new Role();
		adminRole.setName("ADMIN");
		adminRole = this.roleRepository.save(adminRole);
		logger.info("Admin role created: {}", adminRole);

		Role userRole = new Role();
		userRole.setName("USER");
		userRole = this.roleRepository.save(userRole);
		logger.info("User role created: {}", userRole);
	}

	@Transactional
	private User createAdmin() {
		User admin = new User();
		admin.setFirstName("adminFirstName");
		admin.setLastName("adminLastName");
		admin.setEmail("admin@admin.com");
		admin.setPassword(this.bCryptPasswordEncoder.encode("admin"));
		admin.setRoles(this.roleRepository.findAll());
		admin = this.userRepository.save(admin);
		logger.info("Admin user created: {}", admin);
		return admin;
	}

	// user for test
	@Transactional
	private User createUser() {
		User user = new User();
		user.setFirstName("userFirstName");
		user.setLastName("userLastName");
		user.setEmail("user@user.com");
		user.setPassword(this.bCryptPasswordEncoder.encode("user"));
		user.setRoles(Arrays.asList(this.roleRepository.findByName("USER")));
		user = this.userRepository.save(user);
		logger.info("Test user created: {}", user);
		return user;
	}

	@Transactional
	private void createFlights() throws ParseException {
		Flight flight;
		flight = new Flight();
		flight.setFlightNumber("AA1");
		flight.setOperatingAirlines("American Airlines");
		flight.setDepartureCity("AUS");
		flight.setArrivalCity("NYC");
		flight.setDateOfDeparture(this.datefmt.parse("06/01/2018"));
		flight.setEstimatedDepartureTime(Date.from(this.dtfmt.parse("06/01/2018 03:15").toInstant()));
		flight = flightRepository.save(flight);
		logger.info("flight created {}", flight);

		flight = new Flight();
		flight.setFlightNumber("SW1");
		flight.setOperatingAirlines("South West");
		flight.setDepartureCity("NYC");
		flight.setArrivalCity("DAL");
		flight.setDateOfDeparture(this.datefmt.parse("06/02/2018"));
		flight.setEstimatedDepartureTime(Date.from(this.dtfmt.parse("06/02/2018 05:14").toInstant()));
		flight = flightRepository.save(flight);
		logger.info("flight created {}", flight);

		flight = new Flight();
		flight.setFlightNumber("UA1");
		flight.setOperatingAirlines("United Airlines");
		flight.setDepartureCity("AUS");
		flight.setArrivalCity("NYC");
		flight.setDateOfDeparture(this.datefmt.parse("06/03/2018"));
		flight.setEstimatedDepartureTime(Date.from(this.dtfmt.parse("06/03/2018 13:25").toInstant()));
		flight = flightRepository.save(flight);
		logger.info("flight created {}", flight);

		flight = new Flight();
		flight.setFlightNumber("AA2");
		flight.setOperatingAirlines("American Airlines");
		flight.setDepartureCity("NYC");
		flight.setArrivalCity("AUS");
		flight.setDateOfDeparture(this.datefmt.parse("06/04/2018"));
		flight.setEstimatedDepartureTime(Date.from(this.dtfmt.parse("06/04/2018 15:34").toInstant()));
		flight = flightRepository.save(flight);
		logger.info("flight created {}", flight);

		flight = new Flight();
		flight.setFlightNumber("SW2");
		flight.setOperatingAirlines("American Airlines");
		flight.setDepartureCity("NYC");
		flight.setArrivalCity("AUS");
		flight.setDateOfDeparture(this.datefmt.parse("06/05/2018"));
		flight.setEstimatedDepartureTime(Date.from(this.dtfmt.parse("06/05/2018 07:16").toInstant()));
		flight = flightRepository.save(flight);
		logger.info("flight created {}", flight);
	}

	@Transactional
	public Reservation createReservation() {
		Passenger passenger = new Passenger();
		passenger.setEmail("flightgateway@gmail.com");
		passenger.setFirstName("First Name");
		passenger.setLastName("Last Name");
		passenger.setMiddleName("Middle Name");
		passenger.setPhone("1234567");
		passenger = this.passengerRepository.save(passenger);
		logger.info("Passenger created: {}", passenger.toString());
		Reservation reservation = new Reservation();
		reservation.setCheckedIn(false);
		reservation.setNumberOfBags(0);
		reservation.setFlight(this.flightRepository.findById(1L).get());
		reservation.setPassenger(passenger);
		reservation = this.reservationRepository.save(reservation);
		logger.info("Reservation created: {}", reservation.toString());
		return reservation;
	}

//	@PostConstruct
	public void test() {
		Passenger passenger = new Passenger();
		passenger.setEmail("aa");
		passenger.setFirstName("aaa");
		passenger.setLastName("asdf");
		passenger = passengerRepository.save(passenger);
		Reservation reservation = new Reservation();
		reservation.setCheckedIn(false);
		reservation.setNumberOfBags(2);
		reservation.setFlight(this.flightRepository.findById(1L).get());
		reservation.setPassenger(passenger);
		reservationRepository.save(reservation);
	}
}
