package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    @Test
    void createItem_WithValidData_ShouldReturnCreated() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto("Дрель", "Мощная дрель", true, null);
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Мощная дрель", true, null);

        when(itemService.createItem(any(CreateItemDto.class), anyLong())).thenReturn(itemDto);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void createItem_WithoutUserIdHeader_ShouldReturnBadRequest() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto("Дрель", "Мощная дрель", true, null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItem_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        CreateItemDto invalidDto = new CreateItemDto("", "", null, null);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_WithValidData_ShouldReturnOk() throws Exception {
        UpdateItemDto updateItemDto = new UpdateItemDto("Дрель Updated", "Новое описание", false);
        ItemDto itemDto = new ItemDto(1L, "Дрель Updated", "Новое описание", false, null);

        when(itemService.updateItem(anyLong(), any(UpdateItemDto.class), anyLong())).thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Дрель Updated"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void updateItem_WithNonExistingItem_ShouldReturnNotFound() throws Exception {
        UpdateItemDto updateItemDto = new UpdateItemDto("Дрель Updated", "Новое описание", false);

        when(itemService.updateItem(anyLong(), any(UpdateItemDto.class), anyLong()))
                .thenThrow(new NotFoundException("Вещь с ID 999 не найдена"));

        mockMvc.perform(patch("/items/999")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateItemDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getItem_WithExistingId_ShouldReturnItem() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Мощная дрель", true, null);

        when(itemService.getItemById(anyLong())).thenReturn(itemDto);

        mockMvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Дрель"));
    }

    @Test
    void getItem_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        when(itemService.getItemById(anyLong()))
                .thenThrow(new NotFoundException("Вещь с ID 999 не найдена"));

        mockMvc.perform(get("/items/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserItems_ShouldReturnItemList() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Мощная дрель", true, null);
        when(itemService.getUserItems(anyLong())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    void searchItems_WithValidText_ShouldReturnItemList() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Мощная дрель", true, null);
        when(itemService.searchItems(anyString())).thenReturn(List.of(itemDto));

        mockMvc.perform(get("/items/search")
                        .param("text", "дрель"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Дрель"));
    }

    @Test
    void searchItems_WithBlankText_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/items/search")
                        .param("text", "   "))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deleteItem_WithValidData_ShouldReturnNoContent() throws Exception {
        doNothing().when(itemService).deleteItem(anyLong(), anyLong());

        mockMvc.perform(delete("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNoContent());

        verify(itemService, times(1)).deleteItem(1L, 1L);
    }

    @Test
    void deleteItem_WithNonExistingItem_ShouldReturnNotFound() throws Exception {
        doThrow(new NotFoundException("Вещь с ID 999 не найдена"))
                .when(itemService).deleteItem(anyLong(), anyLong());

        mockMvc.perform(delete("/items/999")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound());
    }
}