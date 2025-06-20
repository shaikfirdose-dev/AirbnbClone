package com.firdose.project.airBnbApp.service.impl;

import com.firdose.project.airBnbApp.dto.BookingDto;
import com.firdose.project.airBnbApp.dto.BookingRequest;
import com.firdose.project.airBnbApp.dto.GuestDto;
import com.firdose.project.airBnbApp.entity.*;
import com.firdose.project.airBnbApp.enums.BookingStatus;
import com.firdose.project.airBnbApp.repository.*;
import com.firdose.project.airBnbApp.service.BookingService;
import com.firdose.project.airBnbApp.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;


    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequest bookingRequest) {
        log.info("Initializing booking for request: {}", bookingRequest);

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found with id: " + bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(
                room.getId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomCount()
        );

        long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate())+1;

        log.info("Found {} available inventories for room {} in hotel {} for {} days",
                inventoryList.size(), room.getId(), hotel.getId(), daysCount);

        // Check if the number of available inventories matches the requested room count for each day
        if(inventoryList.size()!=daysCount){
            throw new IllegalStateException("Room is not available anymore for the requested dates");
        }

        //Reserve the room / update the booked count of the inventory

        for(Inventory inventory : inventoryList) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomCount());
        }

        inventoryRepository.saveAll(inventoryList);

        //Create the Booking

        User user = getCurrentUser();

        //Calculate Dynamic Price

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .user(user)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .roomCount(bookingRequest.getRoomCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);
        log.info("Booking created with id: {}", booking.getId());
        return modelMapper.map(booking, BookingDto.class);
    }

    private User getCurrentUser() {
        User user = new User();
        user.setId(1L);
        return user;
    }

    @Override
    public BookingDto addingGuestsToBooking(Long bookingId, List<GuestDto> guests) {
        log.info("Adding guests to booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

        if(hasExpiredBooking(booking)) {
            log.error("Booking with id: {} has expired ", bookingId);
            throw new IllegalStateException("Booking has already expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED) {
            log.error("Booking with id: {} is not in RESERVED state, current status: {}", bookingId, booking.getBookingStatus());
            throw new IllegalStateException("Booking is not under reserved state, cannot add guests");
        }

        for (GuestDto guestDto : guests){
            Guest guestEntity = modelMapper.map(guestDto, Guest.class);
            guestEntity.setUser(getCurrentUser());
            guestEntity = guestRepository.save(guestEntity);
            booking.getGuests().add(guestEntity);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);
        log.info("Guests added to booking with id: {}", booking.getId());
        return modelMapper.map(booking, BookingDto.class);
    }

    private boolean hasExpiredBooking(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }
}
