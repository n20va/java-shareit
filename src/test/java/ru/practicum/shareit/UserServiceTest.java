package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.CreateUserDto;
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
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john@example.com");
        User user = new User(1L, "John Doe", "john@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto result = userService.createUser(createUserDto);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_WithDuplicateEmail_ShouldThrowConflictException() {
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john@example.com");
        User existingUser = new User(1L, "John Doe", "john@example.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        assertThrows(ConflictException.class, () -> userService.createUser(createUserDto));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void getUserById_WithExistingUser_ShouldReturnUserDto() {
        User user = new User(1L, "John Doe", "john@example.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUserDtos() {
        User user = new User(1L, "John Doe", "john@example.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }
}