package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AnswerServiceTest {

  @Mock
  private AnswerRepository answerRepository;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private ItemRepository itemRepository;

  @InjectMocks
  @Spy
  private AnswerService answerService;


  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void saveAnswer_validInputs_success() {
    Answer testAnswer1 = new Answer();
    testAnswer1.setQuestionId(1L);
    testAnswer1.setUserId(1L);
    testAnswer1.setGuessedPrice(5.3F);
    answerService.saveAnswer(testAnswer1);

    Mockito.verify(answerRepository, times(1)).save(Mockito.any());
  }

  @Test
  public void calculatePoints_validInputs_success() {
      Question question = new Question();
      question.setId(1L);
      question.setRoomId(1L);
      question.setGameMode(GameMode.GUESSING);
      question.setAnswer(10.2F);
      Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(question));

      Room room = new Room();
      room.setPlayerIds("1,2");
      Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(room));

      List<Answer> answers = new ArrayList<>();
      Answer testAnswer1 = new Answer();
      testAnswer1.setQuestionId(1L);
      testAnswer1.setUserId(1L);
      testAnswer1.setGuessedPrice(5.3F);
      answers.add(testAnswer1);

      Answer testAnswer2 = new Answer();
      testAnswer2.setQuestionId(1L);
      testAnswer2.setUserId(2L);
      testAnswer2.setGuessedPrice(8.1F);
      answers.add(testAnswer2);

      Mockito.when(answerRepository.findByQuestionId(Mockito.anyLong())).thenReturn(answers);

      Mockito.doReturn(0L).when(answerService).updateUserScore(Mockito.anyLong(), Mockito.anyLong());

      List<Long> point1 = answerService.calculatePoints(testAnswer1);
      List<Long> point2 = answerService.calculatePoints(testAnswer2);
      assertEquals(70L, point1.get(0));
      assertEquals(100L, point2.get(0));
  }

    @Test
    public void calculatePoints_invalidQuestionId() {
      Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

      Answer testAnswer = new Answer();
      testAnswer.setQuestionId(1L);
      testAnswer.setUserId(2L);
      testAnswer.setGuessedPrice(8.1F);

      ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> answerService.calculatePoints(testAnswer));
      assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void calculatePoints_invalidRoomId() {
        Question question = new Question();
        question.setId(1L);
        question.setRoomId(1L);
        question.setGameMode(GameMode.GUESSING);
        question.setAnswer(10.2F);
        Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(question));

        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Answer testAnswer = new Answer();
        testAnswer.setQuestionId(1L);
        testAnswer.setUserId(2L);
        testAnswer.setGuessedPrice(8.1F);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> answerService.calculatePoints(testAnswer));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void calculatePoints_validInputs_budgetMode_success() {
        Question question = new Question();
        question.setId(1L);
        question.setRoomId(1L);
        question.setGameMode(GameMode.BUDGET);
        question.setBudget(120.2f);
        question.setItemList("1,2,3,4,5,6");
        Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(question));

        Room room = new Room();
        room.setPlayerIds("1,2");
        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(room));

        List<Answer> answers = new ArrayList<>();
        Answer testAnswer1 = new Answer();
        testAnswer1.setQuestionId(1L);
        testAnswer1.setUserId(1L);
        testAnswer1.setChosenItemList("1,2");
        answers.add(testAnswer1);

        Answer testAnswer2 = new Answer();
        testAnswer2.setQuestionId(1L);
        testAnswer2.setUserId(2L);
        testAnswer2.setChosenItemList("2,3,4");
        answers.add(testAnswer2);

        Mockito.when(answerRepository.findByQuestionId(Mockito.anyLong())).thenReturn(answers);
        Mockito.doReturn(80.1f).when(answerService).calculateTotalPrice(testAnswer1);
        Mockito.doReturn(120.1f).when(answerService).calculateTotalPrice(testAnswer2);

        Mockito.doReturn(0L).when(answerService).updateUserScore(Mockito.anyLong(), Mockito.anyLong());

        List<Long> point1 = answerService.calculatePoints(testAnswer1);
        List<Long> point2 = answerService.calculatePoints(testAnswer2);
        assertEquals(70L, point1.get(0));
        assertEquals(100L, point2.get(0));
    }

    @Test
    public void getRealPrice_validInputs_success() {
      Question question = new Question();
      question.setId(1L);
      question.setRoomId(1L);
      question.setGameMode(GameMode.GUESSING);
      question.setAnswer(10.2F);
      Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(question));

      Answer testAnswer1 = new Answer();
      testAnswer1.setQuestionId(1L);
      testAnswer1.setUserId(1L);
      testAnswer1.setGuessedPrice(5.3F);

      Float realPrice = answerService.getRealPrice(testAnswer1);
      assertEquals(10.2F, realPrice);
    }

    @Test
    public void getRealPrice_invalidQuestionId_success() {
        Mockito.when(questionRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Answer testAnswer1 = new Answer();
        testAnswer1.setQuestionId(1L);
        testAnswer1.setUserId(1L);
        testAnswer1.setGuessedPrice(5.3F);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> answerService.getRealPrice(testAnswer1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void calculateTotalPrice_validInputs_success() {
        Answer testAnswer1 = new Answer();
        testAnswer1.setQuestionId(1L);
        testAnswer1.setUserId(1L);
        testAnswer1.setChosenItemList("1");

        List<Item> items = new ArrayList<>();
        Item item1 = new Item();
        item1.setPrice(10.1F);
        items.add(item1);
        Mockito.when(itemRepository.findAllByIdIn(Mockito.anyList())).thenReturn(items);

        Float totalPrice = answerService.calculateTotalPrice(testAnswer1);
        assertEquals(10.1F, totalPrice);
    }

    @Test
    public void calculateTotalPrice_emptyChosenItemList_success() {
        Answer testAnswer1 = new Answer();
        testAnswer1.setQuestionId(1L);
        testAnswer1.setUserId(1L);
        testAnswer1.setChosenItemList("");

        Float totalPrice = answerService.calculateTotalPrice(testAnswer1);
        assertEquals(0.0F, totalPrice);
    }

    @Test
    public void updateUserScore_validInputs_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("123");
        user.setScore(0L);

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        Long bonus = answerService.updateUserScore(1L, 40L);
        Mockito.verify(userRepository, times(1)).save(Mockito.any());
        assertEquals(0L, bonus);
    }

    @Test
    public void updateUserScore_validInputs_useGamble1_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("123");
        user.setScore(100L);
        user.setToolStatus(ToolType.GAMBLE.name());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        Long bonus = answerService.updateUserScore(1L, 100L);
        Mockito.verify(userRepository, times(1)).save(Mockito.any());
        assertEquals(150L, bonus);
    }

    @Test
    public void updateUserScore_validInputs_useGamble2_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("123");
        user.setScore(100L);
        user.setToolStatus(ToolType.GAMBLE.name());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        Long bonus = answerService.updateUserScore(1L, 70L);
        Mockito.verify(userRepository, times(1)).save(Mockito.any());
        assertEquals(-100L, bonus);
    }

    @Test
    public void updateUserScore_validInputs_useBonus_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setPassword("123");
        user.setScore(100L);
        user.setToolStatus(ToolType.BONUS.name());

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        Long bonus = answerService.updateUserScore(1L, 100L);
        Mockito.verify(userRepository, times(1)).save(Mockito.any());
        assertEquals(60L, bonus);
    }

    @Test
    public void updateUserScore_invalidUserId() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> answerService.updateUserScore(1L, 50L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
