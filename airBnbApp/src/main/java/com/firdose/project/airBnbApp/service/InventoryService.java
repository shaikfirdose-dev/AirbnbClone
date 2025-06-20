package com.firdose.project.airBnbApp.service;

import com.firdose.project.airBnbApp.dto.HotelDto;
import com.firdose.project.airBnbApp.dto.HotelSearchRequest;
import com.firdose.project.airBnbApp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void initializeRoomForAnYear(Room room);
    void deleteAllInventories(Room room);

    Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
