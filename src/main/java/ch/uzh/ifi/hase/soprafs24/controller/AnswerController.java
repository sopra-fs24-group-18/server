package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Answer;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.answer.AnswerGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.answer.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.AnswerDTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.AnswerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AnswerController {

  private final AnswerService answerService;

  AnswerController(AnswerService answerService) {
    this.answerService = answerService;
  }

  @PostMapping("/answers/guessMode")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public AnswerGetDTO calculatePointGuessingMode(@RequestBody AnswerPostDTO answerPostDTO) {
      Answer answer = AnswerDTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);
      answerService.saveAnswer(answer);
      List<Long> point = answerService.calculatePoints(answer);

      AnswerGetDTO answerGetDTO = new AnswerGetDTO();
      answerGetDTO.setPoint(point.get(0));
      answerGetDTO.setBonus(point.get(1));
      answerGetDTO.setRealPrice(answerService.getRealPrice(answer));
      return answerGetDTO;
  }

  @PostMapping("/answers/budgetMode")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public AnswerGetDTO calculatePointBudgetMode(@RequestBody AnswerPostDTO answerPostDTO) {
      Answer answer = AnswerDTOMapper.INSTANCE.convertAnswerPostDTOtoEntity(answerPostDTO);
      answerService.saveAnswer(answer);
      List<Long> point = answerService.calculatePoints(answer);

      AnswerGetDTO answerGetDTO = new AnswerGetDTO();
      answerGetDTO.setPoint(point.get(0));
      answerGetDTO.setBonus(point.get(1));
      answerGetDTO.setRealPrice(answerService.calculateTotalPrice(answer));
      return answerGetDTO;
  }
}
