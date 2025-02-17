package com.example.gotrip.repository;

import com.example.gotrip.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByHotelCode(String hotelCode);

    @Query("SELECT COUNT(b) > 0 FROM HotelBooking b " +
            "JOIN b.hotelBookingDetails hbd " +
            "WHERE hbd.room.hotel = :hotel " +
            "AND b.checkOut > :currentDate")
    boolean hasActiveBookings(@Param("hotel") Hotel hotel, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT r.roomType, COUNT(r) FROM Room r " +
            "WHERE r.hotel = :hotel " +
            "AND r.id NOT IN (" +
            "  SELECT hb.room.id FROM HotelBookingDetail hb " +
            "  WHERE hb.room.hotel = :hotel " +
            "  AND ((hb.hotelBooking.checkIn BETWEEN :dateFrom AND :dateTo) " +
            "    OR (hb.hotelBooking.checkOut BETWEEN :dateFrom AND :dateTo) " +
            "    OR (hb.hotelBooking.checkIn <= :dateFrom AND hb.hotelBooking.checkOut >= :dateTo))" +
            ") " +
            "GROUP BY r.roomType")
    List<Object[]> countAvailableRoomsByType(@Param("hotel") Hotel hotel,
                                             @Param("dateFrom") LocalDate dateFrom,
                                             @Param("dateTo") LocalDate dateTo);
}
