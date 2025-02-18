package com.example.gotrip.service;

import com.example.gotrip.dto.HotelRequestDTO;
import com.example.gotrip.dto.HotelResponseDTO;
import com.example.gotrip.model.Hotel;

import java.time.LocalDate;
import java.util.List;

public interface IHotelService {
    public HotelResponseDTO save(HotelRequestDTO request);
    public List<HotelResponseDTO> findAll();
    public List<HotelResponseDTO> searchHotels(LocalDate dateFrom, LocalDate dateTo, String destination);
    public HotelResponseDTO findById(Long id);
    public Hotel findByHotelCode(String hotelCode);
    public HotelResponseDTO update(Long id, String name, String location, Integer stars);
    public void delete(Long id);
}
