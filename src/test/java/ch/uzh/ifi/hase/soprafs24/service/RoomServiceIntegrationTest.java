package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the RoomResource REST resource.
 *
 * @see RoomService
 */
@WebAppConfiguration
@SpringBootTest
public class RoomServiceIntegrationTest {

  @Qualifier("roomRepository")
  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private RoomService roomService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setup() {
    roomRepository.deleteAll();
  }

  @Test
  public void createRoom_validInputs_success() {
    Room testRoom = new Room();
    testRoom.setName("testRoom");
    testRoom.setGameMode(GameMode.BUDGET);
    testRoom.setPlayerAmount(5L);
    testRoom.setOwnerId(4L);
    testRoom.setRoundAmount(6L);
    testRoom.setPlayerIds("4,1");

    // when
    Room createdRoom = roomService.createRoom(testRoom);

    // then
    assertEquals(testRoom.getName(), createdRoom.getName());
    assertEquals(testRoom.getOwnerId(), createdRoom.getOwnerId());
    assertNotNull(testRoom.getId());
    assertNotNull(testRoom.getRoomCode());
  }

    @Test
    public void enterRoom_validInputs_success() {
      Room testRoom = new Room();
      testRoom.setName("testRoom");
      testRoom.setGameMode(GameMode.BUDGET);
      testRoom.setPlayerAmount(5L);
      testRoom.setOwnerId(8L);
      testRoom.setRoundAmount(6L);
      testRoom.setPlayerIds("8");

      Room createdRoom = roomService.createRoom(testRoom);

      User testUser = new User();
      testUser.setPassword("testPassword");
      testUser.setUsername("testUsername");
      User createdUser = userService.createUser(testUser);

      Room resultRoom = roomService.enterRoom(createdRoom.getRoomCode(), createdUser.getId());

      assertEquals(resultRoom.getPlayerAmount(), createdRoom.getPlayerAmount());
      assertEquals(resultRoom.getGameMode(), createdRoom.getGameMode());
      assertEquals(resultRoom.getName(), createdRoom.getName());
      assertNotNull(resultRoom.getRoomCode());
    }

    @Test
    public void enterRoom_invalidInputs_throwsException() {
        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> roomService.enterRoom("aaa", 1L));
    }

    @Test
    public void exitRoom_validInputs_success() {
        Room testRoom = new Room();
        testRoom.setName("testRoom");
        testRoom.setGameMode(GameMode.BUDGET);
        testRoom.setPlayerAmount(5L);
        testRoom.setOwnerId(8L);
        testRoom.setRoundAmount(6L);
        testRoom.setPlayerIds("8");

        Room createdRoom = roomService.createRoom(testRoom);

        User testUser = new User();
        testUser.setPassword("123");
        testUser.setUsername("may");
        User createdUser = userService.createUser(testUser);

        Room resultRoom = roomService.enterRoom(createdRoom.getRoomCode(), createdUser.getId());
        assertEquals("8," + createdUser.getId(), resultRoom.getPlayerIds());

        roomService.exitRoom(resultRoom.getId(), createdUser.getId());
        resultRoom = roomRepository.findByRoomCode(createdRoom.getRoomCode());
        assertEquals("8", resultRoom.getPlayerIds());
    }

    @Test
    public void exitRoom_invalidInputs_throwsException() {
        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> roomService.exitRoom(10L, 1L));
    }

//    @Test
//    public void calculateRank_success(){
//        User user1 = new User();
//        user1.setUsername("u1");
//        user1.setPassword("123");
//        user1.setScore(100L);
//        userService.createUser(user1);
//
//        User user2 = new User();
//        user2.setUsername("u2");
//        user2.setPassword("123");
//        user2.setScore(150L);
//        userService.createUser(user2);
//
//        Room testRoom = new Room();
//        testRoom.setName("testRoom");
//        testRoom.setGameMode(GameMode.GUESSING);
//        testRoom.setPlayerAmount(2L);
//        testRoom.setOwnerId(user1.getId());
//        testRoom.setRoundAmount(3L);
//        testRoom.setPlayerIds(user1.getId().toString() + "," + user2.getId());
//
//        Room createdRoom = roomService.createRoom(testRoom);
//        List<User> users = roomService.calculateRank(createdRoom.getId());
//
//        assertEquals(2, users.size());
//    }
}
