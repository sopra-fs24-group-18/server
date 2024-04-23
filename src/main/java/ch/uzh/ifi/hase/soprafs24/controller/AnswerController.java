package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Answer;
import ch.uzh.ifi.hase.soprafs24.rest.dto.answer.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.AnswerDTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnswerController {

  private final AnswerService answerService;

  AnswerController(AnswerService answerService) {
    this.answerService = answerService;
  }

  @PostMapping("/answers/guessMode")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Long calculatePointGuessingMode(@RequestBody AnswerPostDTO answerPostDTO) {
      Answer answer = AnswerDTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);
      answerService.saveAnswer(answer);
      return answerService.calculatePoints(answer);
  }

  @PostMapping("/answers/budgetMode")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Long calculatePointBudgetMode(@RequestBody AnswerPostDTO answerPostDTO) {
      Answer answer = AnswerDTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);
      answerService.saveAnswer(answer);
      return answerService.calculatePoints(answer);
  }
}
