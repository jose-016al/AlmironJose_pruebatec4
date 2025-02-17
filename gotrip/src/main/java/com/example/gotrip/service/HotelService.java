package com.example.gotrip.service;

import com.example.gotrip.dto.HotelRequestDTO;
import com.example.gotrip.dto.HotelResponseDTO;
import com.example.gotrip.dto.RoomRequestDTO;
import com.example.gotrip.dto.RoomResponseDTO;
import com.example.gotrip.exception.HotelException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.model.Room;
import com.example.gotrip.repository.HotelRepository;
import com.example.gotrip.util.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {

    private final HotelRepository repository;

    @Override
    public HotelResponseDTO save(HotelRequestDTO request) {
        Hotel hotel = Hotel.builder()
                .hotelCode(generateHotelCode(request.getLocation()))
                .name(request.getName())
                .location(request.getLocation())
                .stars(request.getStars())
                .build();
        hotel.setRooms(createRoomsForHotel(request.getRooms(), hotel));
        repository.save(hotel);
        return buildResponseDTO(hotel, LocalDate.now(), LocalDate.now());
    }

    @Override
    public List<HotelResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(hotel -> buildResponseDTO(hotel, LocalDate.now(), LocalDate.now()))
                .toList();
    }

    @Override
    public List<HotelResponseDTO> searchHotels(LocalDate dateFrom, LocalDate dateTo, String destination) {
        return repository.findAll().stream()
                .filter(hotel -> hotel.getLocation().equalsIgnoreCase(destination))
                .map(hotel -> buildResponseDTO(hotel, dateFrom, dateTo))
                .toList();
    }

    @Override
    public HotelResponseDTO findById(Long id) {
        return buildResponseDTO(findHotelOrThrow(id), LocalDate.now(), LocalDate.now());
    }

    @Override
    public Hotel findByHotelCode(String hotelCode) {
        return repository.findByHotelCode(hotelCode)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el hotel con el codigo: " + hotelCode));
    }

    @Override
    public HotelResponseDTO update(Long id, HotelRequestDTO request) {
        Hotel hotel = findHotelOrThrow(id);
        hotel.setName(request.getName());
        hotel.setLocation(request.getLocation());
        hotel.setStars(request.getStars());
        repository.save(hotel);
        return buildResponseDTO(hotel, LocalDate.now(), LocalDate.now());
    }

    @Override
    public void delete(Long id) {
        Hotel hotel = findHotelOrThrow(id);

        if (repository.hasActiveBookings(hotel, LocalDate.now())) {
            throw new HotelException("No se puede eliminar el hotel porque tiene reservas activas.");
        }

        repository.delete(hotel);
    }

    private String generateHotelCode(String location) {
        String locationCode = location.substring(0, Math.min(location.length(), 3)).toUpperCase();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String hotelCode = locationCode + "-" + randomNumber;

        return repository.findByHotelCode(hotelCode).isPresent() ? generateHotelCode(location) : hotelCode;
    }

    private List<Room> createRoomsForHotel(List<RoomRequestDTO> rooms, Hotel hotel) {
        final int[] roomNumberCounter = {1};

        return rooms.stream()
                .flatMap(room -> {
                    return IntStream.range(0, room.getQuantity())
                            .mapToObj(i -> {
                                int roomNumber = roomNumberCounter[0]++;

                                return Room.builder()
                                        .number(roomNumber)
                                        .roomType(RoomType.valueOf(room.getType().toUpperCase()))
                                        .pricePerNight(room.getPricePerNight())
                                        .hotel(hotel)
                                        .build();
                            });
                })
                .toList();
    }

    private Hotel findHotelOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el hotel con ID: " + id));
    }

    private HotelResponseDTO buildResponseDTO(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
        return HotelResponseDTO.builder()
                .hotelCode(hotel.getHotelCode())
                .name(hotel.getName())
                .location(hotel.getLocation())
                .stars(hotel.getStars())
                .rooms(getRoomResponseDTOS(hotel, dateFrom, dateTo))
                .build();
    }

    public List<RoomResponseDTO> getRoomResponseDTOS(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
        List<Object[]> availableRooms = repository.countAvailableRoomsByType(hotel, dateFrom, dateTo);

        return availableRooms.stream()
                .map(entry -> RoomResponseDTO.builder()
                        .type(((RoomType) entry[0]).name())
                        .availableRooms(((Long) entry[1]).intValue())
                        .pricePerNight(getPricePerNightByRoomType(hotel, (RoomType) entry[0]))
                        .build())
                .toList();
    }

    private double getPricePerNightByRoomType(Hotel hotel, RoomType roomType) {
        return hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == roomType)
                .findFirst()
                .map(Room::getPricePerNight)
                .orElse(0.0);

    }

}
