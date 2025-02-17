package com.example.gotrip.service;

import com.example.gotrip.dto.UserRequestDTO;
import com.example.gotrip.dto.UserResponseDTO;
import com.example.gotrip.exception.ResourceNotFoundException;
import com.example.gotrip.model.User;
import com.example.gotrip.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;

    /**
     * Método para guardar un nuevo usuario en el sistema.
     *
     * @param request El DTO con los datos del usuario a crear.
     * @return El DTO de respuesta con los datos del usuario guardado.
     */
    @Override
    public UserResponseDTO save(UserRequestDTO request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .passport(request.getPassport())
                .build();
        repository.save(user);
        return buildResponseDTO(user);
    }

    /**
     * Método para recuperar todos los usuarios del sistema.
     *
     * @return Una lista de DTOs con los datos de todos los usuarios.
     */
    @Override
    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    /**
     * Método para encontrar un usuario por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return El DTO de respuesta con los datos del usuario encontrado.
     * @throws ResourceNotFoundException Si no se encuentra el usuario con el ID proporcionado.
     */
    @Override
    public UserResponseDTO findById(Long id) {
        return buildResponseDTO(findUserOrThrow(id));
    }

    /**
     * Método para obtener un usuario por su ID sin convertir a DTO.
     *
     * @param id El ID del usuario a buscar.
     * @return El usuario encontrado.
     * @throws ResourceNotFoundException Si no se encuentra el usuario con el ID proporcionado.
     */
    @Override
    public User findUser(Long id) {
        return findUserOrThrow(id);
    }

    /**
     * Método para actualizar los datos de un usuario existente.
     *
     * @param id El ID del usuario a actualizar.
     * @param request El DTO con los nuevos datos del usuario.
     * @return El DTO de respuesta con los datos actualizados del usuario.
     * @throws ResourceNotFoundException Si no se encuentra el usuario con el ID proporcionado.
     */
    @Override
    public UserResponseDTO update(Long id, UserRequestDTO request) {
        User user = findUserOrThrow(id);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassport(request.getPassport());

        repository.save(user);
        return buildResponseDTO(user);
    }

    /**
     * Método para eliminar un usuario por su ID.
     *
     * @param id El ID del usuario a eliminar.
     * @throws ResourceNotFoundException Si no se encuentra el usuario con el ID proporcionado.
     */
    @Override
    public void delete(Long id) {
        repository.delete(findUserOrThrow(id));
    }

    /**
     * Método privado para buscar un usuario por su ID. Lanza una excepción si no lo encuentra.
     *
     * @param id El ID del usuario a buscar.
     * @return El usuario encontrado.
     * @throws ResourceNotFoundException Si no se encuentra el usuario con el ID proporcionado.
     */
    private User findUserOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el usuario con ID: " + id));
    }

    /**
     * Método privado para construir un DTO de respuesta a partir de un usuario.
     *
     * @param user El usuario a convertir a DTO.
     * @return El DTO de respuesta con los datos del usuario.
     */
    private UserResponseDTO buildResponseDTO(User user) {
        return UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .passport(user.getPassport())
                .build();
    }
}
