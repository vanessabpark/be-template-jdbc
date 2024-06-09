package com.springboot.order.mapper;

import com.springboot.order.entity.Order;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.dto.OrderResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order orderPostDtoToOrder(OrderPostDto orderPostDto);
    OrderResponseDto orderToOrderResponseDto(Order order);
}
