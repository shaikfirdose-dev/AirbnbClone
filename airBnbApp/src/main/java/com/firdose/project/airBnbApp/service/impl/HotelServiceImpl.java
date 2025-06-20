package com.firdose.project.airBnbApp.service.impl;

import com.firdose.project.airBnbApp.dto.HotelDto;
import com.firdose.project.airBnbApp.dto.HotelInfoDto;
import com.firdose.project.airBnbApp.dto.RoomDto;
import com.firdose.project.airBnbApp.entity.Hotel;
import com.firdose.project.airBnbApp.entity.Room;
import com.firdose.project.airBnbApp.exception.ResourceNotFoundException;
import com.firdose.project.airBnbApp.repository.HotelRepository;
import com.firdose.project.airBnbApp.repository.RoomRepository;
import com.firdose.project.airBnbApp.service.HotelService;
import com.firdose.project.airBnbApp.service.InventoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {


    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name : {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with hotel id : {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long hotelId) {
        log.info("Getting hotel by id : {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID : "+hotelId));
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating the hotel with ID : {}",id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with Id : {}"));

        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        log.info("Hotel has been updated");
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        log.info("Deleting the hotel with Id : {}",id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : "+id));

        log.info("Hotel deleted with id : {}",id);
        for(Room room : hotel.getRooms()){
            inventoryService.deleteAllInventories(room);
            roomRepository.deleteById(room.getId());
        }

        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activating the hotel with id : {}",hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : "+hotelId));
        hotel.setActive(true);
        log.info("Hotel has been activated");
        hotelRepository.save(hotel);
        //TODO: create inventory for all the rooms for this hotel

        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAnYear(room);
        }

    }

    @Override
    public List<HotelDto> getAllHotels() {
        log.info("Getting all the hotels");
        List<Hotel> hotels = hotelRepository.findAll();
        return hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelDto.class))
                .toList();
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        log.info("Getting hotel by id : {}", hotelId);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with ID : "+hotelId));
        List<RoomDto> rooms = hotel.getRooms().stream()
                .map(room -> modelMapper.map(room, RoomDto.class))
                .toList();

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }
}
