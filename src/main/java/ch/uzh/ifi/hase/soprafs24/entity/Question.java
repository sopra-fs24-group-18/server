package ch.uzh.ifi.hase.soprafs24.entity;
import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "QUESTION")
@SequenceGenerator(name = "question_seq", sequenceName = "question_sequence", allocationSize = 1)
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "question_seq")
    private Long id;

    @Column(nullable = false)
    private long roomId;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column(nullable = true) //GUESSING single item
    private long itemId;

    @Column(nullable = true)
    private String itemImage;

    @Lob
    @Column(nullable = true)
    private String itemList;//BUDGET multiple items

    @Lob
    @Column(nullable = true)
    private String itemImageList;//BUDGET multiple items

    @Column(nullable = false)
    private int roundNumber;
    @Column(nullable = true)
    private float budget; //for budget mode

    @Lob
    @Column(nullable = true)
    private String selectedItemList;//selected item list to calculate budget

    @Column(nullable = true)
    private int selectedItemNum; //selected item number to calculate budget

    @Column(nullable = true)
    private float answer; //item price for guessing mode

    @Column(nullable = true)
    private int leftRange; //item range for guessing mode

    @Column(nullable = true)
    private int rightRange; //item range for guessing mode

    @Column(nullable = true)
    private int originLeftRange; //item range for guessing mode before applying hint

    @Column(nullable = true)
    private int originRightRange; //item range for guessing mode before applying hint

    @Column(nullable = false)
    private boolean blur = false; //picture effect, default as false



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

    public String getSelectedItemList() {
        return selectedItemList;
    }
    public void setSelectedItemList(String selectedItemList) {
        this.selectedItemList =selectedItemList;
    }

    public int getSelectedItemNum(){return selectedItemNum;}

    public void setSelectedItemNum(int selectedItemNum){
        this.selectedItemNum = selectedItemNum;
    }

}