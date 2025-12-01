package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;
    private final CommentService commentService;

    @Override
    @Transactional
    public ItemDto createItem(CreateItemDto createItemDto, Long ownerId) {
        userService.getUserEntity(ownerId);

        Item item = ItemMapper.toItemFromCreateDto(createItemDto, ownerId);
        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long itemId, UpdateItemDto updateItemDto, Long ownerId) {
        userService.getUserEntity(ownerId);

        Item existingItem = getItemByIdOrThrow(itemId);

        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }

        ItemMapper.updateItemFromDto(updateItemDto, existingItem);
        Item savedItem = itemRepository.save(existingItem);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return getItemById(itemId, null);
    }

    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = getItemByIdOrThrow(itemId);

        ItemDto.BookingInfo lastBooking = null;
        ItemDto.BookingInfo nextBooking = null;
        List<CommentDto> comments = commentService.getCommentsByItemId(itemId);

        if (userId != null && item.getOwnerId().equals(userId)) {
            lastBooking = getLastBooking(itemId);
            nextBooking = getNextBooking(itemId);
        }

        return ItemMapper.toItemDto(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> getUserItems(Long ownerId) {
        userService.getUserEntity(ownerId);

        List<Item> items = itemRepository.findByOwnerIdOrderById(ownerId);
        List<Long> itemIds = items.stream()
                .filter(Objects::nonNull)
                .map(Item::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // Используем оптимизированные методы для получения всех бронирований за один запрос
        Map<Long, ItemDto.BookingInfo> lastBookings = getLastBookingsForItems(itemIds);
        Map<Long, ItemDto.BookingInfo> nextBookings = getNextBookingsForItems(itemIds);

        List<CommentDto> allComments = commentService.getCommentsByItemIds(itemIds);
        Map<Long, List<CommentDto>> commentsByItem = allComments.stream()
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        return items.stream()
                .map(item -> {
                    ItemDto.BookingInfo lastBooking = lastBookings.get(item.getId());
                    ItemDto.BookingInfo nextBooking = nextBookings.get(item.getId());
                    List<CommentDto> comments = commentsByItem.getOrDefault(item.getId(), Collections.emptyList());
                    return ItemMapper.toItemDto(item, lastBooking, nextBooking, comments);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<Item> items = itemRepository.searchAvailableItems(text);
        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .collect(Collectors.toList());

        List<CommentDto> allComments = commentService.getCommentsByItemIds(itemIds);
        Map<Long, List<CommentDto>> commentsByItem = allComments.stream()
                .collect(Collectors.groupingBy(CommentDto::getItemId));

        return items.stream()
                .map(item -> {
                    List<CommentDto> comments = commentsByItem.getOrDefault(item.getId(), Collections.emptyList());
                    return ItemMapper.toItemDtoWithComments(item, comments);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId, Long ownerId) {
        userService.getUserEntity(ownerId);

        Item item = getItemByIdOrThrow(itemId);

        if (!item.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Пользователь не является владельцем вещи");
        }

        itemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, CreateCommentDto createCommentDto, Long authorId) {
        return commentService.createComment(itemId, createCommentDto, authorId);
    }

    private Item getItemByIdOrThrow(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));
    }

    private ItemDto.BookingInfo getLastBooking(Long itemId) {
        return bookingRepository
                .findFirstByItemIdAndStartBeforeAndStatusOrderByStartDesc(
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .map(this::toBookingInfo)
                .orElse(null);
    }

    private ItemDto.BookingInfo getNextBooking(Long itemId) {
        return bookingRepository
                .findFirstByItemIdAndStartAfterAndStatusOrderByStartAsc(
                        itemId, LocalDateTime.now(), BookingStatus.APPROVED)
                .map(this::toBookingInfo)
                .orElse(null);
    }

    private Map<Long, ItemDto.BookingInfo> getLastBookingsForItems(List<Long> itemIds) {
        if (itemIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> lastBookings = bookingRepository.findLastBookingsForItems(
                itemIds, now, BookingStatus.APPROVED
        );

        return lastBookings.stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        this::toBookingInfo
                ));
    }

    private Map<Long, ItemDto.BookingInfo> getNextBookingsForItems(List<Long> itemIds) {
        if (itemIds.isEmpty()) {
            return Collections.emptyMap();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Booking> nextBookings = bookingRepository.findNextBookingsForItems(
                itemIds, now, BookingStatus.APPROVED
        );

        return nextBookings.stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        this::toBookingInfo
                ));
    }

    private ItemDto.BookingInfo toBookingInfo(Booking booking) {
        if (booking == null || booking.getBooker() == null) {
            return null;
        }
        return new ItemDto.BookingInfo(
            booking.getId(),
            booking.getBooker().getId(),
            booking.getStart(),
            booking.getEnd()
        );
    }
}


