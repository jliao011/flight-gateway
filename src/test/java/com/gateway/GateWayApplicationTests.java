package com.gateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gateway.repository.FlightRepository;
import com.gateway.repository.PassengerRepository;
import com.gateway.repository.ReservationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GateWayApplicationTests {

	@Autowired
	private FlightRepository flightRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	@Autowired
	private PassengerRepository passengerRepository;

	@Test
	public void cascade() {

	}

}
