package ru.practicum.shareit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long ownerId);

    List<Item> findAllByRequestId(Long requestId);

    List<Item> findAllByRequestIdIn(List<Long> requestIds);

    @Query("""
        SELECT i FROM Item i
        WHERE (upper(i.name) LIKE upper(concat('%', :text, '%'))
           OR upper(i.description) LIKE upper(concat('%', :text, '%')))
          AND i.available = true
    """)

    List<Item> search(@Param("text") String text);
}
