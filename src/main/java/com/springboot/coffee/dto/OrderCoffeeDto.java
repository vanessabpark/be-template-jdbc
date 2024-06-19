package com.springboot.coffee.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCoffeeDto {
    @Positive
    private long coffeeId;
    @Positive
    private int quantity;
}
