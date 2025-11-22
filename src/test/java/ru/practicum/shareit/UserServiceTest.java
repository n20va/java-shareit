package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void createUser_WithValidData_ShouldReturnUserDto() {
        // given
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john@example.com");
        User user = new User(1L, "John Doe", "john@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserDto result = userService.createUser(createUserDto);

        // then
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowConflictException() {
        // given
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john@example.com");
        User existingUser = new User(1L, "John Doe", "john@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // when & then
        assertThrows(ConflictException.class, () -> userService.createUser(createUserDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_WithExistingUser_ShouldReturnUserDto() {
        // given
        User user = new User(1L, "John Doe", "john@example.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        // when
        UserDto result = userService.getUserById(1L);

        // then
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos() {
        // given
        User user = new User(1L, "John Doe", "john@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // when
        List<UserDto> result = userService.getAllUsers();

        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }
}