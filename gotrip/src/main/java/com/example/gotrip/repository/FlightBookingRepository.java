package com.example.gotrip.repository;

import com.example.gotrip.model.Flight;
import com.example.gotrip.model.FlightBooking;
import com.example.gotrip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {

    @Query("SELECT fb FROM FlightBooking fb WHERE fb.seat.flight.id = :flightId")
    List<FlightBooking> findByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT COUNT(fb) > 0 FROM FlightBooking fb WHERE fb.user = :user AND fb.seat.flight = :flight")
    boolean existsByUserAndFlight(@Param("user") User user, @Param("flight") Flight flight);
}
