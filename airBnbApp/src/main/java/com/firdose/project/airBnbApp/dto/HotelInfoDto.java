package com.firdose.project.airBnbApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelInfoDto {

    private HotelDto hotel;
    private List<RoomDto> rooms;

}
