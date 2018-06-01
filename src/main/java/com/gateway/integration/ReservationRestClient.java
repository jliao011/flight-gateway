package com.gateway.integration;

import com.gateway.domain.Reservation;
import com.gateway.dto.ReservationUpdateRequest;

public interface ReservationRestClient {

	public Reservation findReservation(Long id);

	public Reservation updateReservation(ReservationUpdateRequest reservationUpdateRequest);

}
