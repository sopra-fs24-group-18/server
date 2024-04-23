//package ch.uzh.ifi.hase.soprafs24.service;
//
//import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
//import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
//import ch.uzh.ifi.hase.soprafs24.entity.Answer;
//import ch.uzh.ifi.hase.soprafs24.entity.Question;
//import ch.uzh.ifi.hase.soprafs24.entity.Room;
//import ch.uzh.ifi.hase.soprafs24.entity.User;
//import ch.uzh.ifi.hase.soprafs24.repository.AnswerRepository;
//import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
//import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.times;
//
//public class AnswerServiceTest {
//
//  @Mock
//  private AnswerRepository answerRepository;
//
//  @Mock
//  private AnswerRepository questionRepository;
//
//  @Mock
//  private UserRepository userRepository;
//
//  @Mock
//  private RoomRepository roomRepository;
//
//  @InjectMocks
//  private AnswerService answerService;
//
//  private User testUser;
//  private Answer testAnswer1;
//  private Answer testAnswer2;
//
//  @BeforeEach
//  public void setup() {
//    MockitoAnnotations.openMocks(this);
//
//    // given
//    testUser = new User();
//    testUser.setId(1L);
//    testUser.setPassword("123");
//    testUser.setUsername("testUsername");
//    testUser.setCreationDate(LocalDate.now());
//    testUser.setBirthday(LocalDate.now());
//    testUser.setAvatar("111");
//
//    testAnswer1 = new Answer();
//    testAnswer1.setQuestionId(1L);
//    testAnswer1.setUserId(1L);
//    testAnswer1.setGuessedPrice(5.3F);
//
//    testAnswer2 = new Answer();
//    testAnswer2.setQuestionId(1L);
//    testAnswer2.setUserId(2L);
//    testAnswer2.setGuessedPrice(8.1F);
//
//  }
//
//  @Test
//  public void saveAnswer_validInputs_success() {
//    answerService.saveAnswer(testAnswer1);
//
//    Mockito.verify(answerRepository, times(1)).save(Mockito.any());
//  }
//
//  @Test
//  public void calculatePoints_validInputs_success() {
//      answerService.saveAnswer(testAnswer1);
//      answerService.saveAnswer(testAnswer2);
//
//      Mockito.verify(answerRepository, times(2)).save(Mockito.any());
//
//      List<Answer> answers = null;
//      answers.add(testAnswer1);
//      answers.add(testAnswer2);
//
//      Question question = new Question();
//
//      Room room = new Room();
//      room.setPlayerIds("1,2");
////      Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
//      Mockito.when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
//
//      Mockito.when(questionRepository.countByQuestionId(1L)).thenReturn(2L);
//
//
//  }
//}
