package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.AnswerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class AnswerServiceIntegrationTest {

  @Qualifier("answerRepository")
  @Autowired
  private AnswerRepository answerRepository;

  @Autowired
  private QuestionRepository questionRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setup() {
    answerRepository.deleteAll();
  }

  @Test
  public void calculatePoints_validInputs_success() {
    Room testRoom = new Room();
    testRoom.setName("testRoom");
    testRoom.setGameMode(GameMode.GUESSING);
    testRoom.setPlayerAmount(2L);
    testRoom.setOwnerId(1L);
    testRoom.setRoundAmount(3L);
    testRoom.setPlayerIds("1,2");

    Question testQuestion = new Question();
    testQuestion.setId(1L);
    testQuestion.setRoomId(1L);
    testQuestion.setGameMode(GameMode.GUESSING);
    testQuestion.setRoundNumber(1);
  }

}
