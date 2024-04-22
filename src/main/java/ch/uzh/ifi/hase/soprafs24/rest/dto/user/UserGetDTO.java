package ch.uzh.ifi.hase.soprafs24.rest.dto.user;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;

import javax.persistence.Column;
import java.time.LocalDate;

public class UserGetDTO {

  private Long id;
//  private String name;
  private String username;
//  private String token;
  private UserStatus status;
  private LocalDate creationDate;
  private LocalDate birthday;

  private String avatar;


  private Long score;


  private String toolStatus;


  private String toolList;


    public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

//  public String getName() {
//    return name;
//  }
//
//  public void setName(String name) {
//    this.name = name;
//  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

//  public void setToken(String token) {
//        this.token = token;
//    }
//
//  public String getToken() {
//        return token;
//    }

  public UserStatus getStatus() {
    return status;
  }

  public void setStatus(UserStatus status) {
    this.status = status;
  }

  public void setCreationDate(LocalDate creationDate){this.creationDate = creationDate;}

  public LocalDate getCreationDate(){return creationDate;}

  public void setBirthday(LocalDate birthday){this.birthday = birthday;}

  public LocalDate getBirthday(){return birthday;}

  public String getAvatar() {
        return avatar;
    }

  public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
