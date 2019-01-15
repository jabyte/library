/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.repositories.MemberRepository;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.TransactionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author kshah
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BookService bookService;

    @Test
    public void testMemberRegsitrationsuccessful() {

        memberRepository.deleteAll();

        HttpEntity<Object> member = getHttpEntity(
                "{\"name\": \"Jabir\", \"email\": \"sminjibir@gmail.com\","
                        + " \"membershipStatus\": \"ACTIVE\",\"membershipStartDate\":\"2018-08-08T12:12:12\" }");

        ResponseEntity<Member> response = template.postForEntity("/api/member", member, Member.class);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Jabir", response.getBody().getName());

        //cleanup the user
        memberRepository.deleteById(response.getBody().getId());
    }

    @Test
    public void testGetMemberById() {
        Member m1 = new Member();
        m1.setName("Utah");
        m1.setEmail("utajazz@naija.ng");
        m1.setMembershipStatus(MembershipStatus.ACTIVE);
        m1.setMembershipStartDate(LocalDateTime.of(2018, 8, 8, 12, 12, 12));
        memberRepository.save(m1);

        ResponseEntity<Member> responseEntity = template.getForEntity("/api/member/" + m1.getId(), Member.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(m1.getId(), responseEntity.getBody().getId());
        assertEquals("utajazz@naija.ng", responseEntity.getBody().getEmail());

        // Tidy database up
        memberRepository.delete(m1);
    }

    @Test
    public void testGetAll() {

        memberRepository.deleteAll();

        Member m1 = new Member();
        m1.setName("Utah");
        m1.setEmail("utajazz@naija.ng");
        m1.setMembershipStatus(MembershipStatus.ACTIVE);
        m1.setMembershipStartDate(LocalDateTime.of(2018, 8, 8, 12, 12, 12));
        memberRepository.save(m1);

        Member m2 = new Member();
        m2.setName("Jabir");
        m2.setEmail("minjibir@gmail.com");
        m2.setMembershipStatus(MembershipStatus.ACTIVE);
        m2.setMembershipStartDate(LocalDateTime.of(2018, 10, 1, 0, 0, 0));
        memberRepository.save(m2);

        ResponseEntity responseEntity = template.getForEntity("/api/member", Object.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).isNotNull();

        // Tidy database up
        memberRepository.deleteAll();
    }

    @Test
    public void testGetTopMembers() {

        memberRepository.deleteAll();
        transactionService.getAll();
        bookService.getAll();

        Member m1 = new Member();
        m1.setName("Utah");
        m1.setEmail("utajazz@naija.ng");
        m1.setMembershipStatus(MembershipStatus.ACTIVE);
        m1.setMembershipStartDate(LocalDateTime.of(2018, 8, 8, 12, 12, 12));
        memberRepository.save(m1);

        Member m2 = new Member();
        m2.setName("Jabir");
        m2.setEmail("minjibir@gmail.com");
        m2.setMembershipStatus(MembershipStatus.ACTIVE);
        m2.setMembershipStartDate(LocalDateTime.of(2018, 10, 1, 0, 0, 0));
        memberRepository.save(m2);

        Book b1 = new Book();
        b1.setTitle("Java for Dummies");
        bookService.save(b1);

        Book b2 = new Book();
        b2.setTitle("Beginning Spring Boot");
        bookService.save(b2);

        Transaction t1 = new Transaction();
        t1.setMember(m1);
        t1.setBook(b1);
        t1.setDateOfIssue(LocalDateTime.now());
        t1.setDateOfReturn(LocalDateTime.of(2019, 5, 30, 0, 0, 0));
        transactionService.save(t1);

        Transaction t2 = new Transaction();
        t2.setMember(m2);
        t2.setBook(b2);
        t2.setDateOfIssue(LocalDateTime.now());
        t2.setDateOfReturn(LocalDateTime.of(2019, 3, 10, 0, 0, 0));
        transactionService.save(t2);

        ResponseEntity responseEntity = template.getForEntity("/api/member/top-member?startTime=2018-01-01T00:00:00&endTime=2019-12-31T00:00:00", Object.class);

        List<Member> members = (ArrayList) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(members);
        assertThat(members.size()).isGreaterThan(0);
        // Tidy database up
        memberRepository.deleteAll();
    }

    private HttpEntity<Object> getHttpEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<Object>(body, headers);
    }
}
