package ch.uzh.ifi.hase.soprafs24.rest.dto.user;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;

public class UserPutDTO {

  private Long id;
  private String token;
  private String username;
  private String password;
  private LocalDate birthday;
  private String avatar;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
        return token;
    }

  public void setToken(String token) {
        this.token = token;
    }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setBirthday(LocalDate birthday){this.birthday = birthday;}

  public LocalDate getBirthday(){return birthday;}

  public String getAvatar() {
        return avatar;
    }

  public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

  public String getPassword() {
        return password;
    }

  public void setPassword(String password) {
        this.password = password;
    }
}
