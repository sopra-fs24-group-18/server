package ch.uzh.ifi.hase.soprafs24.rest.dto.answer;

public class AnswerGetDTO {
    private Long point;

    private Float realPrice;

    private Long bonus;

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public Float getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(Float realPrice) {
        this.realPrice = realPrice;
    }

    public Long getBonus() {
        return bonus;
    }

    public void setBonus(Long bonus) {
        this.bonus = bonus;
    }
}
