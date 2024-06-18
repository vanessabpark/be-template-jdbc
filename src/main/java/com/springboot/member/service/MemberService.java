package com.springboot.member.service;

import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import com.springboot.member.entity.Member;
import com.springboot.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * V2
 *  - 메서드 구현
 *  - DI 적용
 */
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member createMember(Member member) {
        verifyExistsEmail(member.getEmail());
        verifyExistsPhone(member.getPhone());
;
        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        // 존재하는 회원인지 체크
        Member findMember = findVerifiedMember(member.getMemberId()); // 받았다는 것은 이미 있다는 것
        // 이름 정보와 휴대폰 번호 엡데이트
        // member
        if (member.getName() != null) {
            findMember.setName(member.getName());
        }
        if (member.getPhone() != null) {
            findMember.setPhone(member.getPhone());
        }
        // 위 if문 두 개랑 아래 Optional 코드랑 같은 내용이야. 아래 내용이 더 advacned 한 내용이야
        Optional.ofNullable(member.getName()).ifPresent(name -> findMember.setName(name));
        Optional.ofNullable(member.getPhone()).ifPresent(phone -> findMember.setPhone(phone));

        return memberRepository.save(findMember);
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public List<Member> findMembers() {
        List<Member> members = (List<Member>)memberRepository.findAll();
        return members;
    }

    public void deleteMember(long memberId) {
        Member findMember = findVerifiedMember(memberId);

        memberRepository.delete(findMember);
    }

    private void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
        }
    }
    private void verifyExistsPhone(String phone) {
        Optional<Member> member = memberRepository.findByPhone(phone);
        if(member.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.MEMBER_PHONE_EXISTS);
        }
    }

    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId); // null이 아니면 이걸 실행
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND)); // null이면 예외 발생

        return findMember;
    }
}
