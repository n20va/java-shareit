package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.CommentService;
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

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentService commentService;

    private ItemServiceImpl itemService;

    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(itemRepository, userService, bookingRepository, commentService);
        owner = new User(1L, "Owner", "owner@example.com");
        item = new Item(1L, "Дрель", "Мощная дрель", true, 1L, null);
    }

    @Test
    void createItem_WithValidData_ShouldReturnItemDto() {
        CreateItemDto createItemDto = new CreateItemDto("Дрель", "Мощная дрель", true, null);

        when(userService.getUserEntity(anyLong())).thenReturn(owner);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.createItem(createItemDto, 1L);

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItemById_WithExistingItem_ShouldReturnItemDto() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentService.getCommentsByItemId(anyLong())).thenReturn(List.of());

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
        when(itemRepository.findByOwnerIdOrderById(anyLong())).thenReturn(List.of(item));
        when(commentService.getCommentsByItemIds(anyList())).thenReturn(List.of());

        List<ItemDto> result = itemService.getUserItems(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).findByOwnerIdOrderById(1L);
    }

    @Test
    void searchItems_WithBlankText_ShouldReturnEmptyList() {
        List<ItemDto> result = itemService.searchItems("   ");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(itemRepository, never()).searchAvailableItems(anyString());
    }

    @Test
    void searchItems_WithValidText_ShouldReturnItemList() {
        when(itemRepository.searchAvailableItems(anyString())).thenReturn(List.of(item));

        List<ItemDto> result = itemService.searchItems("дрель");

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(itemRepository, times(1)).searchAvailableItems("дрель");
    }

    @Test
    void updateItem_WithNonOwner_ShouldThrowNotFoundException() {
        UpdateItemDto updateItemDto = new UpdateItemDto("Дрель", "Мощная дрель", true);

        when(userService.getUserEntity(anyLong())).thenReturn(owner);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotFoundException.class, () ->
                itemService.updateItem(1L, updateItemDto, 2L));
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void getItemById_WhenUserIsOwner_ShouldReturnItemWithBookings() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentService.getCommentsByItemId(anyLong())).thenReturn(List.of());

        ItemDto result = itemService.getItemById(1L, 1L);

        assertNotNull(result);
        verify(itemRepository, times(1)).findById(1L);
    }

    @Test
    void getItemById_WhenUserNotOwner_ShouldReturnItemWithoutBookings() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(commentService.getCommentsByItemId(anyLong())).thenReturn(List.of());

        ItemDto result = itemService.getItemById(1L, 2L);

        assertNotNull(result);
        verify(itemRepository, times(1)).findById(1L);
    }
}