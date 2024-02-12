package com.ssafy.lam.reserve.dto;

import com.ssafy.lam.reserve.domain.Reserve;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReserveResponseDto {
    private String customerName;
    private String hospitalName;
    private int reserveType;
    private int year;
    private int month;
    private int day;
    private String dayofweek;
    private int time;

    private String hospitalProfileBase64;
    private String hospitalProfileType;

    @Builder
    public ReserveResponseDto(String customerName, String hospitalName, int reserveType, int year, int month, int day, String dayofweek, int time) {
        this.customerName = customerName;
        this.hospitalName = hospitalName;
        this.reserveType = reserveType;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayofweek = dayofweek;
        this.time = time;
    }
}