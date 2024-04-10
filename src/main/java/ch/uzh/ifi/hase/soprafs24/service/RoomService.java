package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomGetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

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

  @Autowired
  public RoomService(@Qualifier("roomRepository") RoomRepository roomRepository) {
    this.roomRepository = roomRepository;
  }

  public Room createRoom(Room newRoom) {
    newRoom.setRoomCode(UUID.randomUUID().toString());
    newRoom.setCurrentRound(0L);
    newRoom.setRoundAmount(6L);
    newRoom.setPlayerIds(newRoom.getOwnerId().toString());

    newRoom = roomRepository.save(newRoom);
    roomRepository.flush();

    log.debug("Created Information for Room: {}", newRoom);
    return newRoom;
  }
}
