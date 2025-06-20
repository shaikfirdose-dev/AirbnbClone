package com.firdose.project.airBnbApp.service.impl;

import com.firdose.project.airBnbApp.dto.RoomDto;
import com.firdose.project.airBnbApp.entity.Hotel;
import com.firdose.project.airBnbApp.entity.Room;
import com.firdose.project.airBnbApp.exception.ResourceNotFoundException;
import com.firdose.project.airBnbApp.repository.HotelRepository;
import com.firdose.project.airBnbApp.repository.RoomRepository;
import com.firdose.project.airBnbApp.service.InventoryService;
import com.firdose.project.airBnbApp.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public RoomDto createRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating the new room for hotel with id : {}", hotelId);
        log.info("Fetching the hotel with id : {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : "+hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        log.info("Room has been created");

        if(hotel.getActive()){
            log.info("Initializing inventory for the room");
            inventoryService.initializeRoomForAnYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all the rooms in a hotel with id : {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : "+hotelId));

        return hotel.getRooms()
                .stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting room with id : {}", roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room does not found with id : "+roomId));
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    @Transactional
    public void deleteRoomById(Long roomId) {
        log.info("Deleting the room with Id : {}",roomId);
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room does not found with id : "+roomId));

        inventoryService.deleteAllInventories(room);
        log.info("Room has been deleted with id : {}",roomId);
        roomRepository.deleteById(roomId);

    }
}
