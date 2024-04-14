package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserReducedGetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface RoomDTOMapper {

    RoomDTOMapper INSTANCE = Mappers.getMapper(RoomDTOMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roomCode", ignore = true)
  @Mapping(target = "roundAmount", ignore = true)
  @Mapping(target = "playerIds", ignore = true)
  @Mapping(target = "currentRound", ignore = true)
  @Mapping(source = "name", target = "name")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "gameMode", target = "gameMode")
  @Mapping(source = "playerAmount", target = "playerAmount")
  Room convertRoomPostDTOtoEntity(RoomPostDTO roomPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "roomCode", target = "roomCode")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "roundAmount", target = "roundAmount")
  @Mapping(source = "gameMode", target = "gameMode")
  @Mapping(source = "playerAmount", target = "playerAmount")
  @Mapping(target = "ownerName", ignore = true)
  @Mapping(target = "playerNames", ignore = true)
  RoomGetDTO convertEntityToRoomGetDTO(Room room);
}