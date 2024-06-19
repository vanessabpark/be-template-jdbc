package com.springboot.order.dto;

import com.springboot.coffee.dto.OrderCoffeeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderPostDto {
    // 여기서 OrderPostDto로 바꿔주는건 Mapper가 하고있어
    // mapper가 비워주는거 못하니까 직접 설정해야해

    @Positive
    private long memberId;

    @Valid
    private List<OrderCoffeeDto> orderCoffees;

    // List 안에 이렇게 들어옴
//    {
//        "memberId": 1,
//            "orderCoffees": [
//        {
//            "coffeeId": 1,
//                "quantity": 2
//        },
//        {
//            "coffeeId": 2,
//                "quantity": 3
//        }
//    ]
//    }


}
