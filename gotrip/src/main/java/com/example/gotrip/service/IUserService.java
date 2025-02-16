package com.example.gotrip.service;

import com.example.gotrip.dto.UserRequestDTO;
import com.example.gotrip.dto.UserResponseDTO;
import com.example.gotrip.model.User;

import java.util.List;

public interface IUserService {
    public UserResponseDTO save(UserRequestDTO request);
    public List<UserResponseDTO> findAll();
    public UserResponseDTO findById(Long id);
    public User findUser(Long id);
    public UserResponseDTO update(Long id, UserRequestDTO request);
    public void delete(Long id);
}
