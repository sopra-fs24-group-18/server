package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Answer;
import ch.uzh.ifi.hase.soprafs24.rest.dto.answer.AnswerPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AnswerDTOMapperTest {
  @Test
  public void testAnswerSubmission_fromAnswerPostDTO_toAnswer_success() {
    // create AnswerPostDTO
    AnswerPostDTO answerPostDTO = new AnswerPostDTO();
    answerPostDTO.setQuestionId(3L);
    answerPostDTO.setGuessedPrice(5.3F);
    answerPostDTO.setUserId(10L);

    // MAP -> Submit Answer
    Answer answer = AnswerDTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);

    // check content
    assertEquals(answerPostDTO.getGuessedPrice(), answer.getGuessedPrice());
    assertEquals(answerPostDTO.getUserId(), answer.getUserId());
    assertEquals(answerPostDTO.getQuestionId(), answer.getQuestionId());
  }
}
