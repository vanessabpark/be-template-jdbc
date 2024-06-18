package com.springboot.coffee.service;

import com.springboot.coffee.entity.Coffee;
import com.springboot.coffee.repository.CoffeeRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;

import com.springboot.member.repository.MemberRepository;
import com.springboot.order.entity.Order;
import com.springboot.order.entity.OrderCoffee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CoffeeService {

    private final CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    public Coffee createCoffee(Coffee coffee) {
        // 문자열일 경우 대소문자를 같은걸로 볼건지 항상 체크해야해. 커피코드로 검증. 왜 Id로 검증안하고 Coffee Code로 검증? CreateCoffee는 controller @PostMapping에서 호출하는거잖아. 만드는 시점에서는 기본키는 몰라. 데이블 들어갈 때 자동으로 생성되는거야 CoffeePostDto에 Id가 없어. Id는 항상 null이야 그래서 커피코드로 검증하는거야. 생성할 때 기본키로 검증한다면 코드가 잘못된거야. 항상 모든 데이터가 들어와 있을까라고 생각하면 안됨.
        String coffeeCode = coffee.getCoffeeCode().toUpperCase();

        verifyExistsCoffee(coffeeCode);
        coffee.setCoffeeCode(coffeeCode);

        return  coffeeRepository.save(coffee);
    }

    public Coffee updateCoffee(Coffee coffee) {

        // CoffeeController.java 에서 @PatchMapping가면 id를 받아
        Coffee findCoffee = findVerifiedCoffee(coffee.getCoffeeId()); // 검증

        Optional.ofNullable(coffee.getKorName()).ifPresent(korName -> findCoffee.setKorName(korName));
        Optional.ofNullable(coffee.getEngName()).ifPresent(engName -> findCoffee.setEngName(engName));
        Optional.ofNullable(coffee.getPrice()).ifPresent(price -> findCoffee.setPrice(price));

        return coffeeRepository.save(findCoffee); // 저장, 수정 모두 .save이다
    }

    public Coffee findCoffee(long coffeeId) {
        return findVerifiedCoffee(coffeeId);
    }

    public List<Coffee> findCoffees() {
        return (List<Coffee>)coffeeRepository.findAll();
    }

    public void deleteCoffee(long coffeeId) {
        Coffee findCoffee = findVerifiedCoffee(coffeeId);

        coffeeRepository.delete(findCoffee);
    }

    // 주문에 해당하는 커피 정보 조회
    public List<Coffee> findOrderedCoffees(Order order) {

        // order에서 Set<OrderCoffee>를 가져와서
        Set<OrderCoffee> orderedCoffees = order.getOrderCoffees();

        // Set<OrderCoffee>를 순회하며
        for (OrderCoffee orderCoffee : orderedCoffees) {
            // coffeeId를 가져온 후
            long coffeeId = orderCoffee.getCoffeeId();
            // 해당 coffeeId로 Coffee 객체를 찾은 후에
            Coffee coffee = findVerifiedCoffee(coffeeId);
            // 해당 Coffee를 List에 넣습니다
            coffees.add(coffee);
            // 모든 coffee가 List에 담기면 해당 List를 리턴합니다
    }
        return coffees;

        // 다른 방법으로 풀기
//        List<long> coffeeIds = order.getOrderCoffees().stream()
//                .map(orderCoffee -> orderCoffee.getCoffeeId())
//                .collect(Collectors.toList());
//        return coffeeIds.stream()
//                .map(coffeeId -> findVerifiedCoffee(coffeeId))
//                .collect(Collectors.toList());
//
    // order에 있는건 orderCoffee -> List 순회하면서 나온 Coffee Id를 가져와서 List에 담아야한다

}
    private void verifyExistsCoffee(String coffeeCode) { // void니까 검증만 하고 끝나는거야
        Optional<Coffee> coffee = coffeeRepository.findByCoffeeCode(coffeeCode);
        if (coffee.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.COFFEE_CODE_EXISTS);
        }
    }
    public Coffee findVerifiedCoffee(long coffeeId) { // void가 아니야
        Optional<Coffee> optionalCoffee = coffeeRepository.findById(coffeeId); // Optional는 null 값이나 값이 있다면 그걸 리턴. findByCoffee 써도 상관없어
        Coffee findCoffee = optionalCoffee.orElseThrow(() -> // 람다식. 맨 앞에 Coffee라는 건 커피 값만 들어온다. findCoffee 변수값에 담길때는 optionalCoffee 즉, Id 값이 있을 때만 변수에 할당 된다. optionalCoffee 뒤에 바로 짤려서 변수에 저장됨. 값이 없다면 .orElseThrow가 실행되서 new BusinessLogic 실행되서 not found 리턴함
                new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));
        return findCoffee;
    }



}


// 코드로 같은 코드가 있다면 가입하지말고 exception 던져야해 : coffee_code_exists