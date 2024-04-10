package ch.uzh.ifi.hase.soprafs24.rest.dto.room;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

public class RoomPostDTO {

  private String name;
  private Long ownerId;

  private GameMode gameMode;
  private Long playerAmount;

  public String getName() {
      return name;
  }

  public void setName(String name) {
      this.name = name;
  }

  public Long getOwnerId() {
      return ownerId;
  }

  public void setOwnerId(Long ownerId) {
      this.ownerId = ownerId;
  }

  public GameMode getGameMode() {
      return gameMode;
  }

  public void setGameMode(GameMode gameMode) {
      this.gameMode = gameMode;
  }

  public Long getPlayerAmount() {
      return playerAmount;
  }

  public void setPlayerAmount(Long playerAmount) {
      this.playerAmount = playerAmount;
  }
}
