package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Answer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AnswerRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AnswerRepository answerRepository;

  @Test
  public void countByQuestionId_success() {
    // given
    Answer answer1 = new Answer();
    answer1.setQuestionId(1L);
    answer1.setUserId(1L);
    answer1.setGuessedPrice(8F);

    Answer answer2 = new Answer();
    answer2.setQuestionId(1L);
    answer2.setUserId(2L);
    answer2.setGuessedPrice(9F);

    Answer answer3 = new Answer();
    answer3.setQuestionId(1L);
    answer3.setUserId(3L);
    answer3.setGuessedPrice(10F);

    entityManager.persist(answer1);
    entityManager.persist(answer2);
    entityManager.persist(answer3);
    entityManager.flush();

    Long amount = answerRepository.countByQuestionId(1L);

    assertEquals(3L, amount);
  }

    @Test
    public void findByQuestionId_success() {
        // given
        Answer answer1 = new Answer();
        answer1.setQuestionId(1L);
        answer1.setUserId(1L);
        answer1.setGuessedPrice(8F);

        Answer answer2 = new Answer();
        answer2.setQuestionId(1L);
        answer2.setUserId(2L);
        answer2.setGuessedPrice(9F);

        Answer answer3 = new Answer();
        answer3.setQuestionId(1L);
        answer3.setUserId(3L);
        answer3.setGuessedPrice(10F);

        entityManager.persist(answer1);
        entityManager.persist(answer2);
        entityManager.persist(answer3);
        entityManager.flush();

        List<Answer> answers = answerRepository.findByQuestionId(1L);

        assertEquals(3, answers.size());
//        assertEquals(1L, answers.get(0).getUserId());
    }
}

