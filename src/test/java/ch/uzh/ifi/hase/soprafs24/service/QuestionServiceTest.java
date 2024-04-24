package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ToolService toolService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private QuestionService questionService;// to inject mock or spy dependencies into the fields of a tested object.
    private Room room;
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


        //Create mock item objects
        Item[] itemArray = new Item[27];

        String commonTitle = "item";
        String commonImageUrl = "image";
        int commonPrice = 100;

        // Create 9 items in a loop
        for (int i = 0; i < 27; i++) {

            Item item = new Item();
            item.setId((long) (i + 1));
            item.setItemTitle(commonTitle + (i + 1));
            item.setImageURL(commonImageUrl + (i + 1));
            item.setPrice(commonPrice);
            itemArray[i] = item;
        }
        items = Arrays.asList(itemArray);
        /*
        item1 = new Item();
        item1.setId(1L);
        item1.setItemTitle("item1");
        item1.setImageURL("image1");
        item1.setPrice(100);

        item2 = new Item();
        item2.setId(2L);
        item2.setItemTitle("item2");
        item2.setImageURL("image2");
        item2.setPrice(200);

        item3 = new Item();
        item3.setId(3L);
        item3.setItemTitle("item3");
        item3.setImageURL("image3");
        item3.setPrice(300);

*/

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


        // Create mock Item objects
       // itemsBudget = Arrays.asList(item1, new Item(), new Item(),new Item(), new Item(), new Item(),new Item(), new Item(), new Item());
        //itemsGuessing =  Arrays.asList(item1, item2, item3);

        // Stub mock method calls
        Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room);
        Mockito.when(roomService.getUsersByRoomId(Mockito.anyLong())).thenReturn(Arrays.asList(testUser, testUser2));
        Mockito.doNothing().when(roomService).resetPlayerScore(Mockito.anyLong());
    }


    @Test
    public void testCreateGuessingQuestions() {
        // Arrange
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        Mockito.when(questionRepository.save(Mockito.any())).thenReturn(new Question());

        // Act
        assertDoesNotThrow(() -> questionService.createGuessingQuestions(1L));

        // Assert
        Mockito.verify(itemRepository, times(1)).findAll();
        Mockito.verify(roomService, times(1)).findById(Mockito.anyLong());
        Mockito.verify(questionRepository, times(3)).save(Mockito.any());
    }

    @Test
    public void testCreateBudgetQuestions() {
        // Arrange
        Mockito.when(itemRepository.findAll()).thenReturn(items);
        Mockito.when(questionRepository.save(Mockito.any())).thenReturn(new Question());

        // Act
        assertDoesNotThrow(() -> questionService.createBudgetQuestions(1L));

        // Assert
        Mockito.verify(itemRepository, times(1)).findAll();
        Mockito.verify(roomService, times(1)).findById(Mockito.anyLong());
        Mockito.verify(questionRepository, times(3)).save(Mockito.any());
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
    public void testGetQuestionsByRoomRoundandUserId() {
        // Arrange
        Question question = new Question();
        question.setAnswer(100);
        Mockito.when(questionRepository.findAllByRoomIdAndRoundNumber(Mockito.anyLong(), Mockito.anyInt())).thenReturn(question);
        Mockito.when(toolService.getUserTools(Mockito.anyLong())).thenReturn(Arrays.asList("HINT"));

        // Act
        Question result = questionService.getQuestionsByRoomRoundandUserId(1L, 1, 1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.getLeftRange() >= 80); //0.8-1
        assertTrue(result.getRightRange() <= 120); //1-1.2
        assertFalse(result.getBlur());
    }

    @Test
    public void testGetReady() {
        // Arrange, room with only one person
        Room room1 = new Room();
        room1.setId(1L);
        room1.setRoundAmount(3L);
        room1.setOwnerId(1L);
        room1.setPlayerAmount(1L);
        room1.setPlayerIds("1");
        room1.setGameMode(GameMode.GUESSING);
        Mockito.when(roomService.findById(Mockito.anyLong())).thenReturn(room1);
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        // Act
        String result = questionService.getReady(1L, 1L);

        // Assert
        assertEquals("The game is ready!", result);
        Mockito.verify(roomService, times(2)).findById(Mockito.anyLong());
        Mockito.verify(roomService, times(1)).resetPlayerScore(Mockito.anyLong());
        Mockito.verify(questionRepository, times(3)).save(Mockito.any());//verify the question is created
    }
}