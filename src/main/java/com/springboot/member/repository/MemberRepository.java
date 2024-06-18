package com.springboot.member.repository;

import com.springboot.member.entity.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MemberRepository extends CrudRepository<Member, Long> {

    Optional<Member> findByEmail(String email); // 실제로 이 findBy 뒤에 'Email' 적으면 해당 column명으로 찾는다. 이메일로 조회 가능
    Optional<Member> findByPhone(String phone); // 데이터베이스 컬럼명 쓰면 안돼. Entity 변수명으로 써야돼
}

