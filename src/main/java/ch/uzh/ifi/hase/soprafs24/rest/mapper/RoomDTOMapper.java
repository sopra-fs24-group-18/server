package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoomDTOMapper {

  RoomDTOMapper INSTANCE = Mappers.getMapper(RoomDTOMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "roomCode", ignore = true)
  @Mapping(target = "roundAmount", ignore = true)
  @Mapping(target = "playerIds", ignore = true)
  @Mapping(source = "name", target = "name")
  @Mapping(source = "ownerId", target = "ownerId")
  @Mapping(source = "gameMode", target = "gameMode")
  @Mapping(source = "playerAmount", target = "playerAmount")
  @Mapping(target = "readyIds", ignore = true)
  Room convertRoomPostDTOtoEntity(RoomPostDTO roomPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "roomCode", target = "roomCode")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "roundAmount", target = "roundAmount")
  @Mapping(source = "gameMode", target = "gameMode")
  @Mapping(source = "playerAmount", target = "playerAmount")
  @Mapping(source = "readyIds", target = "readyIds")
  @Mapping(target = "ownerName", ignore = true)
  @Mapping(target = "playerNames", ignore = true)
  RoomGetDTO convertEntityToRoomGetDTO(Room room);
}