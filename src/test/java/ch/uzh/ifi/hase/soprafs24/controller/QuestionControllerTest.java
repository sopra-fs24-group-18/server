package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;



import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;


    @Test
    public void getReadyForGameSuccess() throws Exception {
        // Mocking room and user data
        Room room = new Room();
        room.setId(1L);
        room.setOwnerId(1L);
        room.setPlayerAmount(4L);
        room.setGameMode(GameMode.GUESSING);
        room.setPlayerIds("1,2,3,4");

        User user = new User();
        user.setId(1L);
        // Mocking service behavior
        given(questionService.getReady(Mockito.any(),Mockito.any())).willReturn( "The game is ready!");

        // Performing the request
        mockMvc.perform(post("/games/1/1/getReady")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());

        // Verifying that the service methods were called
        verify(questionService).getReady(1L, 1L);
    }

    @Test
    public void getReadyForGameInvalidRoomId() throws Exception {

        // Mocking service behavior
        // Mocking service behavior to simulate room not found
        given(questionService.getReady(any(Long.class), any(Long.class)))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "The room is not exist!"));

        // Performing the request
        mockMvc.perform(post("/games/1/1/getReady")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Verifying that the service methods were called
        verify(questionService).getReady(1L, 1L);
    }

    @Test
    public void getQuestionByUserIdSuccess() throws Exception {
        // Mocking room and user data
        Room room = new Room();
        Question question = new Question();
        question.setId(1L);
        question.setRoomId(1L);
        question.setGameMode(GameMode.GUESSING);
        question.setItemId(1L);
        question.setItemImage("imageurl");
        question.setRoundNumber(1);
        question.setLeftRange(0);
        question.setRightRange(1);

        // Mocking service behavior
        given(questionService.getQuestionsByRoomRoundandUserId(any(Long.class), anyInt(), any(Long.class))).willReturn(question);

        // Performing the request
        mockMvc.perform(get("/games/1/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(question.getId().intValue())))
                .andExpect(jsonPath("$.roomId", is(question.getRoomId().intValue())))
                .andExpect(jsonPath("$.gameMode", is(question.getGameMode().toString())))
                .andExpect(jsonPath("$.itemId", is(question.getItemId().intValue())))
                .andExpect(jsonPath("$.itemImage", is(question.getItemImage())))
                .andExpect(jsonPath("$.roundNumber", is(question.getRoundNumber())))
                .andExpect(jsonPath("$.leftRange", is(question.getLeftRange())))
                .andExpect(jsonPath("$.rightRange", is(question.getRightRange())));

        // Verifying that the service methods were called
        verify(questionService).getQuestionsByRoomRoundandUserId(1L, 1,1L);
    }

    @Test
    public void getQuestionByUserIdInvalidQuestion() throws Exception {
        // Mocking service behavior to simulate question not found
        given(questionService.getQuestionsByRoomRoundandUserId(any(Long.class), anyInt(), any(Long.class)))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        // Performing the request
        mockMvc.perform(get("/games/1/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                         .andExpect(status().isNotFound());


        // Verifying that the service methods were called
        verify(questionService).getQuestionsByRoomRoundandUserId(1L, 1,1L);
    }

}