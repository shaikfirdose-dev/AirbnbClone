package com.firdose.project.airBnbApp.dto;

import com.firdose.project.airBnbApp.entity.Hotel;
import com.firdose.project.airBnbApp.entity.Room;
import com.firdose.project.airBnbApp.entity.User;
import com.firdose.project.airBnbApp.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {

    private Long id;
    private Integer roomCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;
}
