package com.springboot.order.service;

import com.springboot.coffee.service.CoffeeService;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.service.MemberService;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import com.springboot.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

// 다른 도메인에 접근할 때는 서비스 계층만 접근한다. findMember가 있는데 그걸 호출라는게 아니라 findVerfiiedMember를 호출
    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final CoffeeService coffeeService;

    public OrderService(OrderRepository orderRepository, MemberService memberService, CoffeeService coffeeService) {
        this.orderRepository = orderRepository;
        this.memberService = memberService;
        this.coffeeService = coffeeService;
    }

    // orderId는 항상 null이기 때문에 검증하면 안됨. 그래서 order.
    // memberId 검증해야돼. 존재하는 회원인지 알아야해. 휴대폰을 잃어버렸다고 했을때 누군가 app에 들어가서 주문을 누를 수 있음. 주문할 때 항상 누가, 무엇을 주문하는지 알아야함.
    // 상품 검증을 안하면 admin에서 delete를 누르는 순간 손님이 주문을 누를 수 있다.
    public Order createOrder(Order order) {
        // 회원이 존재하는지 검증
        memberService.findVerifiedMember(order.getMemberId());

        // 커피가 존재하는지 검증. 주문인데 커피를 접근하려면 주문+커피 테이블을 찾아야해
        Set<OrderCoffee> orderCoffees = order.getOrderCoffees();
        for (OrderCoffee orderCoffee : orderCoffees) {
            long coffeeId = orderCoffee.getCoffeeId();
            coffeeService.findVerifiedCoffee(coffeeId);
        }

        // ramda식 표현 방법
//        order.getOrderCoffees().stream()
//                .forEach(orderCoffee ->
//                        coffeeService.findVerifiedCoffee(orderCoffee.getCoffeeId()));
//        // 주문정보 저장을 위한 로직 실행
//        return orderRepository.save(order);

        // TODO order 객체는 나중에 DB에 저장 후, 되돌려 받는 것으로 변경 필요.
        return order;
    }

    public Order findOrder(long orderId) {
       return findVerifiedOrder(orderId);

        // TODO order 객체는 나중에 DB에서 조회 하는 것으로 변경 필요.
        return new Order(1L, 1L);
    }

    // 주문 수정 메서드는 사용하지 않습니다.

    public List<Order> findOrders() {
        // TODO should business logic
        List<Order> orders = (List<Order>) orderRepository.findAll();
        return orders;

        // TODO order 객체는 나중에 DB에서 조회하는 것으로 변경 필요.
        return List.of(new Order(1L, 1L),
                new Order(2L, 2L));
    }

    public void cancelOrder(long orderId) {
        Order findOrder = findVerifiedOrder(orderId); // 검증
        // 출발, 주문 완료 하면 안돼야해
        int stepNumber = findOrder.getOrderStatus().getStepNumber();
        if (stepNumber > 1) {
            throw new BusinessLogicException(ExceptionCode.CANNOT_CHANGE_ORDER);
        }
        findOrder.setOrderStatus(Order.OrderStatus.ORDER_CANCEL);
        orderRepository.save(findOrder);
    }

    // order에 set을 들어가서 순회해서 찾아야한다

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return findOrder = optionalOrder.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.ORDER_NOT_FOUND));

    }
}

