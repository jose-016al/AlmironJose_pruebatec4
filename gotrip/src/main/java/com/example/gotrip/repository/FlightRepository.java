package com.example.gotrip.repository;

import com.example.gotrip.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.departureDate BETWEEN :dateFrom AND :dateTo AND f.origin = :origin AND f.destination = :destination")
    List<Flight> searchFlights(@Param("dateFrom") LocalDate dateFrom,
                               @Param("dateTo") LocalDate dateTo,
                               @Param("origin") String origin,
                               @Param("destination") String destination);
    Optional<Flight> findByFlightCode(String flightCode);
    @Query("SELECT COUNT(fbd) > 0 FROM FlightBookingDetail fbd WHERE fbd.seat.flight.id = :flightId")
    boolean hasActiveBookings(@Param("flightId") Long flightId);
}
