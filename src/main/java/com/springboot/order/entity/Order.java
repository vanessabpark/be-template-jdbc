package com.springboot.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ORDERS")
public class Order {
    @Id
    private long orderId;
    private long memberId; // 물리적으로 들어와야 한다. Order에 이 값이 들어온다고 해도 모른다. 관계 설정을 해줘야 함.

    @MappedCollection(idColumn = "ORDER_ID")
    private Set<OrderCoffee> orderCoffees = new LinkedHashSet<>(); // 순회. 관계 테이블에 한 정보는 여기있음. 이 친구는 만들어져. orderCoffee를 만들어서 추가해야돼

    private OrderStatus orderStatus = OrderStatus.ORDER_REQUEST;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum OrderStatus {
        ORDER_REQUEST(1, "주문 요청"),
        ORDER_CONFIRM(2, "주문 확정"),
        ORDER_COMPLETE(3, "주문 완료"),
        ORDER_CANCEL(4, "주문 취소");

        @Getter
        private int stepNumber; // cancelOrder에서 이거 또는 밑에 stepDescription 사용가능
        @Getter
        private String stepDescription;

        OrderStatus(int stepNumber, String stepDescription) {
            this.stepNumber = stepNumber;
            this.stepDescription = stepDescription;
        }
    }
}
