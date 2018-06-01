package com.gateway.service;

import com.gateway.domain.Reservation;
import com.gateway.dto.ReservationRequest;

public interface ReservationService {
	Reservation bookFlight(ReservationRequest reservationRequest);
}
