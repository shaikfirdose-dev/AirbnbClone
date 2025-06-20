package com.firdose.project.airBnbApp.controller;

import com.firdose.project.airBnbApp.dto.HotelDto;
import com.firdose.project.airBnbApp.dto.HotelInfoDto;
import com.firdose.project.airBnbApp.dto.HotelSearchRequest;
import com.firdose.project.airBnbApp.service.HotelService;
import com.firdose.project.airBnbApp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowserController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;;

    @GetMapping(path = "/search")
    public ResponseEntity<Page<HotelDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {

        Page<HotelDto> page = inventoryService.searchHotels(hotelSearchRequest);

        return ResponseEntity.ok(page);

    }


    @GetMapping(path = "/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));

    }


}
