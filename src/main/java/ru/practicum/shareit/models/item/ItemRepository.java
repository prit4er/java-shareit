package ru.practicum.shareit.models.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("""
            SELECT i FROM Item i 
            WHERE i.available = true 
            AND (UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) 
            OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%')))
            """)
    List<Item> search(@Param("text") String text);

    @Query("""
            SELECT i FROM Item i 
            LEFT JOIN FETCH i.comments 
            LEFT JOIN FETCH i.bookings b 
            WHERE i.id = :id
            """)
    Optional<Item> findByIdInFull(@Param("id") Integer id);

    @Query("""
            SELECT i FROM Item i 
            LEFT JOIN FETCH i.comments 
            LEFT JOIN FETCH i.bookings b 
            WHERE i.owner.id = :ownerId
            """)
    List<Item> findAllByOwnerIdInFull(@Param("ownerId") Integer ownerId);
}
