package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.RoomService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RoomService roomService;

  @MockBean
  private UserService userService;

  @Test
  public void createRoom_validInput_roomCreated() throws Exception {
    // given
    Room room = new Room();
    room.setId(1L);
    room.setRoomCode("123456");
    room.setName("testRoom");
    room.setGameMode(GameMode.BUDGET);
    room.setPlayerAmount(5L);
    room.setOwnerId(4L);

    RoomPostDTO roomPostDTO = new RoomPostDTO();
    roomPostDTO.setName("testRoom");
    roomPostDTO.setGameMode(GameMode.BUDGET);
    roomPostDTO.setOwnerId(4L);
    roomPostDTO.setPlayerAmount(5L);

    given(roomService.createRoom(Mockito.any())).willReturn(room);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/rooms")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(roomPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(room.getId().intValue())))
        .andExpect(jsonPath("$.playerAmount", is(room.getPlayerAmount().intValue())))
        .andExpect(jsonPath("$.roomCode", is(room.getRoomCode())))
        .andExpect(jsonPath("$.gameMode", is(room.getGameMode().toString())));
  }

  @Test
  public void enterRoom_validRoomCode() throws Exception {
    Room room = new Room();
    room.setId(1L);
    room.setRoomCode("123456");
    room.setName("testRoom");
    room.setGameMode(GameMode.BUDGET);
    room.setPlayerAmount(5L);
    room.setOwnerId(4L);

    given(roomService.enterRoom(Mockito.anyString(), Mockito.anyLong())).willReturn(room);

    // when
    MockHttpServletRequestBuilder postReqest = post("/rooms/123456/1/enter")
            .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(postReqest).andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(room.getName())))
            .andExpect(jsonPath("$.playerAmount", is(room.getPlayerAmount().intValue())))
            .andExpect(jsonPath("$.ownerName", nullValue()))
            .andExpect(jsonPath("$.gameMode", is(room.getGameMode().toString())));
  }

  @Test
  public void enterRoom_invalidRoomCode() throws Exception {
    given(roomService.enterRoom(Mockito.anyString(), Mockito.anyLong())).willThrow(
            new ResponseStatusException(HttpStatus.NOT_FOUND, "The room was not found!"));

    // when
    MockHttpServletRequestBuilder postReqest = post("/rooms/123456/1/enter")
            .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(postReqest)
            .andExpect(status().isNotFound())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResponseStatusException));
  }

  @Test
  public void exitRoom_validInputs() throws Exception {
    doNothing().when(roomService).exitRoom(Mockito.anyLong(), Mockito.anyLong());

    // when
    MockHttpServletRequestBuilder postReqest = post("/rooms/1/1/exit")
            .contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(postReqest).andExpect(status().isNoContent());
  }

  @Test
  public void exitRoom_invalidInputs() throws Exception {
      doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "The room is not exist!"))
              .when(roomService).exitRoom(Mockito.anyLong(), Mockito.anyLong());

      MockHttpServletRequestBuilder postReqest = post("/rooms/1/1/exit")
              .contentType(MediaType.APPLICATION_JSON);

      mockMvc.perform(postReqest).andExpect(status().isNotFound());
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