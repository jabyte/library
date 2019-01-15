/**
 * 
 */
package com.crossover.techtrial.service;

import com.crossover.techtrial.model.Book;

import java.util.List;

/**
 * BookService interface for Books.
 * @author cossover
 *
 */
public interface BookService {
  
  List<Book> getAll();
  
  Book save(Book p);
  
  Book findById(Long bookId);

  void delete(Long id);
}
