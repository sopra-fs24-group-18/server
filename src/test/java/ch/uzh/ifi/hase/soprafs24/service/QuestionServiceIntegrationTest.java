package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuestionServiceIntegrationTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        questionRepository.deleteAll();
        roomRepository.deleteAll();
    }
    @Test
    public void testGetQuestionsByRoomRoundandUserId() {
        // Create a mock Room object
        Room roomOnePerson = new Room();
        roomOnePerson.setName("testRoom");
        roomOnePerson.setId(2L);
        roomOnePerson.setOwnerId(1L);
        roomOnePerson.setRoundAmount(3L);
        roomOnePerson.setPlayerAmount(1L);
        roomOnePerson.setGameMode(GameMode.GUESSING);
        Room testroom = roomService.createRoom(roomOnePerson);
        // Arrange
        Question question = new Question();
        question.setRoomId(2L);
        question.setAnswer(100);
        question.setRoundNumber(1);
        question.setLeftRange(60);
        question.setRightRange(140);
        question.setGameMode(GameMode.GUESSING);
        questionRepository.save(question);
        //Create mock player
        User testUser = new User();
        testUser.setId(2L);
        testUser.setPassword("123");
        testUser.setUsername("testUsername");
        testUser.setToolList("HINT,BLUR");
        User createdUser = userService.createUser(testUser);
        // Act
        Question gettedquestion = questionService.getQuestionsByRoomRoundandUserId(testroom.getId(), 1,createdUser.getId());
        // Assert
        assertNotNull(gettedquestion);
        assertTrue(gettedquestion.getLeftRange() >= 80); //after using hint tool, price range change to 0.8-1
        assertTrue(gettedquestion.getRightRange() <= 120); //1-1.2
        assertFalse(gettedquestion.getBlur());//player owns blur tool won't be infected
    }

    @Test
    public void testGetReady() {
        // Arrange
        // Create a mock Room object
        Room roomOnePerson = new Room();
        roomOnePerson.setId(1L);
        roomOnePerson.setName("testRoom");
        roomOnePerson.setOwnerId(1L);
        roomOnePerson.setRoundAmount(3L);
        roomOnePerson.setRoomCode("123456");
        roomOnePerson.setPlayerAmount(1L);
        roomOnePerson.setPlayerIds("1");
        roomOnePerson.setGameMode(GameMode.GUESSING);
        roomRepository.save(roomOnePerson);

        //Create mock player
        User testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("123");
        testUser.setUsername("testUsername");
        testUser.setScore(0L);
        testUser.setToken("1");
        testUser.setStatus(UserStatus.ONLINE);
        testUser.setToolList("HINT,BLUR");
        userRepository.save(testUser);
        //Create mock item objects
        String commonTitle = "item";
        String commonImageUrl = "image";
        int commonPrice = 100;
        for (int i = 0; i < 9; i++) {
            Item item = new Item();
            item.setId((long) (i + 1));
            item.setItemTitle(commonTitle + (i + 1));
            item.setImageURL(commonImageUrl + (i + 1));
            item.setPrice(commonPrice);
            itemRepository.save(item);
        }
        // Act
        String result = questionService.getReady(1L, 1L);
        Room room = roomService.findById(1L);
        List<Question> questions = questionRepository.findAllByRoomId(1L);

        // Assert
        assertEquals("The game is ready!", result);
        assertEquals(3, questions.size()); //questions are created
        assertNotNull(room.getReadyIds()); //id is added

    }
}
