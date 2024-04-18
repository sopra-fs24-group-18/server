package ch.uzh.ifi.hase.soprafs24.rest.dto.answer;

import javax.persistence.Column;

public class AnswerPostDTO {

    private Long questionId;

    private Long userId;

    private Float guessedPrice; // guessing mode

    private String chosenItemList;  // budget mode

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
