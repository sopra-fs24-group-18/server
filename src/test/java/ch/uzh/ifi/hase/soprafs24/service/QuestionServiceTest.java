package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private QuestionService questionService;

    private Question testQuestion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        List<Item> items = new ArrayList<>();

        for (int i = 0; i < 27; i++) {
            Item item = new Item();
            item.setId((long) (i + 1)); // Assuming item IDs start from 1
            item.setPrice((float) ((i + 1) * 10)); // Assuming prices increase by 10
            item.setImageURL("image" + (i + 1) + ".jpg");
            items.add(item);
        }

        when(itemRepository.findAll()).thenReturn(items);

        // Create a dummy question
        testQuestion = new Question();
        testQuestion.setId(1L);
        testQuestion.setRoomId(1L);
        testQuestion.setGameMode(GameMode.GUESSING);
        testQuestion.setRoundNumber(1);
    }

    @Test
    public void createGuessingQuestions_success() {

        // Testing the method
        questionService.createGuessingQuestions(1L);

        // Verifying that the save method of questionRepository is called 3 times
        verify(questionRepository, times(3)).save(any(Question.class));
    }

    @Test
    public void createBudgetQuestions_success() {

        // Testing the method
        questionService.createBudgetQuestions(1L);

        // Verifying that the save method of questionRepository is called 3 times
        verify(questionRepository, times(3)).save(any(Question.class));
    }

    @Test
    public void getQuestionsByRoomRound_success() {
        // Mocking the find method of questionRepository
        when(questionRepository.findAllByRoomIdAndRoundNumber(1L, 1)).thenReturn(testQuestion);

        // Testing the method
        Question result = questionService.getQuestionsByRoomRound(1L, 1);

        // Verifying the result
        assertEquals(testQuestion, result);
    }
}