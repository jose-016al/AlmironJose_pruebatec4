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

    @Override
    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::buildResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO findById(Long id) {
        return buildResponseDTO(findUserOrThrow(id));
    }

    @Override
    public User findUser(Long id) {
        return findUserOrThrow(id);
    }

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

    @Override
    public void delete(Long id) {
        repository.delete(findUserOrThrow(id));
    }

    private User findUserOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado el usuario con ID: " + id));
    }

    private UserResponseDTO buildResponseDTO(User user) {
        return UserResponseDTO.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .passport(user.getPassport())
                .build();
    }
}
