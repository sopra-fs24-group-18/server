package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuestionRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void findById_success() {
        // given
        Question question = new Question();
        question.setRoomId(1L);
        question.setGameMode(GameMode.GUESSING);
        question.setRoundNumber(1);
        question.setItemId(1L);
        question.setItemImage("image");
        question.setLeftRange(0);
        question.setRightRange(100);

        entityManager.persist(question);
        entityManager.flush();

        // when
        Optional<Question> found = questionRepository.findById(question.getId());

        // then
        assertNotNull(found);
        assertTrue(found.isPresent());
        assertEquals(question.getId(), found.get().getId());
        assertEquals(question.getRoomId(), found.get().getRoomId());
        assertEquals(question.getGameMode(), found.get().getGameMode());
        assertEquals(question.getRoundNumber(), found.get().getRoundNumber());
        assertEquals(question.getItemId(), found.get().getItemId());
        assertEquals(question.getItemImage(), found.get().getItemImage());
        assertEquals(question.getLeftRange(), found.get().getLeftRange());
        assertEquals(question.getRightRange(), found.get().getRightRange());
    }

    @Test
    public void findAllByRoomIdAndRoundNumber_success() {
        // given
        Question question = new Question();
        question.setRoomId(2L);
        question.setGameMode(GameMode.BUDGET);
        question.setRoundNumber(2);
        question.setItemId(1L);
        question.setItemImage("image");
        question.setLeftRange(0);
        question.setRightRange(100);

        entityManager.persist(question);
        entityManager.flush();

        // when
        Question found = questionRepository.findAllByRoomIdAndRoundNumber(question.getRoomId(), question.getRoundNumber());

        // then
        assertNotNull(found);
        assertEquals(question.getId(), found.getId());
        assertEquals(question.getRoomId(), found.getRoomId());
        assertEquals(question.getGameMode(), found.getGameMode());
        assertEquals(question.getRoundNumber(), found.getRoundNumber());
        assertEquals(question.getItemId(), found.getItemId());
        assertEquals(question.getItemImage(), found.getItemImage());
        assertEquals(question.getLeftRange(), found.getLeftRange());
        assertEquals(question.getRightRange(), found.getRightRange());
    }
    @Test
    public void findAllByRoomId_success() {
        Question question = new Question();
        question.setRoomId(1L);
        question.setGameMode(GameMode.BUDGET);
        question.setRoundNumber(2);
        question.setItemId(1L);
        question.setItemImage("image");
        question.setLeftRange(0);
        question.setRightRange(100);
        entityManager.persist(question);
        entityManager.flush();

        Question question1 = new Question();
        question1.setRoomId(1L);
        question1.setGameMode(GameMode.BUDGET);
        question1.setRoundNumber(2);
        question1.setItemId(1L);
        question1.setItemImage("image");
        question1.setLeftRange(0);
        question1.setRightRange(100);
        entityManager.persist(question1);
        entityManager.flush();


        // when
        List<Question> foundQuestions = questionRepository.findAllByRoomId(1L);

        // then
        assertEquals(2, foundQuestions.size()); // Check if two questions are found for room ID 1
    }
}