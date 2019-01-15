/**
 *
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author crossover
 *
 */
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(Long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        return optionalMember.orElse(null);
    }

    public Member findByEmail(String memberEmail) {
        Optional<Member> optionalMember = memberRepository.findByEmail(memberEmail);
        return optionalMember.orElse(null);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

}
