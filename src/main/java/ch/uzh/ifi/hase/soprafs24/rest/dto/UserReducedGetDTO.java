package ch.uzh.ifi.hase.soprafs24.rest.dto;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import java.time.LocalDate;

public class UserReducedGetDTO {

  private Long id;
  private String token;
  private String username;
  private UserStatus status;
  private LocalDate creationDate;
  private LocalDate birthday;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setToken(String token) {
        this.token = token;
    }

  public String getToken() {
        return token;
    }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {this.status = status;}

  public void setCreationDate(LocalDate creationDate){this.creationDate = creationDate;}

  public LocalDate getCreationDate(){return creationDate;}

  public void setBirthday(LocalDate birthday){this.birthday = birthday;}

  public LocalDate getBirthday(){return birthday;}

 }
