package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserReducedGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserReducedGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserReducedGetDTO(createdUser);
  }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserReducedGetDTO login(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userCredentials = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        User user = userService.login(userCredentials);
        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToUserReducedGetDTO(user);
    }
/*
  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUserById(@PathVariable Long userId){
      Optional<User> user = userService.getUserById(userId);
      if(user.isPresent()){
          return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user.get());
      }else {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
      }
  }

 */
  @GetMapping("/users/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<?> getUserById(@PathVariable Long userId) {
      try {
          Optional<User> user = userService.getUserById(userId);
          if (user.isPresent()) {
              UserGetDTO userDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user.get());
              return ResponseEntity.ok(userDTO);
          } else {
              String errorMessage = String.format("User with userId %d was not found!", userId);
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
          }
      } catch (ResponseStatusException e) {
        // If userService throws a ResponseStatusException, catch it and return the appropriate response
          return ResponseEntity.status(e.getStatus()).body(e.getReason());
      }
  }

    @PutMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void updateUser(@PathVariable Long userId, @RequestBody UserPutDTO userPutDTO) {
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        userService.updateUser(userId, userInput);
    }

    @PutMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void logout(@RequestBody UserPutDTO userPutDTO){
        User userInput = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);
        // change user status to OFFLINE
        userService.logout(userInput);
    }
}
