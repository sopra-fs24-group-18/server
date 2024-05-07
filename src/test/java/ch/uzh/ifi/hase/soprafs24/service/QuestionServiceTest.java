package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ToolService toolService;

    @Mock
    private RoomService roomService;

    @Mock
    private UserService userService;



    @InjectMocks
    private QuestionService questionService;// to inject mock or spy dependencies into the fields of a tested object.
    private Room room;
    private Room roomOnePerson;
    private List<Item> items;

    private User testUser;

    private User testUser2;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);//initializes the mock objects annotated with @Mock and injects them into the test class

        // Create a mock Room object
        room = new Room();
        room.setId(1L);
        room.setOwnerId(1L);
        room.setPlayerAmount(2L);
        room.setPlayerIds("1,2");

        roomOnePerson = new Room();
        roomOnePerson.setId(2L);
        roomOnePerson.setOwnerId(1L);
        roomOnePerson.setPlayerAmount(1L);
        roomOnePerson.setPlayerIds("1");
        roomOnePerson.setGameMode(GameMode.GUESSING);


        //Create mock item objects
        Item[] itemArray = new Item[18];

        String commonTitle = "item";
        String commonImageUrl = "image";
        int commonPrice = 100;

        // 6 items per round, totally 3 rounds
        for (int i = 0; i < 18; i++) {

            Item item = new Item();
            item.setId((long) (i + 1));
            item.setItemTitle(commonTitle + (i + 1));
            item.setImageURL(commonImageUrl + (i + 1));
            item.setPrice(commonPrice);
            itemArray[i] = item;
        }
        items = Arrays.asList(itemArray);

        //mock player
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("123");
        testUser.setUsername("testUsername");
        testUser.setScore(0L);


        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setPassword("123");
        testUser2.setUsername("testUsername");
        testUser2.setScore(0L);

        // Stub mock method calls
        Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
        Mockito.when(roomService.getUsersByRoomId(Mockito.anyLong())).thenReturn(Arrays.asList(testUser, testUser2));
        Mockito.doNothing().when(roomService).resetPlayerScore(Mockito.anyLong());
    }


    @Test
    public void testCreateGuessingQuestionsSuccess() {
        // Arrange
        Mockito.when(itemRepository.findAll()).thenReturn(items);
        Mockito.when(questionRepository.save(Mockito.any())).thenReturn(new Question());

        // Act
        assertDoesNotThrow(() -> questionService.createGuessingQuestions(1L));

        // Assert
        Mockito.verify(itemRepository, times(1)).findAll();
        Mockito.verify(roomService, times(1)).findById(Mockito.anyLong());
        //Mockito.verify(questionRepository, times(3)).save(Mockito.any());

        // verify price range
        ArgumentCaptor<Question> argument = ArgumentCaptor.forClass(Question.class);
        Mockito.verify(questionRepository, times(3)).save(argument.capture());
        List<Question> savedQuestions = argument.getAllValues();
        for (int i = 0; i < 3; i++) {
            assertTrue(savedQuestions.get(i).getLeftRange() <= 80);
            assertTrue(savedQuestions.get(i).getLeftRange() >= 60);
            assertTrue(savedQuestions.get(i).getRightRange() <= 140);
            assertTrue(savedQuestions.get(i).getRightRange() >= 120);
        }
    }

    @Test
    public void testCreateGuessingQuestionsInValidItems() {
        // Arrange
        List<Item> shortItemList = items.subList(0, 2); //3 items are needed
        Mockito.when(itemRepository.findAll()).thenReturn(shortItemList);

        // Act and assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionService.createGuessingQuestions(1L);
        });

        // Assert
        String expectedMessage = "Not enough items in the database for all rounds";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testCreateBudgetQuestionsSuccess() {
        // Arrange
        Mockito.when(itemRepository.findAll()).thenReturn(items);
        Mockito.when(questionRepository.save(Mockito.any())).thenReturn(new Question());

        // Act
        assertDoesNotThrow(() -> questionService.createBudgetQuestions(1L));

        // Assert
        Mockito.verify(itemRepository, times(1)).findAll();
        Mockito.verify(roomService, times(1)).findById(Mockito.anyLong());

        // verify budget range
        ArgumentCaptor<Question> argument = ArgumentCaptor.forClass(Question.class);
        Mockito.verify(questionRepository, times(3)).save(argument.capture());
        List<Question> savedQuestions = argument.getAllValues();
        for (int i = 0; i < 3; i++) {
            assertTrue(savedQuestions.get(i).getBudget() <= 600);
            assertTrue(savedQuestions.get(i).getBudget() >= 100);
            assertNotNull(savedQuestions.get(i).getSelectedItemList());
            assertTrue(savedQuestions.get(i).getSelectedItemNum() >= 1); //at least one item should be selected to calculate budget
            assertTrue(savedQuestions.get(i).getSelectedItemNum() <= 6); //at most 6
        }

    }
    @Test
    public void testCreateBudgetQuestionsInValidItems() {
        // Arrange
        List<Item> shortItemList = items.subList(0, 2); //27 items are needed
        Mockito.when(itemRepository.findAll()).thenReturn(shortItemList);

        // Act and assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            questionService.createBudgetQuestions(1L);
        });

        // Assert
        String expectedMessage = "Not enough items in the database for all rounds";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testGetQuestionsByRoomRound() {
        // Arrange
        Question question = new Question();
        Mockito.when(questionRepository.findAllByRoomIdAndRoundNumber(Mockito.anyLong(), Mockito.anyInt())).thenReturn(question);

        // Act
        Question result = questionService.getQuestionsByRoomRound(1L, 1);

        // Assert
        assertNotNull(result);
        Mockito.verify(questionRepository, times(1)).findAllByRoomIdAndRoundNumber(Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    public void testGetQuestionsByRoomRoundandUserIdWithHint() {
        // Arrange
        Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(roomOnePerson);
        Mockito.when(roomService.getUsersByRoomId(Mockito.anyLong())).thenReturn(Arrays.asList(testUser));

        Question question = new Question();
        question.setAnswer(100);
        question.setSelectedItemNum(1);

        Mockito.when(questionRepository.findAllByRoomIdAndRoundNumber(Mockito.anyLong(), Mockito.anyInt())).thenReturn(question);
        Mockito.when(toolService.getUserTools(Mockito.anyLong())).thenReturn(Arrays.asList("HINT", "BLUR"));

        // Act
        Question result = questionService.getQuestionsByRoomRoundandUserId(1L, 1, 1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getLeftRange() >= 80); //after using hint tool, price range change to 0.8-1
        assertTrue(result.getRightRange() <= 120); //1-1.2
        assertEquals(1,result.getSelectedItemNum());
        assertFalse(result.getBlur());//player owns blur tool won't be infected
    }
    @Test
    public void testGetQuestionsByRoomRoundandUserIdWithBlur() {
        // Arrange
        Question question = new Question();
        question.setAnswer(100);
        question.setLeftRange(60);
        question.setRightRange(140);
        question.setSelectedItemNum(1);

        Mockito.when(questionRepository.findAllByRoomIdAndRoundNumber(Mockito.anyLong(), Mockito.anyInt())).thenReturn(question);
        Mockito.when(toolService.getUserTools(Mockito.anyLong())).thenReturn(Arrays.asList("BLUR"));

        // Act
        Question result = questionService.getQuestionsByRoomRoundandUserId(1L, 1, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(60,result.getLeftRange() ); //no hint tool, range shouldn't change
        assertEquals(140,result.getRightRange() );
        assertTrue(result.getBlur());//another player owns Blur
        assertNotEquals(1,result.getSelectedItemNum()); //without hint tool, this term should not be returned
    }
    @Test
    public void testGetQuestionsByRoomRoundandUserIdWithHintandBlur() {
        // Arrange
        Question question = new Question();
        question.setAnswer(100);

        Mockito.when(questionRepository.findAllByRoomIdAndRoundNumber(Mockito.anyLong(), Mockito.anyInt())).thenReturn(question);
        Mockito.when(toolService.getUserTools(Mockito.anyLong())).thenReturn(Arrays.asList("HINT", "BLUR"));

        // Act
        Question result = questionService.getQuestionsByRoomRoundandUserId(1L, 1, 1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getLeftRange() >= 80); //after using hint tool, price range change to 0.8-1
        assertTrue(result.getRightRange() <= 120); //1-1.2
        assertTrue(result.getBlur());//another player owns Blur
    }


    @Test
    public void testOwnerGetReadySuccess() {
        // Arrange, room with only one person
        Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(roomOnePerson);
        Mockito.when(itemRepository.findAll()).thenReturn(items);
        Mockito.when(roomRepository.save(Mockito.any())).thenReturn(roomOnePerson);
        Mockito.when(userService.getUserById(Mockito.any())).thenReturn(Optional.ofNullable(testUser));

        // Act
        String result = questionService.getReady(2L, 1L);

        // Assert
        assertEquals("ready", result);
        //System.out.println(roomOnePerson.getReadyIds());
        Mockito.verify(roomService, times(3)).findById(Mockito.anyLong());
        Mockito.verify(userService, times(1)).getUserById(Mockito.anyLong());
        Mockito.verify(roomRepository, times(1)).save(Mockito.any());
        Mockito.verify(questionRepository, times(3)).save(Mockito.any());//verify the question is created when Owner is ready
    }



}