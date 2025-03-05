package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findBookingsByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer userId,
                                                                                  LocalDateTime nowStart,
                                                                                  LocalDateTime nowEnd);

    List<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartDesc(Integer userId, LocalDateTime now);

    List<Booking> findBookingsByBookerIdAndStartAfterOrderByStartDesc(Integer userId, LocalDateTime now);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(Integer userId, BookingStatus status);

    List<Booking> findBookingsByBookerIdOrderByStartDesc(Integer userId);


    List<Booking> findBookingsByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Integer userId,
                                                                                     LocalDateTime nowStart,
                                                                                     LocalDateTime nowEnd);

    List<Booking> findBookingsByItemOwnerIdAndEndBeforeOrderByStartDesc(Integer userId, LocalDateTime now);

    List<Booking> findBookingsByItemOwnerIdAndStartAfterOrderByStartDesc(Integer userId, LocalDateTime now);

    List<Booking> findBookingsByItemOwnerIdAndStatusOrderByStartDesc(Integer userId, BookingStatus status);

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Integer userId);


    Booking findTopBookingByItemIdAndStatusNotAndStartBeforeOrderByEndDesc(Integer bookingId,
                                                                           BookingStatus status,
                                                                           LocalDateTime start);

    Booking findTopBookingByItemIdAndStatusNotAndStartAfterOrderByStartAsc(Integer bookingId,
                                                                           BookingStatus status,
                                                                           LocalDateTime start);
}