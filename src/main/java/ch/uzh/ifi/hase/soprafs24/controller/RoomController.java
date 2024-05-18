package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserPointsGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.RoomDTOMapper;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.UserDTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.RoomService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomController {

  private final RoomService roomService;
  private final UserService userService;

  RoomController(RoomService roomService, UserService userService) {
    this.roomService = roomService;
    this.userService = userService;
  }

  @PostMapping("/rooms")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public RoomGetDTO createRoom(@RequestBody RoomPostDTO roomPostDTO) {
    // convert API room to internal representation
    Room roomInput = RoomDTOMapper.INSTANCE.convertRoomPostDTOtoEntity(roomPostDTO);
    // create room
    Room createdRoom = roomService.createRoom(roomInput);
    // convert internal representation of room back to API
    RoomGetDTO roomGetDTO = RoomDTOMapper.INSTANCE.convertEntityToRoomGetDTO(createdRoom);

    roomGetDTO.setOwnerName(userService.userId2Username(createdRoom.getOwnerId()));
    roomGetDTO.setPlayerNames(userService.userIdList2UsernameList(createdRoom.getPlayerIds()));
    return roomGetDTO;
  }

  @PostMapping("/rooms/{roomCode}/{userId}/enter")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public RoomGetDTO enterRoom(@PathVariable String roomCode, @PathVariable Long userId) {
      Room room = roomService.enterRoom(roomCode, userId);

      // convert internal representation of user back to API
      RoomGetDTO roomGetDTO = RoomDTOMapper.INSTANCE.convertEntityToRoomGetDTO(room);

      roomGetDTO.setOwnerName(userService.userId2Username(room.getOwnerId()));
      roomGetDTO.setPlayerNames(userService.userIdList2UsernameList(room.getPlayerIds()));
      return roomGetDTO;
  }

    @PostMapping("/rooms/{roomId}/{userId}/exit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public ResponseEntity<?> exitRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        roomService.exitRoom(roomId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/rooms/{roomId}/rank")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<UserPointsGetDTO> retreiveRank(@PathVariable Long roomId) {
        List<User> users = roomService.calculateRank(roomId);
        List<UserPointsGetDTO> userPointsGetDTOS = new ArrayList<>();

        // convert each user to the API representation
        for (User user : users) {
            userPointsGetDTOS.add(UserDTOMapper.INSTANCE.convertEntityToUserPointsGetDTO(user));
        }
        return userPointsGetDTOS;
    }
}
