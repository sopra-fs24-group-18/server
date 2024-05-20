package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.Tool;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ToolRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ToolServiceTest {

  @Mock
  private ToolRepository toolRepository;
  @Mock
  private UserRepository userRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private UserService userService;

  @InjectMocks
  private ToolService toolService;

  private Tool testTool;
  private Tool testTool2;
  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testTool = new Tool();
    testTool.setId(1L);
    testTool.setType(ToolType.HINT);
    testTool.setPrice(30L);
    testTool.setDescription("This is a hint tool!");

    testTool2 = new Tool();
    testTool2.setId(2L);
    testTool2.setType(ToolType.BLUR);
    testTool2.setPrice(60L);
    testTool2.setDescription("This is a blur tool!");

    testUser = new User();
    testUser.setId(1L);
    testUser.setPassword("123");
    testUser.setUsername("testUsername");
    testUser.setCreationDate(LocalDate.now());
    testUser.setBirthday(LocalDate.now());
  }

    @Test
    public void uesTool_validInputs_buyHint_success() {
      testUser.setScore(100L);
      Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));
      Mockito.when(toolRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testTool));
      toolService.useTool(1L, 1L, 1L);

      assertEquals("HINT", testUser.getToolStatus());
      assertEquals("HINT", testUser.getToolList());
    }

    @Test
    public void uesTool_validInputs_buyBlur_success() {
        testUser.setScore(100L);
        testUser.setToolList("HINT");
        testUser.setToolStatus("HINT");

        User user2 = new User();
        user2.setId(2L);
        user2.setPassword("123");
        user2.setUsername("u2");

        Room room = new Room();
        room.setId(1L);
        room.setPlayerIds("1,2");

        Mockito.when(userService.getUserById(1L)).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(userService.getUserById(2L)).thenReturn(Optional.ofNullable(user2));
        Mockito.when(toolRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testTool2));
        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(room));

        toolService.useTool(2L, 1L, 1L);
        assertEquals("HINT", testUser.getToolStatus());
        assertEquals("HINT,BLUR", testUser.getToolList());
        assertEquals("BLUR", user2.getToolStatus());
    }

    @Test
    public void uesTool_invalidInputs_success() {
        testUser.setScore(10L);
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(toolRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testTool));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> toolService.useTool(1L, 1L, 1L));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    public void getUserTools_validInputs_success() {
        testUser.setToolList("HINT");
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));

        List<String> userTools = toolService.getUserTools(1L);

        assertEquals(Arrays.asList("HINT"), userTools);
    }

    @Test
    public void getUserTools_emptyToolList() {
        testUser.setToolList("");
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));

        List<String> userTools = toolService.getUserTools(1L);

        assertNull(userTools);
    }

    @Test
    public void getAllTools_success(){
        Mockito.when(toolRepository.findAll()).thenReturn(Collections.singletonList(testTool));

        List<Tool> tools = toolService.getTools();
        assertEquals(1, tools.size());
        assertEquals(testTool.getType() ,tools.get(0).getType());
    }
}
