package com.firdose.project.airBnbApp.controller;

import com.firdose.project.airBnbApp.dto.BookingDto;
import com.firdose.project.airBnbApp.dto.BookingRequest;
import com.firdose.project.airBnbApp.dto.GuestDto;
import com.firdose.project.airBnbApp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping(path = "/init")
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequest));
    }

    @PostMapping(path = "/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuestsToBooking(@PathVariable Long bookingId, @RequestBody List<GuestDto> guests) {
        BookingDto bookingDto = bookingService.addingGuestsToBooking(bookingId, guests);
        return ResponseEntity.ok(bookingDto);
    }
}
