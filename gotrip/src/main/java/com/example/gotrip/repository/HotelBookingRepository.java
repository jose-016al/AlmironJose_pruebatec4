package com.example.gotrip.repository;

import com.example.gotrip.model.FlightBooking;
import com.example.gotrip.model.HotelBooking;
import com.example.gotrip.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {

    @Query("SELECT hb FROM HotelBooking hb " +
            "JOIN hb.hotelBookingDetails hbd " +
            "WHERE hbd.room.hotel.id = :hotelId")
    List<HotelBooking> findByHotelId(@Param("hotelId") Long hotelId);

    @Query("SELECT COUNT(hb) > 0 FROM HotelBooking hb WHERE hb.user = :user AND hb.checkIn <= :checkOut AND hb.checkOut >= :checkIn")
    boolean existsByUserAndDateRange(@Param("user") User user,
                                     @Param("checkIn") LocalDate checkIn,
                                     @Param("checkOut") LocalDate checkOut);
}
