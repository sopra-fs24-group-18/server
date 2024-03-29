package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@SpringBootTest
public class UserServiceIntegrationTest {

  @Qualifier("userRepository")
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();
  }

  @Test
  public void createUser_validInputs_success() {
    // given
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setPassword("123");
    testUser.setUsername("testUsername");

    // when
    User createdUser = userService.createUser(testUser);

    // then
    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateUsername_throwsException() {
    assertNull(userRepository.findByUsername("testUsername"));

    User testUser = new User();
    testUser.setPassword("123");
    testUser.setUsername("testUsername");
    User createdUser = userService.createUser(testUser);

    // attempt to create second user with same username
    User testUser2 = new User();

    // change the name but forget about the username
    testUser2.setPassword("456");
    testUser2.setUsername("testUsername");

    // check that an error is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser2));
  }

    @Test
    public void login_validInputs_success() {
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("123");
        User createdUser = userService.createUser(testUser);

        User loginUser = new User();
        loginUser.setUsername("testUsername");
        loginUser.setPassword("123");
        User resultUser = userService.login(loginUser);

        // then
        assertEquals(resultUser.getPassword(), createdUser.getPassword());
        assertEquals(resultUser.getUsername(), createdUser.getUsername());
        assertNotNull(resultUser.getToken());
        assertEquals(UserStatus.ONLINE, resultUser.getStatus());
    }

    @Test
    public void login_invalidUsername_throwsException() {
        assertNull(userRepository.findByUsername("testUsername"));

        User loginUser = new User();
        loginUser.setUsername("testUsername");
        loginUser.setPassword("123");

        // check that an error is thrown
        assertThrows(ResponseStatusException.class, () -> userService.login(loginUser));
    }

    @Test
    public void updateUser_validInputs_success() {
        // given
        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");
        testUser.setAvatar("testAvatar");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);


        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setBirthday(LocalDate.now());
        updatedUser.setAvatar("updatedAvatar");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setToken(testUser.getToken());

        // when
        User resultUser = userService.updateUser(createdUser.getId(), updatedUser);

        // then
        assertEquals(updatedUser.getUsername(), resultUser.getUsername());
        assertEquals(updatedUser.getBirthday(), resultUser.getBirthday());
        assertEquals(updatedUser.getAvatar(), resultUser.getAvatar());
        assertEquals(updatedUser.getPassword(), resultUser.getPassword());
    }
    @Test
    public void updateUser_notFound_throwsException() {
        assertTrue(userRepository.findById(999L).isEmpty());
        // given
        User nonExistentUser = new User();
        nonExistentUser.setId(999L); // Assume a non-existent user ID
        // when, then
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(nonExistentUser.getId(), new User()));
    }
    @Test
    public void updateUser_tokenMismatch_throwsException() {
        // given
        User testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        User createdUser = userService.createUser(testUser);
        assertNotEquals(testUser.getToken(),"differentToken");
        assertNotEquals(testUser.getId(),2l);

        // Create another user with different token
        User userWithDifferentToken = new User();
        userWithDifferentToken.setToken("differentToken");
        userWithDifferentToken.setId(2l); // Use same ID
        userWithDifferentToken.setUsername("anotheruser");
        userWithDifferentToken.setPassword("testPassword");
        userWithDifferentToken.setStatus(UserStatus.ONLINE);
        userRepository.save(userWithDifferentToken);

        // when, then
        assertThrows(ResponseStatusException.class, () -> userService.updateUser(createdUser.getId(), new User()));
    }

//    @Test
//    public void getUserById_validId__success() {
//      assertFalse(userRepository.findById(1L).isPresent());
//
//      User testUser = new User();
//      testUser.setPassword("123");
//      testUser.setUsername("testUsername");
//
//      userService.createUser(testUser);
//
//      // when
//      Optional<User> getUser = userService.getUserById(1L);
//
//      // then
//      assertEquals(testUser.getUsername(), getUser.get().getUsername());
//      assertEquals(testUser.getPassword(), getUser.get().getPassword());
//    }
}
