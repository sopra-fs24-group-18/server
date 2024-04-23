package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
@SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
  private Long id;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus status;

  @Column
  private LocalDate creationDate;

  @Column
  private LocalDate birthday;

  @Column
  private Long score;

  @Column
  private String toolStatus;

  @Column
  private String toolList;

  @Column
  private String avatar;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public LocalDate getCreationDate() { return creationDate; }

  public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

  public LocalDate getBirthday() { return birthday; }

  public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

  public Long getScore() {
      return score;
  }

  public void setScore(Long score) {
      this.score = score;
  }

  public String getToolStatus() {
      return toolStatus;
  }

  public void setToolStatus(String toolStatus) {
      this.toolStatus = toolStatus;
  }

  public String getToolList() {
      return toolList;
  }

  public void setToolList(String toolList) {
      this.toolList = toolList;
  }

  public String getAvatar() {
        return avatar;
    }

  public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}
