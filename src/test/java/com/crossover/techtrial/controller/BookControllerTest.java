package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.Book;
import com.crossover.techtrial.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void getBooksTest() {
        Book b1, b2;
        b1 = new Book();
        b2 = new Book();

        b1.setTitle("Scala Basics");
        b2.setTitle("Scala Basics");

        bookService.save(b1);
        bookService.save(b2);

        ResponseEntity<Object> responseEntity = testRestTemplate.getForEntity("/api/book", Object.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertThat(responseEntity.getBody()).isNotNull();

        bookService.delete(b1.getId());
        bookService.delete(b2.getId());
    }

    @Test
    public void saveBookTest() {
        HttpEntity<Object> book = getHttpEntity("{\"title\": \"Unit Testing with Spring-Boot\"}");

        ResponseEntity<Book> responseEntity = testRestTemplate.postForEntity("/api/book", book, Book.class);

        assertEquals(201, responseEntity.getStatusCodeValue());
        assertEquals("Unit Testing with Spring-Boot", responseEntity.getBody().getTitle());
        assertNotNull(responseEntity.getBody().getId());

        bookService.delete(responseEntity.getBody().getId());
    }

    @Test
    public void getBookByIdTest() {
        Book book = new Book();
        book.setTitle("Scala Basics");
        bookService.save(book);

        ResponseEntity<Book> responseEntity = testRestTemplate.getForEntity("/api/book/" + book.getId(), Book.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(book.getId(), responseEntity.getBody().getId());
        assertEquals(book.getTitle(), responseEntity.getBody().getTitle());

        bookService.delete(book.getId());
    }

    private HttpEntity<Object> getHttpEntity(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<Object>(object, headers);
    }
}
