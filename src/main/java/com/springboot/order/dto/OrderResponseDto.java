package com.springboot.order.dto;

import com.springboot.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {
    private long memberId;
    private long coffeeId;
    private Order.OrderStatus orderStatus;
    private List<OrderCoffeeResponseDto> orderCoffees;
    private LocalDateTime createdAt;

}
