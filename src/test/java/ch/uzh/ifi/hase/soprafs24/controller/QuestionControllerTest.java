/*package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.rest.dto.question.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.QuestionDTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuestionController.class)
public class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @Test
    public void getQuestion_ValidInput_ReturnQuestion() throws Exception {
        // given
        Question question = new Question();
        question.setId(1L);
        question.setRoomId(123L);
        question.setGameMode(GameMode.GUESSING);
        question.setRoundNumber(1);
        question.setItemId(456L);
        question.setItemImage("image1.jpg");
        question.setAnswer(100f);

        given(questionService.getQuestionsByRoomRound(anyLong(), anyInt())).willReturn(question);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/123/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(question.getId().intValue()))
                .andExpect(jsonPath("$.roomId").value(question.getRoomId().intValue()))
                .andExpect(jsonPath("$.gameMode").value(question.getGameMode().toString()))
                .andExpect(jsonPath("$.roundNumber").value(question.getRoundNumber()))
                .andExpect(jsonPath("$.itemId").value(question.getItemId().intValue()))
                .andExpect(jsonPath("$.itemImage").value(question.getItemImage()))
                .andExpect(jsonPath("$.answer").value(question.getAnswer()));
    }

    @Test
    public void getQuestion_QuestionNotFound_ReturnNotFound() throws Exception {
        // given
        given(questionService.getQuestionsByRoomRound(anyLong(), anyInt())).willReturn(null);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/123/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getQuestion_InternalServerError_ReturnInternalServerError() throws Exception {
        // given
        given(questionService.getQuestionsByRoomRound(anyLong(), anyInt())).willThrow(RuntimeException.class);

        // when
        MockHttpServletRequestBuilder getRequest = get("/games/123/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isInternalServerError());
    }
    @Test
    public void startGuessingGame_Success() throws Exception {
        // given
        Long roomId = 123L;
        //here doesn't return anything, so cannot use given
        Mockito.doNothing().when(questionService).createGuessingQuestions(roomId);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/" + roomId + "/guessMode/start")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void startGuessingGame_Failure() throws Exception {
        // given
        Long roomId = 123L;
        Mockito.doThrow(new RuntimeException("Some error")).when(questionService).createGuessingQuestions(roomId);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/" + roomId + "/guessMode/start")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game could not start or questions could not be generated: Some error"));
    }

    @Test
    public void startBudgetGame_Success() throws Exception {
        // given
        Long roomId = 123L;
        Mockito.doNothing().when(questionService).createBudgetQuestions(roomId);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/" + roomId + "/budgetMode/start")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void startBudgetGame_Failure() throws Exception {
        // given
        Long roomId = 123L;
        Mockito.doThrow(new RuntimeException("Some error")).when(questionService).createBudgetQuestions(roomId);

        // when
        MockHttpServletRequestBuilder postRequest = post("/games/" + roomId + "/budgetMode/start")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Game could not start or questions could not be generated: Some error"));
    }
}*/