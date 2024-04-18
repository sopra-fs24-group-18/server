package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ANSWER")
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Float guessedPrice; // guessing mode

    @Column
    private String chosenItemList;  // budget mode

    @Column
    private Long point;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Float getGuessedPrice() {
        return guessedPrice;
    }

    public void setGuessedPrice(Float guessedPrice) {
        this.guessedPrice = guessedPrice;
    }

    public String getChosenItemList() {
        return chosenItemList;
    }

    public void setChosenItemList(String chosenItemList) {
        this.chosenItemList = chosenItemList;
    }
}