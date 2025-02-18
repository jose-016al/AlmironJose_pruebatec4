package com.example.gotrip.service;

import com.example.gotrip.dto.HotelBookingRequestDTO;
import com.example.gotrip.dto.HotelBookingResponseDTO;
import com.example.gotrip.exception.HotelException;
import com.example.gotrip.exception.InvalidDateException;
import com.example.gotrip.exception.NoContentException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.model.HotelBooking;
import com.example.gotrip.repository.HotelBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelBookingService implements IHotelBookingService {

    private final HotelBookingRepository repository;
    private final IUserService userService;
    private final HotelBookingDetailService hotelBookingDetailService;

    /**
     * Guarda una nueva reserva de hotel después de realizar las validaciones necesarias.
     * @param request Datos de la solicitud de la reserva
     * @return DTO de la respuesta con los detalles de la reserva
     */
    @Override
    public HotelBookingResponseDTO save(HotelBookingRequestDTO request) {
        boolean existsByUserAndDateRange = repository.existsByUserAndDateRange(
                userService.findUser(request.getUser()),
                request.getCheckIn(),
                request.getCheckOut(),
                hotelBookingDetailService.findByHotelCode(request.getHotelCode())
        );
        if (existsByUserAndDateRange) {
            throw new HotelException("Ya tienes una reserva para este rango de fechas.");
        }
        if (!request.getCheckOut().isAfter(request.getCheckIn())) {
            throw new InvalidDateException("La fecha de checkout debe ser posterior a la fecha de checkin.");
        }
        HotelBooking booking = HotelBooking.builder()
                .checkIn(request.getCheckIn())
                .checkOut(request.getCheckOut())
                .user(userService.findUser(request.getUser()))
                .build();
        booking.setHotelBookingDetails(hotelBookingDetailService.generateStays(request, booking));
        booking.setTotalPrice(hotelBookingDetailService.calculateTotalPrice(
                booking.getHotelBookingDetails(),
                request.getCheckIn(),
                request.getCheckOut()
        ));
        repository.save(booking);
        return buildResponseDTO(booking);
    }

    /**
     * Obtiene todas las reservas de hotel.
     * @return Lista de DTOs con las reservas de hotel
     */
    @Override
    public List<HotelBookingResponseDTO> findAll() {
        List<HotelBooking> bookings = repository.findAll();
        if (bookings.isEmpty()) {
            throw new NoContentException("No hay reservas disponibles en este momento.");
        }
        return bookings.stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    /**
     * Busca una reserva por su ID.
     * @param id ID de la reserva
     * @return DTO de la reserva
     */
    @Override
    public HotelBookingResponseDTO findById(Long id) {
        return buildResponseDTO(findBookingOrThrow(id));
    }

    /**
     * Busca todas las reservas de un hotel específico por su ID.
     * @param hotelId ID del hotel
     * @return Lista de DTOs de reservas de hotel
     */
    @Override
    public List<HotelBookingResponseDTO> findByHotelId(Long hotelId) {
        List<HotelBooking> bookings = repository.findByHotelId(hotelId);
        if (bookings.isEmpty()) {
            throw new NoContentException("No hay reservas disponibles en este momento.");
        }
        return bookings.stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    /**
     * Elimina una reserva de hotel por su ID.
     * @param id ID de la reserva a eliminar
     */
    @Override
    public void delete(Long id) {
        repository.delete(findBookingOrThrow(id));
    }

    /**
     * Busca una reserva por su ID o lanza una excepción si no se encuentra.
     * @param id ID de la reserva
     * @return Reserva de hotel correspondiente al ID
     * @throws ResourceNotFoundException Si no se encuentra la reserva
     */
    private HotelBooking findBookingOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la reserva con ID: " + id));
    }

    /**
     * Busca el hotel asociado a una reserva.
     * @param booking Reserva de hotel
     * @return Hotel correspondiente a la reserva
     * @throws ResourceNotFoundException Si no se encuentra el hotel
     */
    private Hotel findHotel(HotelBooking booking) {
        return booking.getHotelBookingDetails().stream()
                .map(hotelBookingDetail -> hotelBookingDetail.getRoom().getHotel())
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el hotel"));
    }

    /**
     * Construye un DTO de respuesta a partir de una reserva de hotel.
     * @param booking Reserva de hotel
     * @return DTO de respuesta con los detalles de la reserva
     */
    private HotelBookingResponseDTO buildResponseDTO(HotelBooking booking) {
        return HotelBookingResponseDTO.builder()
                .reserveId(booking.getId())
                .hotelCode(findHotel(booking).getHotelCode())
                .checkIn(booking.getCheckIn())
                .checkOut(booking.getCheckOut())
                .totalPrice(booking.getTotalPrice())
                .user(booking.getUser().getId())
                .guests(hotelBookingDetailService.getStaysResponseDTOS(booking))
                .build();
    }
}
