package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.AnswerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoomService {

    private final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public RoomService(@Qualifier("roomRepository") RoomRepository roomRepository, UserService userService, UserRepository userRepository, AnswerRepository answerRepository) {
        this.roomRepository = roomRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
    }

    public Room createRoom(Room newRoom) {
        newRoom.setRoomCode(roomCodeGenerator(6));
        newRoom.setRoundAmount(3L);
        newRoom.setPlayerIds(newRoom.getOwnerId().toString());

        newRoom = roomRepository.save(newRoom);
        roomRepository.flush();

        log.debug("Created Information for Room: {}", newRoom);
        return newRoom;
    }

    private String roomCodeGenerator(int length) {
        Random random = new Random();
        Room room = null;
        String roomCode = null;
        do{
            StringBuilder sb = new StringBuilder(length);
            for (int i = 0; i < length; i++) {
                int randomNumber = random.nextInt(10); // generate random integer between 0 and 9
                sb.append(randomNumber);
            }
            roomCode = sb.toString();
            room = roomRepository.findByRoomCode(roomCode);
        }while(room != null);
        return roomCode;
    }

    public Room enterRoom(String roomCode, Long userId) {
        userService.getUserById(userId);
        Room room = roomRepository.findByRoomCode(roomCode);
        if (room == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The roomCode is invalid!");
        }

        String playerIds = room.getPlayerIds();
        String[] ids = playerIds.split(",");

        for (String id : ids) {
            Long trimmedId = Long.parseLong(id.trim());
            if (trimmedId.equals(userId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already entered the room!");
            }
        }

        if(ids.length >= room.getPlayerAmount()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, you cannot enter the room. The room is full!");
        }

        playerIds = playerIds + "," + userId;
        room.setPlayerIds(playerIds);
        roomRepository.save(room);
        return room;
    }

    public void exitRoom(Long roomId, Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);
        Room room = findById(roomId);

        String playerIds = room.getPlayerIds();
        String[] ids = playerIds.split(",");
        List<String> newPlayerList = new ArrayList<>();
        for (String id : ids) {
            if(userId.equals(Long.parseLong(id))){
                continue;
            }
            newPlayerList.add(id);
        }
        User user = optionalUser.get();
        user.setScore(100L);
        userRepository.save(user);
        answerRepository.deleteByUserId(userId);

        if (userId.equals(room.getOwnerId())) {
            if (!newPlayerList.isEmpty()) {
                Long newOwnerId = Long.parseLong(newPlayerList.get(0));
                room.setOwnerId(newOwnerId);
            }
            else {
                // if the room is empty, delete the room
                roomRepository.deleteById(roomId);
                return;
            }
        }
        room.setPlayerIds(String.join(",", newPlayerList));
        roomRepository.save(room);
    }

    public List<User> calculateRank(Long roomId) {
        Room room = findById(roomId);
        String[] playerIds = room.getPlayerIds().split(",");
        List<Long> playerIdList = Arrays.stream(playerIds)
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<User> users = userRepository.findAllByIdInOrderByScoreDesc(playerIdList);
        return users;
    }

    public Room findById(Long roomId){
        Optional<Room> optinalRoom = roomRepository.findById(roomId);
        if (!optinalRoom.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The room is not exist!");
        }
        return optinalRoom.get();
    }

    //retrive all users in the room
    public List<User> getUsersByRoomId(Long roomId) {
        // Retrieve the room entity based on roomId
        Room room = findById(roomId);

        // Extract player IDs from the room entity
        String playerIdsString = room.getPlayerIds();
        String[] playerIds = playerIdsString.split(",");
        // Convert player IDs to Long
        List<Long> playerIdsList = Arrays.stream(playerIds)
                .map(Long::valueOf)
                .collect(Collectors.toList());
        // Query UserRepository to fetch users with player IDs
        return userRepository.findAllById(playerIdsList);
    }

    public void resetPlayerScore(Long roomId) {
        // Retrieve the list of users in the current room
        List<User> userList = getUsersByRoomId(roomId);
        // Call UserService to reset their scores to 100
        userService.resetScore(userList);
    }
}
