package com.example.gotrip.service;

import com.example.gotrip.exception.HotelException;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.model.HotelBookingDetail;
import com.example.gotrip.model.Room;
import com.example.gotrip.repository.RoomRepository;
import com.example.gotrip.util.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final RoomRepository repository;
    private final IHotelService hotelService;

    /**
     * Método para encontrar habitaciones disponibles de un hotel específico.
     *
     * @param hotelCode El código del hotel donde buscar habitaciones.
     * @param checkIn La fecha de check-in.
     * @param checkOut La fecha de check-out.
     * @param roomType El tipo de habitación que se busca.
     * @param numberOfRooms El número de habitaciones que se desean reservar.
     * @return Lista de habitaciones disponibles.
     * @throws HotelException Si no hay suficientes habitaciones disponibles.
     */
    @Override
    public List<Room> findRoomAvailable(String hotelCode, LocalDate checkIn, LocalDate checkOut, RoomType roomType, int numberOfRooms) {
        List<Room> availableRooms = repository.findAvailableRooms(hotelCode, checkIn, checkOut, roomType);

        if (availableRooms.size() < numberOfRooms) {
            throw new HotelException("No hay suficientes habitaciones disponibles de tipo " + roomType);
        }

        return availableRooms;
    }

    /**
     * Método para obtener un hotel a partir de su código.
     *
     * @param hotelCode El código del hotel.
     * @return El objeto Hotel correspondiente al código proporcionado.
     */
    @Override
    public Hotel findByHotelCode(String hotelCode) {
        return hotelService.findByHotelCode(hotelCode);
    }

    /**
     * Método para calcular el precio total de una reserva de hotel.
     *
     * @param bookingDetails Los detalles de la reserva.
     * @param checkIn La fecha de check-in.
     * @param checkOut La fecha de check-out.
     * @return El precio total de la reserva.
     */
    @Override
    public double calculateTotalPrice(List<HotelBookingDetail> bookingDetails,
                                      LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return bookingDetails.stream()
                .mapToDouble(detail -> {
                    Room room = detail.getRoom();
                    return room != null ? room.getPricePerNight() * nights : 0;
                })
                .sum();
    }

}
