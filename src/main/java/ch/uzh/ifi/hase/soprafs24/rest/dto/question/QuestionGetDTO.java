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
    private int leftRange;
    private int rightRange;
    private int originLeftRange;
    private int originRightRange;
    private boolean blur = false;



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

    public int getLeftRange() {
        return leftRange;
    }
    public void setLeftRange(int leftRange) {
        this.leftRange = leftRange;
    }
    public int getRightRange() {
        return rightRange;
    }
    public void setRightRange(int rightRange) {
        this.rightRange = rightRange;
    }

    public int getOriginLeftRange() {
        return originLeftRange;
    }
    public void setOriginLeftRange(int originLeftRange) {
        this.originLeftRange = originLeftRange;
    }
    public int getOriginRightRange() {
        return originRightRange;
    }
    public void setOriginRightRange(int originRightRange) {
        this.originRightRange = originRightRange;
    }


    public boolean getBlur() {
        return blur;
    }
    public void setBlur(boolean blur) {
        this.blur = blur;
    }
}
