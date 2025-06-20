package com.firdose.project.airBnbApp.controller;

import com.firdose.project.airBnbApp.dto.RoomDto;
import com.firdose.project.airBnbApp.entity.Room;
import com.firdose.project.airBnbApp.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping(path = "/")
    public ResponseEntity<RoomDto> createRoom(@PathVariable Long hotelId, @RequestBody RoomDto roomDto){
         RoomDto room = roomService.createRoom(hotelId, roomDto);
         return new ResponseEntity<>(room, HttpStatus.CREATED);
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<RoomDto>> getAllRoomsInHotel(@PathVariable Long hotelId){
        List<RoomDto> rooms = roomService.getAllRoomsInHotel(hotelId);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping(path = "/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long roomId){
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }

    @DeleteMapping(path = "/{roomId}")
    public ResponseEntity<Void> deleteRoomById(@PathVariable Long roomId){
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }

}
