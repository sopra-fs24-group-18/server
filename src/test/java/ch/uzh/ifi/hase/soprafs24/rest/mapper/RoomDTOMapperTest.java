package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomPostDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RoomDTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class RoomDTOMapperTest {
  @Test
  public void testCreateRoom_fromRoomPostDTO_toRoom_success() {
    // create RoomPostDTO
    RoomPostDTO roomPostDTO = new RoomPostDTO();
    roomPostDTO.setName("testRoom");
    roomPostDTO.setGameMode(GameMode.BUDGET);
    roomPostDTO.setOwnerId(4L);
    roomPostDTO.setPlayerAmount(5L);

    // MAP -> Create room
    Room room = RoomDTOMapper.INSTANCE.convertRoomPostDTOtoEntity(roomPostDTO);

    // check content
    assertEquals(roomPostDTO.getName(), room.getName());
    assertEquals(roomPostDTO.getOwnerId(), room.getOwnerId());
    assertEquals(roomPostDTO.getGameMode(), room.getGameMode());
    assertEquals(roomPostDTO.getPlayerAmount(), room.getPlayerAmount());
  }

  @Test
  public void testGetRoom_fromRoom_toRoomGetDTO_success() {
    // create Room
    Room room = new Room();
    room.setId(1L);
    room.setRoomCode("123456");
    room.setName("testRoom");
    room.setGameMode(GameMode.BUDGET);
    room.setPlayerAmount(5L);
    room.setOwnerId(4L);

    // MAP -> Create UserGetDTO
    RoomGetDTO roomGetDTO = RoomDTOMapper.INSTANCE.convertEntityToRoomGetDTO(room);

    // check content
    assertEquals(room.getId(), roomGetDTO.getId());
    assertEquals(room.getRoomCode(), roomGetDTO.getRoomCode());
    assertEquals(room.getName(), roomGetDTO.getName());
    assertEquals(room.getGameMode(), roomGetDTO.getGameMode());
    assertEquals(room.getPlayerAmount(),roomGetDTO.getPlayerAmount());
  }
}
