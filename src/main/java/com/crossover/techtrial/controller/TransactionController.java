/**
 *
 */
package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.model.Member;
import com.crossover.techtrial.model.Transaction;
import com.crossover.techtrial.service.BookService;
import com.crossover.techtrial.service.MemberService;
import com.crossover.techtrial.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author kshah
 *
 */
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BookService bookService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager entityManager;

    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS Example Post
     * Request : { "bookId":1,"memberId":33 }
     */
    @PostMapping(path = "/api/transaction")
    public ResponseEntity<Transaction> issueBookToMember(@RequestBody Map<String, Long> params) {

        Long bookId = params.get("bookId");
        Long memberId = params.get("memberId");

        Book book = bookService.findById(bookId);
        Member member = memberService.findById(memberId);

        if (member != null && book != null) {

            int unreturndedBooksCount = entityManager
                    .createQuery("SELECT t FROM Transaction t WHERE t.member.id = :memberId AND t.dateOfReturn IS NULL")
                    .setParameter("memberId", member.getId()).getResultList().size();

            int bookIssuedCount = entityManager
                    .createQuery("SELECT t FROM Transaction t WHERE t.book.id = :bookId AND dateOfReturn IS NULL")
                    .setParameter("bookId", book.getId()).getResultList().size();

            if (unreturndedBooksCount < 5 && bookIssuedCount < 1) {
                Transaction transaction = new Transaction();

                transaction.setBook(bookService.findById(bookId));
                transaction.setMember(memberService.findById(memberId));
                transaction.setDateOfIssue(LocalDateTime.now());

                Transaction t = transactionService.save(transaction);

                return ResponseEntity.ok().body(t);
            }

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.notFound().build();
    }
    /*
     * PLEASE DO NOT CHANGE SIGNATURE OR METHOD TYPE OF END POINTS
     */
    @PatchMapping(path = "/api/transaction/{transaction-id}/return")
    public ResponseEntity<Transaction> returnBookTransaction(
            @PathVariable(name = "transaction-id") Long transactionId) {

        Optional<Transaction> optionalTransaction = transactionService.findById(transactionId);

        if (optionalTransaction.isPresent()) {

            Transaction transaction = optionalTransaction.get();

            if (transaction.getDateOfReturn() == null) {
                transaction.setDateOfReturn(LocalDateTime.now());
                transactionService.save(transaction);
                return ResponseEntity.ok(transaction);
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(transaction);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}
