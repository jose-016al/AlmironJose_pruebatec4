package com.example.gotrip.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HotelBookingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String guestName;

    @Column(nullable = false)
    private String guestIdDocument;

    @ManyToOne
    @JoinColumn(name = "hotel_booking_id", nullable = false)
    private HotelBooking hotelBooking;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}
