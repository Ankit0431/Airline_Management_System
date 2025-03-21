package com.example.airline.controller;

import com.example.airline.model.Flight;
import com.example.airline.repository.FlightRepository;
import com.example.airline.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;
    @Autowired
    private FlightRepository flightRepository;
    @GetMapping("/all")
    public List<Flight> getAllFlights() {
        return flightService.getAllFlights();
    }

    @PostMapping("/add")
    public Flight addFlight(@RequestBody Flight flight) {
        return flightRepository.save(flight);
    }
}

