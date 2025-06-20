package com.firdose.project.airBnbApp.dto;

import com.firdose.project.airBnbApp.entity.User;
import com.firdose.project.airBnbApp.enums.Gender;
import lombok.Data;


@Data
public class GuestDto {

    private Long id;
    private String name;
    private Gender gender;
    private Integer age;
}
