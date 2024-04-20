package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

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
public interface UserDTOMapper {

  UserDTOMapper INSTANCE = Mappers.getMapper(UserDTOMapper.class);

  @Mapping(source = "password", target = "password")
  @Mapping(source = "username", target = "username")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "token", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "birthday", ignore = true)
  @Mapping(target = "score", ignore = true)
  @Mapping(target = "toolList", ignore = true)
  @Mapping(target = "avatar", ignore = true)
  @Mapping(target = "toolStatus", ignore = true)
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "avatar", target = "avatar")
  UserGetDTO convertEntityToUserGetDTO(User user);


  @Mapping(source = "username", target = "username")
  @Mapping(source = "score", target = "score")
  UserPointsGetDTO convertEntityToUserPointsGetDTO(User user);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "status", target = "status")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "avatar", target = "avatar")
  UserReducedGetDTO convertEntityToUserReducedGetDTO(User user);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(source = "avatar", target = "avatar")
  @Mapping(source = "password", target = "password")
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "score", ignore = true)
  @Mapping(target = "toolList", ignore = true)
  @Mapping(target = "toolStatus", ignore = true)
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

}
