/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.dto.TopMemberDTO;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author crossover
 */
@RestController
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager entityManager;
    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PostMapping(path = "/api/member")
    public ResponseEntity<Member> register(@RequestBody Member p) {
        Member member = memberService.findByEmail(p.getEmail());

        if (member == null) {
            return ResponseEntity.ok(memberService.save(p));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/member")
    public ResponseEntity<List<Member>> getAll() {
        return ResponseEntity.ok(memberService.findAll());
    }

    /*
     * PLEASE DO NOT CHANGE API SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @GetMapping(path = "/api/member/{member-id}")
    public ResponseEntity<Member> getMemberById(@PathVariable(name = "member-id", required = true) Long memberId) {
        Member member = memberService.findById(memberId);
        if (member != null) {
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This API returns the top 5 members who issued the most books within the search duration. Only
     * books that have dateOfIssue and dateOfReturn within the mentioned duration should be counted.
     * Any issued book where dateOfIssue or dateOfReturn is outside the search, should not be
     * considered.
     *
     * DONT CHANGE METHOD SIGNATURE AND RETURN TYPES
     *
     * @return
     */
    @GetMapping(path = "/api/member/top-member")
    public ResponseEntity<List<TopMemberDTO>> getTopMembers(
            @RequestParam(value = "startTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startTime,
            @RequestParam(value = "endTime", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endTime) {
        List<TopMemberDTO> topDrivers = new ArrayList<>();
        /**
         * Your Implementation Here.
         *
         */
        String stringQuery = "SELECT NEW com.crossover.techtrial.dto.TopMemberDTO(m.id, m.name, m.email, COUNT(t.member.id) AS bookCount) FROM Member m, Transaction t WHERE m.id = t.member.id AND t.dateOfIssue BETWEEN :startTime AND :endTime AND t.dateOfReturn BETWEEN :startTime AND :endTime GROUP BY m.id ORDER BY bookCount DESC";

        TypedQuery<TopMemberDTO> typedQuery = entityManager
                .createQuery(stringQuery, TopMemberDTO.class)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setMaxResults(5);

        topDrivers = typedQuery.getResultList();

        return ResponseEntity.ok(topDrivers);
    }

}
