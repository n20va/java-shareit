package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;

public class UpdateUserDto {
    private String name;

    @Email(message = "Некорректный формат email")
    private String email;

    public UpdateUserDto() {
    }

    public UpdateUserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}