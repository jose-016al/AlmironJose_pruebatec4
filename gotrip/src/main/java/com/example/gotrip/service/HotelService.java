package com.example.gotrip.service;

import com.example.gotrip.dto.HotelRequestDTO;
import com.example.gotrip.dto.HotelResponseDTO;
import com.example.gotrip.dto.RoomRequestDTO;
import com.example.gotrip.dto.RoomResponseDTO;
import com.example.gotrip.exception.HotelException;
import com.example.gotrip.exception.NoContentException;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.Hotel;
import com.example.gotrip.model.Room;
import com.example.gotrip.repository.HotelRepository;
import com.example.gotrip.util.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {

    private final HotelRepository repository;

    /**
     * Guarda un nuevo hotel en la base de datos.
     *
     * @param request Datos del hotel a guardar.
     * @return Los datos del hotel guardado.
     */
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

    /**
     * Recupera todos los hoteles de la base de datos.
     *
     * @return Lista de hoteles.
     */
    @Override
    public List<HotelResponseDTO> findAll() {
        List<Hotel> hotels = repository.findAll();
        if (hotels.isEmpty()) {
            throw new NoContentException("No hay hoteles disponibles en este momento.");
        }
        return hotels.stream()
                .map(hotel -> buildResponseDTO(hotel, LocalDate.now(), LocalDate.now()))
                .toList();
    }

    /**
     * Busca hoteles según las fechas de reserva y el destino.
     *
     * @param dateFrom Fecha de inicio de la búsqueda.
     * @param dateTo Fecha de fin de la búsqueda.
     * @param destination Destino de búsqueda.
     * @return Lista de hoteles que coinciden con la búsqueda.
     */
    @Override
    public List<HotelResponseDTO> searchHotels(LocalDate dateFrom, LocalDate dateTo, String destination) {
        List<Hotel> hotels = repository.findAll();
        if (hotels.isEmpty()) {
            throw new NoContentException("No hay hoteles disponibles en este momento.");
        }
        return hotels.stream()
                .filter(hotel -> hotel.getLocation().equalsIgnoreCase(destination))
                .map(hotel -> buildResponseDTO(hotel, dateFrom, dateTo))
                .toList();
    }

    /**
     * Busca un hotel por su ID.
     *
     * @param id El ID del hotel.
     * @return Los datos del hotel encontrado.
     */
    @Override
    public HotelResponseDTO findById(Long id) {
        return buildResponseDTO(findHotelOrThrow(id), LocalDate.now(), LocalDate.now());
    }

    /**
     * Busca un hotel por su código único.
     *
     * @param hotelCode El código del hotel.
     * @return El hotel encontrado.
     */
    @Override
    public Hotel findByHotelCode(String hotelCode) {
        return repository.findByHotelCode(hotelCode)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el hotel con el codigo: " + hotelCode));
    }

    /**
     * Actualiza los datos de un hotel existente. Solo se actualizarán los campos que se proporcionen.
     *
     * @param id El ID del hotel a actualizar.
     * @param name El nuevo nombre del hotel (opcional).
     * @param location La nueva ubicación del hotel (opcional).
     * @param stars El nuevo número de estrellas del hotel (opcional).
     * @return Los datos del hotel actualizado.
     */
    @Override
    public HotelResponseDTO update(Long id, String name, String location, Integer stars) {
        Hotel hotel = findHotelOrThrow(id);
        if (name != null) hotel.setName(name);
        if (location != null) hotel.setLocation(location);
        if (stars != null) hotel.setStars(stars);
        repository.save(hotel);
        return buildResponseDTO(hotel, LocalDate.now(), LocalDate.now());
    }

    /**
     * Elimina un hotel de la base de datos si no tiene reservas activas.
     *
     * @param id El ID del hotel a eliminar.
     */
    @Override
    public void delete(Long id) {
        Hotel hotel = findHotelOrThrow(id);

        if (repository.hasActiveBookings(hotel, LocalDate.now())) {
            throw new HotelException("No se puede eliminar el hotel porque tiene reservas activas.");
        }

        repository.delete(hotel);
    }

    /**
     * Genera un código único para el hotel basado en su ubicación.
     *
     * @param location Ubicación del hotel.
     * @return El código generado para el hotel.
     */
    private String generateHotelCode(String location) {
        String locationCode = location.substring(0, Math.min(location.length(), 3)).toUpperCase();
        int randomNumber = (int) (Math.random() * 9000) + 1000;
        String hotelCode = locationCode + "-" + randomNumber;

        return repository.findByHotelCode(hotelCode).isPresent() ? generateHotelCode(location) : hotelCode;
    }

    /**
     * Crea las habitaciones para un hotel a partir de los datos proporcionados.
     *
     * @param rooms Lista de habitaciones a crear.
     * @param hotel El hotel al que pertenecen las habitaciones.
     * @return Lista de habitaciones creadas.
     */
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

    /**
     * Busca un hotel por su ID y lanza una excepción si no se encuentra.
     *
     * @param id El ID del hotel a buscar.
     * @return El hotel encontrado.
     */
    private Hotel findHotelOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el hotel con ID: " + id));
    }

    /**
     * Construye un objeto DTO para la respuesta de un hotel.
     *
     * @param hotel El hotel a convertir en DTO.
     * @param dateFrom Fecha de inicio de la búsqueda.
     * @param dateTo Fecha de fin de la búsqueda.
     * @return El DTO del hotel.
     */
    private HotelResponseDTO buildResponseDTO(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
        return HotelResponseDTO.builder()
                .hotelCode(hotel.getHotelCode())
                .name(hotel.getName())
                .location(hotel.getLocation())
                .stars(hotel.getStars())
                .rooms(getRoomResponseDTOS(hotel, dateFrom, dateTo))
                .build();
    }

    /**
     * Obtiene la lista de habitaciones disponibles para un hotel en un rango de fechas.
     *
     * @param hotel El hotel para el que se buscan las habitaciones disponibles.
     * @param dateFrom Fecha de inicio de la búsqueda.
     * @param dateTo Fecha de fin de la búsqueda.
     * @return Lista de habitaciones disponibles.
     */
    private List<RoomResponseDTO> getRoomResponseDTOS(Hotel hotel, LocalDate dateFrom, LocalDate dateTo) {
        List<Object[]> availableRooms = repository.countAvailableRoomsByType(hotel, dateFrom, dateTo);

        return availableRooms.stream()
                .map(entry -> RoomResponseDTO.builder()
                        .type(((RoomType) entry[0]).name())
                        .availableRooms(((Long) entry[1]).intValue())
                        .pricePerNight(getPricePerNightByRoomType(hotel, (RoomType) entry[0]))
                        .build())
                .toList();
    }

    /**
     * Obtiene el precio por noche de un tipo de habitación.
     *
     * @param hotel El hotel al que pertenece la habitación.
     * @param roomType El tipo de habitación.
     * @return El precio por noche de la habitación.
     */
    private double getPricePerNightByRoomType(Hotel hotel, RoomType roomType) {
        return hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == roomType)
                .findFirst()
                .map(Room::getPricePerNight)
                .orElse(0.0);

    }

}
