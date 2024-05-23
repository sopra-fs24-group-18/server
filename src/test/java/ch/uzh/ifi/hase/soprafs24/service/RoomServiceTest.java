package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.AnswerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

public class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private RoomService roomService;

    private Room testRoom;
    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testRoom = new Room();
        testRoom.setId(1L);
        testRoom.setRoomCode("123456");
        testRoom.setName("testRoom");
        testRoom.setGameMode(GameMode.BUDGET);
        testRoom.setPlayerAmount(5L);
        testRoom.setOwnerId(8L);
        testRoom.setRoundAmount(6L);
        testRoom.setPlayerIds("8");

        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("123");
        testUser.setUsername("testUsername");
        testUser.setCreationDate(LocalDate.now());
        testUser.setBirthday(LocalDate.now());

        // when -> any object is being save in the roomRepository -> return the dummy testRoom
        Mockito.when(roomRepository.save(Mockito.any())).thenReturn(testRoom);
    }

    @Test
    public void createRoom_validInputs_success() {
        // when -> any object is being save in the roomRepository -> return the dummy
        // testRoom
        Room createdRoom = roomService.createRoom(testRoom);

        // then
        Mockito.verify(roomRepository, times(1)).save(Mockito.any());

        assertEquals(testRoom.getId(), createdRoom.getId());
        assertEquals(testRoom.getName(), createdRoom.getName());
        assertEquals(testRoom.getOwnerId(), createdRoom.getOwnerId());
        assertNotNull(createdRoom.getRoomCode());
    }

    @Test
    public void enterRoom_validInputs_success() {
        Room createdRoom = roomService.createRoom(testRoom);

        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));
        Mockito.when(roomRepository.findByRoomCode(Mockito.anyString())).thenReturn(testRoom);
        Room room = roomService.enterRoom(createdRoom.getRoomCode(), testUser.getId());

        Mockito.verify(roomRepository, times(2)).findByRoomCode(Mockito.anyString());

        assertEquals("8,1", room.getPlayerIds());
    }

    @Test
    public void enterRoom_enterTwice_throwsException() {
        Room createdRoom = roomService.createRoom(testRoom);

        User user = new User();
        user.setId(8L);
        user.setPassword("123");
        user.setUsername("u1");
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));

        Mockito.when(roomRepository.findByRoomCode(Mockito.anyString())).thenReturn(testRoom);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> roomService.enterRoom(createdRoom.getRoomCode(), user.getId()));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
    }

    @Test
    public void enterRoom_fullRoom_throwsException() {
        Room room = new Room();
        room.setId(2L);
        room.setRoomCode("111111");
        room.setName("gameRoom");
        room.setGameMode(GameMode.BUDGET);
        room.setPlayerAmount(1L);
        room.setOwnerId(8L);
        room.setRoundAmount(6L);
        room.setPlayerIds("8");

        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testUser));

        Mockito.when(roomRepository.findByRoomCode(Mockito.anyString())).thenReturn(room);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> roomService.enterRoom(room.getRoomCode(), testUser.getId()));
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    public void enterRoom_invalidInputs_throwsException() {
        Mockito.when(roomRepository.findByRoomCode(Mockito.anyString())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> roomService.enterRoom("aaa", 1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void exitRoom_validInputs_changeRoomOwner_success() {
        Room room = new Room();
        room.setId(2L);
        room.setRoomCode("111111");
        room.setName("gameRoom");
        room.setGameMode(GameMode.BUDGET);
        room.setPlayerAmount(3L);
        room.setOwnerId(8L);
        room.setRoundAmount(6L);
        room.setPlayerIds("8,7,6");

        User user = new User();
        user.setId(8L);
        user.setPassword("123");
        user.setUsername("u1");

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(room));
        roomService.exitRoom(room.getId(), user.getId());

        Mockito.verify(roomRepository, times(1)).findById(Mockito.any());
        Mockito.verify(roomRepository, times(0)).deleteById(Mockito.any());
    }

    @Test
    public void exitRoom_invalidInputs_throwsException() {
        User user = new User();
        user.setId(8L);
        user.setPassword("123");
        user.setUsername("u1");

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> roomService.exitRoom(3L, 1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void calculateRank_validInputs() {
        Mockito.when(roomRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(testRoom));

        User user = new User();
        user.setId(8L);
        user.setPassword("123");
        user.setUsername("u1");
        user.setScore(100L);

        List<User> list = new ArrayList<>();
        list.add(user);

        Mockito.when(userRepository.findAllByIdInOrderByScoreDesc(Mockito.anyList())).thenReturn(list);

        List<User> users = roomService.calculateRank(testRoom.getId());
        Mockito.verify(userRepository, times(1)).findAllByIdInOrderByScoreDesc(Mockito.any());

        assertEquals(1, users.size());
        assertEquals(100L, users.get(0).getScore());
    }
}
