package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ANSWER")
@SequenceGenerator(name = "ans_seq", sequenceName = "ans_sequence", allocationSize = 1)
public class Answer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ans_seq")
    private Long id;

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Float guessedPrice; // guessing mode

    @Column
    private String chosenItemList;  // budget mode

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