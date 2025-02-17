package com.example.gotrip.repository;

import com.example.gotrip.model.HotelBookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelBookingDetailRepository extends JpaRepository<HotelBookingDetail, Long> {
}
