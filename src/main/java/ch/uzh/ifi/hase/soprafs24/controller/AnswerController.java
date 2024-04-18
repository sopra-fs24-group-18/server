package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Answer;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.answer.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.AnswerDTOMapper;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.UserDTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnswerController {

  private final AnswerService answerService;
  private final UserRepository userRepository;

  AnswerController(AnswerService answerService, UserRepository userRepository) {
    this.answerService = answerService;
    this.userRepository = userRepository;
  }

  @PostMapping("/answers/guessMode")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Long calculatePointGuessingMode(@RequestBody AnswerPostDTO answerPostDTO) {
      Answer answer = AnswerDTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);

      return answerService.calculatePointGuessingMode(answer);
  }
}
