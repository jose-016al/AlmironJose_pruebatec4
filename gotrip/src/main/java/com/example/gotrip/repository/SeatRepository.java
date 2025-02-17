package com.example.gotrip.repository;

import com.example.gotrip.model.Flight;
import com.example.gotrip.model.Seat;
import com.example.gotrip.util.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Query("SELECT s FROM Seat s WHERE s.flight.flightCode = :flightCode AND s.seatType = :seatType AND s.available = true ORDER BY s.id ASC LIMIT :limit")
    List<Seat> findAvailableSeats(@Param("flightCode") String flightCode,
                                  @Param("seatType") SeatType seatType,
                                  @Param("limit") int limit);
}