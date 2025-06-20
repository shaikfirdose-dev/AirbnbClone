package com.firdose.project.airBnbApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingRequest {

    private Long hotelId;
    private Long roomId;
    private Integer roomCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

}
