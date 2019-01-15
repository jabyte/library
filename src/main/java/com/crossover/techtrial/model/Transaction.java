/**
 * 
 */
package com.crossover.techtrial.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * @author kshah
 *
 */
@Entity
@Table(name="transaction")
public class Transaction implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 8951221480021840448L;
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @NotNull
  @OneToOne
  @JoinColumn(name = "book_id", referencedColumnName = "id")
  private Book book;
  
  @NotNull
  @OneToOne
  @JoinColumn(name="member_id", referencedColumnName="id")
  private Member member;

  //Date and time of issuance of this book
  @Column(name="date_of_issue")
  private LocalDateTime dateOfIssue;
  
  //Date and time of return of this book
  @Column(name="date_of_return")
  private LocalDateTime dateOfReturn;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(Member member) {
    this.member = member;
  }

  public LocalDateTime getDateOfIssue() {
    return dateOfIssue;
  }

  public void setDateOfIssue(LocalDateTime dateOfIssue) {
    this.dateOfIssue = dateOfIssue;
  }

  public LocalDateTime getDateOfReturn() {
    return dateOfReturn;
  }

  public void setDateOfReturn(LocalDateTime dateOfReturn) {
    this.dateOfReturn = dateOfReturn;
  }

  @Override
  public String toString() {
    return "Transaction [id=" + id + ", book=" + book + ", member=" + member + ", dateOfIssue=" + dateOfIssue + ", dateOfReturn=" + dateOfReturn + "]";
  }

}
