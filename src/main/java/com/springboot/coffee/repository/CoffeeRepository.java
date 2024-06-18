package com.springboot.coffee.repository;

import com.springboot.coffee.entity.Coffee;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CoffeeRepository extends CrudRepository<Coffee, Long> {
    Optional<Coffee> findByCoffeeCode(String coffeeCode); // null이 들어올 수 도 있다. 커피코드가 없을수도 있으니까. Null이면 예외를 던진다.
    Optional<Coffee> findById(Long coffeeId);

    // @Query("SELECT * FROM COFFEE WHERE COFFEE_ID = :coffeeId") // 직접 쿼리문 작성 가능: 예시

}
