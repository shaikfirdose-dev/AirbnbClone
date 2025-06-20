package com.firdose.project.airBnbApp.service;

import com.firdose.project.airBnbApp.dto.HotelDto;
import com.firdose.project.airBnbApp.dto.HotelInfoDto;
import com.firdose.project.airBnbApp.entity.Hotel;

import java.util.List;

public interface HotelService {

    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long hotelId);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);
    void deleteHotelById(Long id);

    void activateHotel(Long hotelId);

    List<HotelDto> getAllHotels();

    HotelInfoDto getHotelInfoById(Long hotelId);
}
