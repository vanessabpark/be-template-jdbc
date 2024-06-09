package com.springboot.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {
    private long memberId;
    private long coffeeId;
}
