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
        boolean hasBookings = hotel.getRooms().stream()
                .flatMap(room -> room.getBookings().stream())
                .anyMatch(booking -> booking.getCheckOut().isAfter(LocalDate.now()));

        if (hasBookings) {
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

    private List<RoomResponseDTO> getRoomResponseDTOS(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
        return hotel.getRooms().stream()
                .collect(Collectors.groupingBy(Room::getRoomType))
                .entrySet().stream()
                .map(entry -> RoomResponseDTO.builder()
                        .type(entry.getKey().name())
                        .pricePerNight(entry.getValue().get(0).getPricePerNight())
                        .availableRooms(countAvailableRoomsByType(
                                hotel, dateFrom, dateTo
                        ).getOrDefault(entry.getKey(), 0L).intValue())
                        .build())
                .toList();
    }


    private boolean isRoomAvailable(Room room, LocalDate dateFrom, LocalDate dateTo) {
        return room.getBookings().stream().noneMatch(booking ->
                (booking.getCheckIn().isBefore(dateTo) && booking.getCheckOut().isAfter(dateFrom))
        );
    }

    private Map<RoomType, Long> countAvailableRoomsByType(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
        return hotel.getRooms().stream()
                .filter(room -> isRoomAvailable(room, dateFrom, dateTo))
                .collect(Collectors.groupingBy(Room::getRoomType, Collectors.counting()));
    }
}
