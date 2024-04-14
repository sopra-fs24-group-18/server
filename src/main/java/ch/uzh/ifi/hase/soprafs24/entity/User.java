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
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private UserStatus status;

  @Column(name = "creation_date")
  private LocalDate creationDate;

  @Column
  private LocalDate birthday;

  @Column
  private Long score;

  @Column(name = "tool_list")
  private String toolList;

  @Column(name = "avatar")
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
