package com.example.gotrip.repository;

import com.example.gotrip.model.FlightBookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightBookingDetailRepository extends JpaRepository<FlightBookingDetail, Long> {
}
