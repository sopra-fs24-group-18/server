package ch.uzh.ifi.hase.soprafs24.rest.dto.question;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

public class QuestionGetDTO {
    private Long id;
    private long roomId;
    private GameMode gameMode;
    private long itemId;
    private String itemList;//BUDGET multiple items


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }
    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Long getItemId() {
        return itemId;
    }
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemList() {
        return itemList;
    }
    public void setItemList(String itemList) {
        this.itemList =itemList;
    }
}
