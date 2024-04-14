package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
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

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class RoomService {

    private final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final UserService userService;

    @Autowired
    public RoomService(@Qualifier("roomRepository") RoomRepository roomRepository, UserService userService) {
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

    public Room createRoom(Room newRoom) {
        newRoom.setRoomCode(roomCodeGenerator(6));
        newRoom.setCurrentRound(0L);
        newRoom.setRoundAmount(6L);
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
                int randomNumber = random.nextInt(10); // 生成0到9之间的随机数
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
        if(ids.length >= room.getPlayerAmount()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, you cannot enter the room. The room is full!");
        }
        for (String id : ids) {
            Long trimmedId = Long.parseLong(id.trim());
            if (trimmedId.equals(userId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already entered the room!");
            }
        }
        playerIds = playerIds + "," + userId;
        room.setPlayerIds(playerIds);
        roomRepository.save(room);
        return room;
    }

    public void exitRoom(Long roomId, Long userId) {
        userService.getUserById(userId);
        Optional<Room> optinalRoom = roomRepository.findById(roomId);
        if (!optinalRoom.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The room is not exist!");
        }
        Room room = optinalRoom.get();

        String playerIds = room.getPlayerIds();
        String[] ids = playerIds.split(",");
        List<String> newPlayerList = new ArrayList<>();
        for (String id : ids) {
            if(userId.equals(Long.parseLong(id))){
                continue;
            }
            newPlayerList.add(id);
        }

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
}
