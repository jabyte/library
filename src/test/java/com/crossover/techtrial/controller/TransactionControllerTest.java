package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.MembershipStatus;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

    @LocalServerPort
    Long port;

    @Autowired
    BookService bookService;

    @Autowired
    MemberService memberService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void issueBookToMemberTest() {
        Book book = new Book();
        book.setTitle("Spring-Boot Applications");
        bookService.save(book);

        Member member = new Member();
        member.setName("Jabir");
        member.setEmail("sminjibir@gmail.com");
        member.setMembershipStatus(MembershipStatus.ACTIVE);
        member.setMembershipStartDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        memberService.save(member);

        HttpEntity<Object> tranxJson =
                getHttpEntity("{\"bookId\":" + book.getId() + ", \"memberId\":" + member.getId() + "}");

        ResponseEntity<Transaction> responseEntity = testRestTemplate
                .postForEntity("/api/transaction", tranxJson, Transaction.class);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(book.getTitle(), responseEntity.getBody().getBook().getTitle());
        assertEquals(member.getName(), responseEntity.getBody().getMember().getName());

        transactionService.delete(responseEntity.getBody().getId());
        memberService.delete(member.getId());
        bookService.delete(book.getId());
    }

    @Test
    public void returnBookTransactionTest() throws IOException {

        Book book = new Book();
        book.setTitle("Spring-Boot Applications");
        bookService.save(book);

        Member member = new Member();
        member.setName("Jabir");
        member.setEmail("sminjibir@gmail.com");
        member.setMembershipStatus(MembershipStatus.ACTIVE);
        member.setMembershipStartDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
        memberService.save(member);

        Transaction transaction = new Transaction();
        transaction.setDateOfIssue(LocalDateTime.of(2018, 12, 1, 0, 0, 0));
        transaction.setMember(member);
        transaction.setBook(book);
        transactionService.save(transaction);

        String url = "http://localhost:" + port + "/api/transaction/" + transaction.getId() + "/return";

        HttpPatch httpPatch = new HttpPatch(url);
        httpPatch.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        int respnseCode = HttpClientBuilder.create()
                .build()
                .execute(httpPatch)
                .getStatusLine()
                .getStatusCode();

        assertEquals(200, respnseCode);

        transactionService.delete(transaction.getId());
        memberService.delete(member.getId());
        bookService.delete(book.getId());
    }

    private HttpEntity<Object> getHttpEntity(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<Object>(object, headers);
    }
}
