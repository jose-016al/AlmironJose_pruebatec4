package com.example.gotrip.repository;

import com.example.gotrip.model.Room;
import com.example.gotrip.util.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r " +
            "WHERE r.hotel.hotelCode = :hotelCode " +
            "AND r.roomType = :roomType " +
            "AND NOT EXISTS (SELECT 1 FROM HotelBookingDetail hbd " +
            "WHERE hbd.room = r AND hbd.hotelBooking.checkIn <= :checkOut " +
            "AND hbd.hotelBooking.checkOut >= :checkIn)")
    List<Room> findAvailableRooms(
            @Param("hotelCode") String hotelCode,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("roomType") RoomType roomType
    );
}
