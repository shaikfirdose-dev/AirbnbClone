package com.firdose.project.airBnbApp.service;

import com.firdose.project.airBnbApp.dto.RoomDto;

import java.util.List;

public interface RoomService {
    RoomDto createRoom(Long hotelId, RoomDto roomDto);
    List<RoomDto> getAllRoomsInHotel(Long hotelId);
    RoomDto getRoomById(Long roomId);
    void deleteRoomById(Long roomId);
}
