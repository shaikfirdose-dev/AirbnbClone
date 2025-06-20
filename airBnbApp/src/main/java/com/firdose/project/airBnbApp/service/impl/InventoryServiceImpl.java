package com.firdose.project.airBnbApp.service.impl;

import com.firdose.project.airBnbApp.dto.HotelDto;
import com.firdose.project.airBnbApp.dto.HotelSearchRequest;
import com.firdose.project.airBnbApp.entity.Hotel;
import com.firdose.project.airBnbApp.entity.Inventory;
import com.firdose.project.airBnbApp.entity.Room;
import com.firdose.project.airBnbApp.repository.InventoryRepository;
import com.firdose.project.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void initializeRoomForAnYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);

        for(; !today.isAfter(endDate);today = today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .bookedCount(0)
                    .reservedCount(0)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventories(Room room) {
        log.info("Deleting inventory date after");
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        log.info("Searching hotels in city: {}, from: {}, to: {}, rooms count: {}",
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount());

        // Calculate the number of days between start and end date
        long dateCount = ChronoUnit.DAYS
                .between(hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate()) + 1;

        log.info("Number of days between start and end date: {}", dateCount);

        Page<Hotel> hotelPage = inventoryRepository.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                dateCount, pageable);


        return hotelPage.map(hotel -> modelMapper.map(hotel, HotelDto.class));

    }
}
