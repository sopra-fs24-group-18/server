package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserPutDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UserDTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class UserDTOMapperTest {
  @Test
  public void testCreateUser_fromUserPostDTO_toUser_success() {
    // create UserPostDTO
    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setPassword("name");
    userPostDTO.setUsername("username");

    // MAP -> Create user
    User user = UserDTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check content
    assertEquals(userPostDTO.getPassword(), user.getPassword());
    assertEquals(userPostDTO.getUsername(), user.getUsername());
  }

  @Test
  public void testGetUser_fromUser_toUserGetDTO_success() {
    // create User
    User user = new User();
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setAvatar("111");
    user.setBirthday(LocalDate.now());
    user.setCreationDate(LocalDate.now());

    // MAP -> Create UserGetDTO
    UserGetDTO userGetDTO = UserDTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

    // check content
    assertEquals(user.getId(), userGetDTO.getId());
    assertEquals(user.getUsername(), userGetDTO.getUsername());
    assertEquals(user.getStatus(), userGetDTO.getStatus());
    assertEquals(user.getAvatar(), userGetDTO.getAvatar());
    assertEquals(user.getBirthday(),userGetDTO.getBirthday());
    assertEquals(user.getCreationDate(),userGetDTO.getCreationDate());
  }
  @Test
  public void testUpdateUser_fromUserPutDTO_toUser_success() {
        // Create UserPutDTO
        UserPutDTO userPutDTO = new UserPutDTO();
        userPutDTO.setUsername("new_username");
        userPutDTO.setToken("new_token");
        userPutDTO.setPassword("new_password");

        // MAP -> Update user
        User user = UserDTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        // Check content
        assertEquals(userPutDTO.getUsername(), user.getUsername());
        assertEquals(userPutDTO.getToken(), user.getToken());
        assertEquals(userPutDTO.getPassword(), user.getPassword());
    }

//  @Test
//  public void testGetUserById_fromUser_toUserReducedGetDTO_success(){
//      // create User
//      User user = new User();
//      user.setUsername("firstname@lastname");
//      user.setStatus(UserStatus.OFFLINE);
//      user.setToken("1");
//      user.setCreationDate(LocalDate.now());
//
//      // MAP -> Create UserReducedGetDTO
//      UserReducedGetDTO userReducedGetDTO = DTOMapper.INSTANCE.convertEntityToUserReducedGetDTO(user);
//
//      // check content
//      assertEquals(user.getId(), userReducedGetDTO.getId());
//      assertEquals(user.getUsername(), userReducedGetDTO.getUsername());
//      assertEquals(user.getStatus(), userReducedGetDTO.getStatus());
//  }

//  @Test
//  public void testUpdateUser_fromUserPutDTO_toUser_success(){
//      // create UserPostDTO
//      UserPutDTO userPutDTO = new UserPutDTO();
//      userPutDTO.setUsername("username");
//      userPutDTO.setToken("1");
//
//      // MAP -> Update user
//      User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
//
//      // check content
//      assertEquals(userPutDTO.getUsername(), user.getUsername());
//      assertEquals(userPutDTO.getToken(), user.getToken());
//  }
}
