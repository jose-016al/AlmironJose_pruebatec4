package com.example.gotrip.service;

import com.example.gotrip.dto.HotelBookingDetailResponseDTO;
import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.model.HotelBooking;
import com.example.gotrip.model.HotelBookingDetail;

import java.time.LocalDate;
import java.util.List;

public interface IHotelBookingDetailService {
    public List<HotelBookingDetail> generateStays(HotelBookingRequestDTO request, HotelBooking booking);
    public List<HotelBookingDetailResponseDTO> getStaysResponseDTOS(HotelBooking booking);
    public double calculateTotalPrice(List<HotelBookingDetail> bookingDetails, LocalDate checkIn, LocalDate checkOut);
}
