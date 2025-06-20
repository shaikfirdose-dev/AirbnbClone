package com.firdose.project.airBnbApp.dto;

import com.firdose.project.airBnbApp.entity.HotelContactInfo;
import lombok.*;

@Data
public class HotelDto {
    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private Boolean active;
    private HotelContactInfo contactInfo;
}
