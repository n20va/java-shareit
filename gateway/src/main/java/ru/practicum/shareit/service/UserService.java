package ru.practicum.shareit.service;

import ru.practicum.shareit.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto createUser(UserDto dto);

    UserDto updateUser(Long id, UserDto dto);

    void deleteUser(Long id);

}
