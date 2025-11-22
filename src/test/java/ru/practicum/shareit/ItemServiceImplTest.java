package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ItemServiceImpl itemService;

    private User owner;
    private Item item;
    private CreateItemDto createItemDto;
    private UpdateItemDto updateItemDto;

    @BeforeEach
    void setUp() {
        owner = new User(1L, "Owner", "owner@example.com");
        item = new Item(1L, "Дрель", "Мощная дрель", true, 1L, null);
        createItemDto = new CreateItemDto("Дрель", "Мощная дрель", true, null);
        updateItemDto = new UpdateItemDto("Дрель Updated", "Обновленное описание", false);
    }

    @Test
    void createItem_WithValidData_ShouldReturnItemDto() {
        when(userService.getUserEntity(anyLong())).thenReturn(owner);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.createItem(createItemDto, 1L);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createItem_WithNonExistingUser_ShouldThrowNotFoundException() {
        when(userService.getUserEntity(anyLong())).thenThrow(new NotFoundException("Пользователь не найден"));

        assertThrows(NotFoundException.class, () -> itemService.createItem(createItemDto, 999L));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void createItem_WithInvalidData_ShouldThrowValidationException() {
        CreateItemDto invalidDto = new CreateItemDto("", "", null, null);

        assertThrows(ValidationException.class, () -> itemService.createItem(invalidDto, 1L));
    }

    @Test
    void updateItem_WithValidData_ShouldReturnUpdatedItemDto() {
        when(userService.getUserEntity(anyLong())).thenReturn(owner);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.update(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.updateItem(1L, updateItemDto, 1L);

        assertNotNull(result);
        verify(itemRepository, times(1)).update(any(Item.class));
    }

    @Test
    void updateItem_WithNonOwner_ShouldThrowNotFoundException() {
        when(userService.getUserEntity(anyLong())).thenReturn(owner);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () -> itemService.updateItem(1L, updateItemDto, 2L));
        verify(itemRepository, never()).update(any(Item.class));
    }

    @Test
    void getItemById_WithExistingItem_ShouldReturnItemDto() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto result = itemService.getItemById(1L);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void getItemById_WithNonExistingItem_ShouldThrowNotFoundException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(999L));
    }

    @Test
    void getUserItems_WithExistingUser_ShouldReturnItemList() {
        when(userService.getUserEntity(anyLong())).thenReturn(owner);
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));

        List<ItemDto> result = itemService.getUserItems(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).findByOwnerId(1L);
    }

    @Test
    void searchItems_WithBlankText_ShouldReturnEmptyList() {
        List<ItemDto> result = itemService.searchItems("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, never()).search(anyString());
    }

    @Test
    void searchItems_WithValidText_ShouldReturnItemList() {
        when(itemRepository.search(anyString())).thenReturn(List.of(item));

        List<ItemDto> result = itemService.searchItems("дрель");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).search("дрель");
    }

    @Test
    void deleteItem_WithValidData_ShouldCallRepository() {
        when(userService.getUserEntity(anyLong())).thenReturn(owner);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        itemService.deleteItem(1L, 1L);

        verify(itemRepository, times(1)).deleteById(1L);
    }
}