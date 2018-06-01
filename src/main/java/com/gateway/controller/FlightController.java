package com.gateway.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gateway.domain.Flight;
import com.gateway.repository.FlightRepository;

@Controller

public class FlightController {

	private static final Logger logger = LoggerFactory.getLogger(FlightController.class);
	FlightRepository flightRepository;

	@Autowired
	private FlightController(FlightRepository flightRepository) {
		this.flightRepository = flightRepository;
	}

	@RequestMapping("/flightReservation")
	public String showFindFlightPage() {
		return "views/findFlightsView";
	}

	@RequestMapping(value = "/findFlightsAction", method = RequestMethod.POST)
	public String findFlight(@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("departureDate") @DateTimeFormat(pattern = "MM/dd/yyyy") Date departureDate,
			ModelMap modelMap) {
		List<Flight> flights;
		if (from.isEmpty() && to.isEmpty() && departureDate == null) {
			flights = this.flightRepository.findAll();
			modelMap.addAttribute("error", "Now all flights are shown below: ");
		} else
			flights = this.flightRepository.findFlights(from, to, departureDate);
		logger.info("Search Flight from {} to {} on {}", from, to, departureDate);
		if (flights.size() == 0) {
			modelMap.addAttribute("error", "No result found.");
			return "views/findFlightsView";
		} else {
			logger.info("Search total {} result", flights.size());
			for (Flight flight : flights)
				logger.info("flight: {}", flight.toString());
			modelMap.addAttribute("flights", flights);
			return "views/displayFindFlightsView";
		}
	}

	@RequestMapping("/addFlights")
	public String showAddFlightPage() {
		return "views/addFlightsView";
	}

	@RequestMapping(value = "/addFlightsAction", method = RequestMethod.POST)
	public String addFlight(@RequestParam("flightNumber") String flightNumber,
			@RequestParam("flightAirlines") String flightAirlines, @RequestParam("from") String from,
			@RequestParam("to") String to,
			@RequestParam("departureDate") @DateTimeFormat(pattern = "MM/dd/yyyy") Date departureDate,
			ModelMap modelMap) {
		Flight flight = new Flight();
		flight.setFlightNumber(flightNumber);
		flight.setOperatingAirlines(flightAirlines);
		flight.setDepartureCity(from);
		flight.setArrivalCity(to);
		flight.setDateOfDeparture(departureDate);
		flight.setEstimatedDepartureTime(departureDate);
		flight = this.flightRepository.save(flight);
		logger.info("Flight created: {}", flight);
		modelMap.addAttribute("flights", Arrays.asList(flight));
		modelMap.addAttribute("msg", "Flight added successfully!");
		return "views/displayAddFlightsView";
	}

	@RequestMapping("/updateFlight")
	public String showUpdateFlightPage(@RequestParam("flightId") Long flightId, ModelMap modelMap) {
		try {
			Flight flight = this.flightRepository.findById(flightId).get();
			logger.info("Flight {} ready for update :{}", flight.toString());
			modelMap.addAttribute("flight", flight);
			return "views/updateFlightView";
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("error", "No flight found.");
			return "views/findFlightsView";
		}
	}

	@RequestMapping(value = "/updateFlightAction", method = RequestMethod.POST)
	public String updateFlight(@RequestParam("flightId") Long flightId,
			@RequestParam("flightNumber") String flightNumber, @RequestParam("flightAirlines") String flightAirlines,
			@RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("departureDate") @DateTimeFormat(pattern = "MM/dd/yyyy") Date departureDate,
			ModelMap modelMap) {
		try {
			Flight flight = this.flightRepository.findById(flightId).get();
			flight.setFlightNumber(flightNumber);
			flight.setOperatingAirlines(flightAirlines);
			flight.setDepartureCity(from);
			flight.setArrivalCity(to);
			flight.setDateOfDeparture(departureDate);
			flight = this.flightRepository.save(flight);
			logger.info("Flight updated: {}", flight);
			modelMap.addAttribute("flights", this.flightRepository.findAll());
			modelMap.addAttribute("msg", "Flight updated successfully.");
			return "views/displayFindFlightsView";

		} catch (NoSuchElementException e) {
			modelMap.addAttribute("error", "No flight found.");
			return "views/findFlightsView";
		}
	}

	@RequestMapping("/deleteFlight")
	public String deleteFlightPage(@RequestParam("flightId") Long flightId, ModelMap modelMap) {
		try {
			Flight flight = this.flightRepository.findById(flightId).get();
			logger.info("Flight {} ready for delete: {}", flight.toString());
			this.flightRepository.deleteById(flightId);
			modelMap.addAttribute("msg", "Flight deleted successfully.");
			modelMap.addAttribute("flights", this.flightRepository.findAll());
			return "views/displayFindFlightsView";
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("error", "No flight found.");
			return "views/findFlightsView";
		}
	}

}
