package com.gateway.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Type;

@Entity
public class Flight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 20, nullable = false)
	private String flightNumber;
	@Column(length = 20, nullable = false)
	private String operatingAirlines;
	@Column(length = 20, nullable = false)
	private String departureCity;
	@Column(length = 20, nullable = false)
	private String arrivalCity;
	@Column(nullable = false)
	@Type(type = "date")
	private Date dateOfDeparture = new Date();

//	@Type(type = "time")
	@Column
	private Date estimatedDepartureTime = new Date();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getOperatingAirlines() {
		return operatingAirlines;
	}

	public void setOperatingAirlines(String operatingAirlines) {
		this.operatingAirlines = operatingAirlines;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public Date getDateOfDeparture() {
		return dateOfDeparture;
	}

	public void setDateOfDeparture(Date dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}

	public Date getEstimatedDepartureTime() {
		return estimatedDepartureTime;
	}

	public void setEstimatedDepartureTime(Date estimatedDepartureTime) {
		this.estimatedDepartureTime = estimatedDepartureTime;
	}

	@Override
	public String toString() {
		return "Flight [id=" + id + ", flightNumber=" + flightNumber + ", operatingAirlines=" + operatingAirlines
				+ ", departureCity=" + departureCity + ", arrivalCity=" + arrivalCity + ", dateOfDeparture="
				+ dateOfDeparture + ", estimatedDepartureTime=" + estimatedDepartureTime + "]";
	}

}
