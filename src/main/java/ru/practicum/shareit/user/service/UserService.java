package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(CreateUserDto createUserDto) {
        userRepository.findByEmail(createUserDto.getEmail())
                .ifPresent(user -> {
                    throw new ConflictException("Пользователь с email "
                            + createUserDto.getEmail() + " уже существует");
                });

        User user = UserMapper.toUserFromCreateDto(createUserDto);
        User savedUser = userRepository.save(user);
        return UserMapper.toUserDto(savedUser);
    }

    public UserDto updateUser(Long userId, UpdateUserDto updateUserDto) {
        User existingUser = getUserByIdOrThrow(userId);

        if (updateUserDto.getEmail() != null
                && !updateUserDto.getEmail().equals(existingUser.getEmail())) {
            userRepository.findByEmail(updateUserDto.getEmail())
                    .ifPresent(user -> {
                        throw new ConflictException("Пользователь с email "
                                + updateUserDto.getEmail() + " уже существует");
                    });
        }

        User updatedUser = UserMapper.updateUserFromDto(updateUserDto, existingUser);
        User savedUser = userRepository.update(updatedUser);
        return UserMapper.toUserDto(savedUser);
    }

    public UserDto getUserById(Long userId) {
        User user = getUserByIdOrThrow(userId);
        return UserMapper.toUserDto(user);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

    public User getUserEntity(Long userId) {
        return getUserByIdOrThrow(userId);
    }

    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID "
                        + userId + " не найден"));
    }
}