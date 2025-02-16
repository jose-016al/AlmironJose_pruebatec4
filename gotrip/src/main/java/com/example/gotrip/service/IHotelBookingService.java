package com.example.gotrip.service;

import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.dto.HotelBookingResponseDTO;

import java.util.List;

public interface IHotelBookingService {
    public HotelBookingResponseDTO save(HotelBookingRequestDTO request);
    public List<HotelBookingResponseDTO> findAll();
    public HotelBookingResponseDTO findById(Long id);
    public List<HotelBookingResponseDTO> findByHotelId(Long hotelId);
    public void delete(Long id);
}
