package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.rest.dto.answer.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.AnswerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CountDownLatch;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnswerController.class)
public class AnswerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AnswerService answerService;

  @Test
  public void calculatePointGuessingMode_success() throws Exception {
    AnswerPostDTO answerPostDTO = new AnswerPostDTO();
    answerPostDTO.setGuessedPrice(8.8F);
    answerPostDTO.setUserId(1L);
    answerPostDTO.setQuestionId(1L);

    given(answerService.calculatePoints(Mockito.any())).willReturn(100L);

    MockHttpServletRequestBuilder postRequest = post("/answers/guessMode")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(answerPostDTO));
    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.point", is(100)));
  }

    @Test
    public void testUser1SubmitAnswer1ThenUser2SubmitAnswer2AndGetResult() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);

        // user1 submit answer
        Thread user1Thread = new Thread(() -> {
            try {
                AnswerPostDTO answerPostDTO = new AnswerPostDTO();
                answerPostDTO.setGuessedPrice(8.8F);
                answerPostDTO.setUserId(1L);
                answerPostDTO.setQuestionId(1L);

                MockHttpServletRequestBuilder postRequest = post("/answers/guessMode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerPostDTO));
                mockMvc.perform(postRequest)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.point", is(100)));

                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 用户2提交答案的线程
        Thread user2Thread = new Thread(() -> {
            try {
                // 用户2提交答案2的逻辑
                AnswerPostDTO answerPostDTO = new AnswerPostDTO();
                answerPostDTO.setGuessedPrice(10.0F);
                answerPostDTO.setUserId(2L);
                answerPostDTO.setQuestionId(1L);

                // 模拟用户2提交答案2
                given(answerService.calculatePoints(Mockito.any())).willReturn(100L);

                MockHttpServletRequestBuilder postRequest = post("/answers/guessMode")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(answerPostDTO));
                mockMvc.perform(postRequest)
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.point", is(100)));

                // 用户2执行完毕，倒计数减1
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        user1Thread.start();
        user2Thread.start();

        latch.await();
    }

    @Test
    public void calculatePointGuessingMode_invalidUserId_throwsException() throws Exception {
        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setGuessedPrice(8.8F);
        answerPostDTO.setUserId(1L);
        answerPostDTO.setQuestionId(1L);

        given(answerService.calculatePoints(Mockito.any())).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "The user was not found!"));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/answers/guessMode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    @Test
    public void calculatePointBudgetMode_success() throws Exception {
        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setChosenItemList("121,45,23");
        answerPostDTO.setUserId(1L);
        answerPostDTO.setQuestionId(1L);

        given(answerService.calculatePoints(Mockito.any())).willReturn(100L);

        MockHttpServletRequestBuilder postRequest = post("/answers/budgetMode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));
        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point", is(100)));
    }

    @Test
    public void calculatePointBudgetMode_invalidUserId_throwsException() throws Exception {
        AnswerPostDTO answerPostDTO = new AnswerPostDTO();
        answerPostDTO.setChosenItemList("121,45,23");
        answerPostDTO.setUserId(1L);
        answerPostDTO.setQuestionId(1L);

        given(answerService.calculatePoints(Mockito.any())).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "The user was not found!"));


        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/answers/budgetMode")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(answerPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
    }

    private String asJsonString(final Object object) {
    try {
        return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format("The request body could not be created.%s", e.toString()));
    }
  }
}