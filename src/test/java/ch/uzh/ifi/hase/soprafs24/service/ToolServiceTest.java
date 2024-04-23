package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.Tool;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class ToolServiceTest {

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private RoomService roomService;

  private Tool testTool;
  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    Tool testTool = new Tool();
    testTool.setType(ToolType.HINT);
    testTool.setPrice(30L);
    testTool.setDescription("This is a hint tool!");

    testUser = new User();
    testUser.setId(1L);
    testUser.setPassword("123");
    testUser.setUsername("testUsername");
    testUser.setCreationDate(LocalDate.now());
    testUser.setBirthday(LocalDate.now());


  }

    @Test
    public void uesTool_validInputs_success() {
    }
}
