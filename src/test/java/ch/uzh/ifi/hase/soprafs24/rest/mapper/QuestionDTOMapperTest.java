package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.rest.dto.question.QuestionGetDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuestionDTOMapperTest {

    @Test
    public void testConvertEntitytoQuestionGetDTO() {
        // Create a sample Question entity
        Question question = new Question();
        question.setId(1L);
        question.setRoomId(123L);
        question.setGameMode(GameMode.GUESSING);
        question.setRoundNumber(1);
        question.setItemId(456L);
        question.setItemImage("image1.jpg");
        question.setItemList("item1,item2");
        question.setItemImageList("image1.jpg,image2.jpg");
        question.setLeftRange(0);
        question.setRightRange(100);

        // Convert entity to DTO using the mapper
        QuestionGetDTO questionGetDTO = QuestionDTOMapper.INSTANCE.convertEntitytoQuestionGetDTO(question);

        // Check if the conversion is successful
        assertEquals(question.getId(), questionGetDTO.getId());
        assertEquals(question.getRoomId(), questionGetDTO.getRoomId());
        assertEquals(question.getGameMode(), questionGetDTO.getGameMode());
        assertEquals(question.getRoundNumber(), questionGetDTO.getRoundNumber());
        assertEquals(question.getItemId(), questionGetDTO.getItemId());
        assertEquals(question.getItemImage(), questionGetDTO.getItemImage());
        assertEquals(question.getItemList(), questionGetDTO.getItemList());
        assertEquals(question.getItemImageList(), questionGetDTO.getItemImageList());
        assertEquals(question.getLeftRange(), questionGetDTO.getLeftRange());
        assertEquals(question.getRightRange(), questionGetDTO.getRightRange());
        assertEquals(question.getBlur(), questionGetDTO.getBlur());
    }

}