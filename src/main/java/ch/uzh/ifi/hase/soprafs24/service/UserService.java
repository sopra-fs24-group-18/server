package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
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
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setStatus(UserStatus.ONLINE);
    LocalDate date = LocalDate.now();
    newUser.setCreationDate(date);
    checkIfUserExists(newUser);
    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,
              "The username provided is not unique. Therefore, the user could not be created!");
    }
  }

    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            String baseErrorMessage = "User with userId %d was not found!";
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(baseErrorMessage, id));
        }
        return user;

    }

    public User login(User user) {
        User userByUsername = userRepository.findByUsername(user.getUsername());

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed! Invalid username!");
        } else if (!userByUsername.getPassword().equals(user.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed! Your password is wrong!");
        }
        userByUsername.setStatus(UserStatus.ONLINE);
        return userByUsername;
    }

 /*   public void updateUser(Long userId, User userInput) {
        Optional<User> user = getUserById(userId);
        //userInput.getToken() is the token from the person who want to edit the profile
        //user.get().getToken() is the token from the person who being edited
        //this trick can reduce communication cost, and improve security
//        if(!user.get().getToken().equals(userInput.getToken())){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to edit other people's profile!");
//        }
        if(userInput.getUsername() != null){
            if(!user.get().getUsername().equals(userInput.getUsername())){
                checkIfUserExists(userInput);
                user.ifPresent(x -> x.setUsername(userInput.getUsername()));
            }
        }
        if(userInput.getBirthday() != null){
            user.ifPresent(x -> x.setBirthday(userInput.getBirthday()));
        }
    }*/
 public User updateUser(Long userId, User new_info){
     User existingUser = userRepository.findById(userId)
             .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
     String new_username = new_info.getUsername();
     LocalDate new_birthdate = new_info.getBirthday();
     String Usertoken = new_info.getToken();
     String new_avatar = new_info.getAvatar();
     String new_password = new_info.getPassword();

     if (!Objects.equals(Usertoken, existingUser.getToken())) {
         throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                 "The user has no right to edit this page");
     }
     //if new username is not null and not equal to the orginal name, check the duplication
     if (new_username!= null && !new_username.equals(existingUser.getUsername())){
         User existingUserWithNewUsername = userRepository.findByUsername(new_username);
         if (existingUserWithNewUsername != null){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                     String.format("The username '%s' is already in use. Please choose another name.",new_username));

         }
         existingUser.setUsername(new_username);
     }
     if (new_birthdate != null){
         existingUser.setBirthday(new_birthdate);
     }
     if (new_avatar != null){
         existingUser.setAvatar(new_avatar);
     }
     if (new_password != null){
         existingUser.setPassword(new_password);
     }
     return userRepository.save(existingUser);

 }

    public void logout(User userInput){
        User user = userRepository.findByToken(userInput.getToken());
        user.setStatus(UserStatus.OFFLINE);
    }
}
