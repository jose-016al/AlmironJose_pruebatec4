package com.example.gotrip.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String passport;

    @OneToMany(mappedBy = "user")
    private List<HotelBooking> hotelBookings;

    @OneToMany(mappedBy = "user")
    private List<FlightBooking> flightBookings;
}
