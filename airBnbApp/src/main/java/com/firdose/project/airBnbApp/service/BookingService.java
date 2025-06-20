package com.firdose.project.airBnbApp.service;

import com.firdose.project.airBnbApp.dto.BookingDto;
import com.firdose.project.airBnbApp.dto.BookingRequest;
import com.firdose.project.airBnbApp.dto.GuestDto;

import java.util.List;

public interface BookingService {
    BookingDto initializeBooking(BookingRequest bookingRequest);

    BookingDto addingGuestsToBooking(Long bookingId, List<GuestDto> guests);
}
