package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toUserDto_WithValidUser_ShouldReturnUserDto() {
        User user = new User(1L, "John Doe", "john@example.com");

        UserDto userDto = UserMapper.toUserDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void toUserDto_WithNullUser_ShouldReturnNull() {
        UserDto userDto = UserMapper.toUserDto(null);

        assertNull(userDto);
    }

    @Test
    void toUserFromCreateDto_WithValidDto_ShouldReturnUser() {
        CreateUserDto createUserDto = new CreateUserDto("John Doe", "john@example.com");

        User user = UserMapper.toUserFromCreateDto(createUserDto);

        assertNotNull(user);
        assertEquals(createUserDto.getName(), user.getName());
        assertEquals(createUserDto.getEmail(), user.getEmail());
    }

    @Test
    void updateUserFromDto_WithValidData_ShouldUpdateUser() {
        User user = new User(1L, "Old Name", "old@example.com");
        UpdateUserDto updateUserDto = new UpdateUserDto("New Name", "new@example.com");

        User updatedUser = UserMapper.updateUserFromDto(updateUserDto, user);

        assertEquals("New Name", updatedUser.getName());
        assertEquals("new@example.com", updatedUser.getEmail());
    }

    @Test
    void updateUserFromDto_WithPartialData_ShouldUpdateOnlyProvidedFields() {
        User user = new User(1L, "Old Name", "old@example.com");
        UpdateUserDto updateUserDto = new UpdateUserDto("New Name", null);

        User updatedUser = UserMapper.updateUserFromDto(updateUserDto, user);

        assertEquals("New Name", updatedUser.getName());
        assertEquals("old@example.com", updatedUser.getEmail());
    }
}