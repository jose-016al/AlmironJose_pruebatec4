package com.example.gotrip.repository;

import com.example.gotrip.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {

    @Query("SELECT hb FROM HotelBooking hb WHERE hb.room.hotel.id = :hotelId")
    List<HotelBooking> findByHotelId(@Param("hotelId") Long hotelId);
}
