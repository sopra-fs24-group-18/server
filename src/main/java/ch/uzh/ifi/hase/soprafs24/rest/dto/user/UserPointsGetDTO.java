package ch.uzh.ifi.hase.soprafs24.rest.dto.user;

public class UserPointsGetDTO {
  private String username;
  private Long score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }
}
