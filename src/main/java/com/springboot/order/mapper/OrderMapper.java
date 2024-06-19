package com.springboot.order.mapper;

import com.springboot.coffee.dto.OrderCoffeeDto;
import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.service.CoffeeService;
import com.springboot.order.dto.OrderCoffeeResponseDto;
import com.springboot.order.entity.Order;
import com.springboot.order.dto.OrderPostDto;
import com.springboot.order.dto.OrderResponseDto;
import com.springboot.order.entity.OrderCoffee;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default Order orderPostDtoToOrder(OrderPostDto orderPostDto) { // 커피 정보를 담기 위해서는 orderCoffee에 거쳐야해. OrderPostDtos는 뭐를 보내는지 생각
        Order order = new Order();
        order.setMemberId(order.getMemberId());

        // option 1
        Set<OrderCoffee> orderCoffees = new LinkedHashSet<>(); // 순회. 관계 테이블에 한 정보는 여기있음. 이 친구는 만들어져. orderCoffee를 만들어서 추가해야돼. 여기에 데이터 2개 들어오는게 검증이 안되잖아.
        List<OrderCoffeeDto> orderCoffeeDtos = orderPostDto.getOrderCoffees();
        for(OrderCoffeeDto orderCoffeeDto : orderCoffeeDtos) {
            OrderCoffee orderCoffee = OrderCoffee.builder()
                    .coffeeId(orderCoffeeDto.getCoffeeId())
                    .quantity(orderCoffeeDto.getQuantity())
                    .build();
            orderCoffees.add(orderCoffee);
        }
        // option 2: ramda
        Set<OrderCoffee> orderCoffees = orderPostDto.getOrderCoffees()
                .stream()
                .map(orderCoffeeDto ->
                        OrderCoffee.builder()
                                .coffeeId(orderCoffeeDto.getCoffeeId())
                                .quantity(orderCoffeeDto.getQuantity())
                                .build()
                ).collect(Collectors.toSet());

        order.setOrderCoffees(orderCoffees);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(Order.OrderStatus.ORDER_REQUEST);
        return order;
    }
    default OrderResponseDto orderToOrderResponseDto(Order order) {
        long memberId = order.getMemberId();

        List<OrderCoffeeResponseDto> orderCoffees =
                orderCoffeesToOrderCoffeeResponseDtos(coffeeService, order.getOrderCoffees());

        orderResponseDto orderResponseDto = new OrderResponseDto(
                order.getOrderId(),
                order.getMemberId(),
                order.getOrderStatus(),
                orderCoffees,
                order.getCreatedAt()
        );
        return orderResponseDto;

    }

    default List<OrderCoffeeResponseDto> orderCoffeesToOrderCoffeeResponseDtos
            (CoffeeService coffeeService
             ,Set<OrderCoffee> orderCoffees) {
        List<OrderCoffeeResponseDto> result = new ArrayList<>();
        for(OrderCoffee orderCoffee: orderCoffees) {
            Coffee coffee = coffeeService.findVerifiedCoffee(orderCoffee.getOrderCoffeeId());

            OrderCoffeeResponseDto dto = OrderCoffeeResponseDto.builder()
                    .coffeeId(coffee.getCoffeeId())
                    .korName(coffee.getKorName())
                    .engName(coffee.getEngName())
                    .price(coffee.getPrice())
                    .quantity(orderCoffee.getQuantity())
                    .coffeeCode(coffee.getCoffeeCode())
                    .build();

            result.add(dto);
        }
        return result;
    }
}
