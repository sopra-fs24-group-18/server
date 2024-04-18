package ch.uzh.ifi.hase.soprafs24.rest.dto.question;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class QuestionGetDTO {
    private Long id;
    private long roomId;
    private GameMode gameMode;
    private long itemId;
    private String itemImage;
    private String itemList;//BUDGET multiple items
    private String itemImageList;//BUDGET multiple items
    private int roundNumber;
    private float budget; //for budget mode
    private float answer; //item price for guessing mode

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

    public String getItemImage() {
        return itemImage;
    }
    public void setItemImage(String itemImage) {
        this.itemImage =itemImage;
    }

    public String getItemList() {
        return itemList;
    }
    public void setItemList(String itemList) {
        this.itemList =itemList;
    }

    public String getItemImageList() {
        return itemImageList;
    }
    public void setItemImageList(String itemImageList) {
        this.itemImageList =itemImageList;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public float getBudget() {
        return budget;
    }
    public void setBudget(float budget) {
        this.budget = budget;
    }

    public float getAnswer() {
        return answer;
    }
    public void setAnswer(float answer) {
        this.answer = answer;
    }
}
