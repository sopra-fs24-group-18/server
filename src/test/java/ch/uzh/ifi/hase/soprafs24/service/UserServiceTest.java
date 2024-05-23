package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import org.springframework.http.HttpStatus;

public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  private User testUser;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // given
    testUser = new User();
    testUser.setId(1L);
    testUser.setPassword("123");
    testUser.setUsername("testUsername");
    testUser.setCreationDate(LocalDate.now());
    testUser.setBirthday(LocalDate.now());
    testUser.setAvatar("111");

    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
  }

  @Test
  public void createUser_validInputs_success() {
    // when -> any object is being save in the userRepository -> return the dummy
    // testUser
    User createdUser = userService.createUser(testUser);

    // then
    Mockito.verify(userRepository, times(1)).save(Mockito.any());

    assertEquals(testUser.getId(), createdUser.getId());
//    assertEquals(testUser.getPassword(), createdUser.getPassword());
    assertEquals(testUser.getUsername(), createdUser.getUsername());
    assertNotNull(createdUser.getToken());
    assertEquals(UserStatus.ONLINE, createdUser.getStatus());
  }

  @Test
  public void createUser_duplicateName_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
//    Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

  @Test
  public void createUser_duplicateInputs_throwsException() {
    // given -> a first user has already been created
    userService.createUser(testUser);

    // when -> setup additional mocks for UserRepository
//    Mockito.when(userRepository.findByName(Mockito.any())).thenReturn(testUser);
    Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

    // then -> attempt to create second user with same user -> check that an error
    // is thrown
    assertThrows(ResponseStatusException.class, () -> userService.createUser(testUser));
  }

    @Test
    public void login_validInputs_success() {
        User createdUser = userService.createUser(testUser);

        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("123");
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        User loginUser = userService.login(user);

        Mockito.verify(userRepository, times(2)).findByUsername(Mockito.any());

        assertEquals(loginUser.getId(), createdUser.getId());
        assertEquals(loginUser.getUsername(), createdUser.getUsername());
        assertEquals(loginUser.getToken(), createdUser.getToken());
        assertEquals(UserStatus.ONLINE, loginUser.getStatus());
    }

    @Test
    public void login_invalidUsername_throwsException() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(user));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void login_incorrectPassword_throwsException() {
        User loginUser = new User();
        loginUser.setUsername("testUsername");
        loginUser.setPassword("333");

        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.login(loginUser));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }


    @Test
  public void getUsers_validInputs_success() {
        // given
      List<User> userList = Arrays.asList(testUser, new User(), new User());
      Mockito.when(userRepository.findAll()).thenReturn(userList);
        // when
      List<User> returnedUsers = userService.getUsers();
        // then
      Mockito.verify(userRepository, times(1)).findAll();
      assertEquals(3, returnedUsers.size());
    }

    @Test
    public void getUserById_validInputs_success() {
        // given
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        // when
        Optional<User> returnedUserOptional = userService.getUserById(1L);
        // then
        Mockito.verify(userRepository, times(1)).findById(1L);

        // Check if the user is present in the optional
        assertTrue(returnedUserOptional.isPresent());

        // Get the user object from the optional
        User returnedUser = returnedUserOptional.get();

        // Now you can access the properties of the user object
        assertEquals(testUser.getUsername(), returnedUser.getUsername());
        assertEquals(testUser.getStatus(), returnedUser.getStatus());
        assertEquals(testUser.getAvatar(), returnedUser.getAvatar());
        assertEquals(testUser.getCreationDate(), returnedUser.getCreationDate());
        assertEquals(testUser.getBirthday(), returnedUser.getBirthday());
    }

    @Test
    public void getUserById_userNotFound_throwNotFoundException() {
        // given
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.getUserById(1L));
        // Check if the status code of the exception is 404 (Not Found)
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void updateUser_validInputs_success() {
        // given
        String updatedUsername = "updatedUsername";
        String updatedAvatar = "testAvatar";
        String updatedPassword = "testPassword";
        LocalDate updatedBirthdate = LocalDate.now().minusDays(1);



        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // when
        User updatedUser = new User();
        updatedUser.setUsername(updatedUsername);
        updatedUser.setBirthday(updatedBirthdate);
        updatedUser.setAvatar(updatedAvatar);
        updatedUser.setPassword(updatedPassword);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(updatedUser);
        User result = userService.updateUser(1L, updatedUser);
        // then
        assertEquals(updatedUsername, result.getUsername());
        assertEquals(updatedBirthdate, result.getBirthday());
        assertEquals(updatedAvatar, result.getAvatar());
        assertEquals(updatedPassword, result.getPassword());
    }

    @Test
    public void updateUser_userNotFound_throwNotFoundException() {
        // given
        User updatedUser = new User();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUser(1L, updatedUser));

        // Check if the status code of the exception is 404 (Not Found)
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
    @Test
    public void updateUser_invalidNewName() {
        // given
        String updatedUsername = " ";
        String updatedPassword = "testPassword";
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        // when
        User updatedUser = new User();
        updatedUser.setUsername(updatedUsername);
        updatedUser.setPassword(updatedPassword);
        // then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUser(1L, updatedUser));
        assertEquals("The username cannot be empty", exception.getReason());
    }
    @Test
    public void updateUser_invalidNewPassword() {
        // given
        String updatedUsername = "newUsername";
        String updatedPassword = " ";
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        // when
        User updatedUser = new User();
        updatedUser.setUsername(updatedUsername);
        updatedUser.setPassword(updatedPassword);
        // then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.updateUser(1L, updatedUser));
        assertEquals("The password cannot be empty", exception.getReason());
    }

    @Test
    public void logout_success(){
      User user = new User();
      user.setUsername("u1");
      user.setPassword("123");
      user.setToken("tokenForU1");
      user.setStatus(UserStatus.ONLINE);
      Mockito.when(userRepository.findByToken("tokenForU1")).thenReturn(user);

      userService.logout(user);
      assertEquals(UserStatus.OFFLINE, user.getStatus());
    }

    @Test
    public void logout_invalidToken(){
        User user = new User();
        user.setUsername("u1");
        user.setPassword("123");
        user.setToken("invalidToken");
        user.setStatus(UserStatus.ONLINE);
        Mockito.when(userRepository.findByToken("tokenForU1")).thenReturn(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userService.logout(user));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    public void userId2Username(){
        User user = new User();
        user.setId(3L);
        user.setUsername("u3");
        user.setPassword("123");
        Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        String username = userService.userId2Username(user.getId());
        assertEquals(user.getUsername(), username);
    }

    @Test
    public void userIdList2UsernameList(){
        User user = new User();
        user.setId(3L);
        user.setUsername("u3");
        user.setPassword("123");

        User user4 = new User();
        user4.setId(4L);
        user4.setUsername("u4");
        user4.setPassword("123");

        Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(4L)).thenReturn(Optional.of(user4));

        String result = userService.userIdList2UsernameList("3,4");
        assertEquals("u3,u4", result);
    }

    @Test
    public void resetScore(){
        User user = new User();
        user.setId(3L);
        user.setUsername("u3");
        user.setPassword("123");
        user.setScore(10L);

        User user4 = new User();
        user4.setId(4L);
        user4.setUsername("u4");
        user4.setPassword("123");
        user4.setScore(500L);

        List<User> users = List.of(user, user4);

        userService.resetScore(users);
        assertEquals(100L, users.get(0).getScore());
        assertEquals(100L, users.get(1).getScore());
    }
}
