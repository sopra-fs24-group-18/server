package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.rest.dto.question.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.QuestionDTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.QuestionService;
import ch.uzh.ifi.hase.soprafs24.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/games")
public class QuestionController {
    private final QuestionService questionService;



    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/{roomId}/{userId}/getReady")
    public ResponseEntity<?> getReadyForGame(@PathVariable Long roomId,@PathVariable Long userId) {
        try {
            questionService.updateReadyList(roomId,userId);
            String response = questionService.getReady(roomId,userId);
            return ResponseEntity.ok(response);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Game could not start correctly: " + e.getMessage());
        }
    }

    @GetMapping("/{roomId}/{roundNumber}/{userId}")
    public ResponseEntity<?> getQuestionByUserId(@PathVariable Long roomId, @PathVariable int roundNumber,@PathVariable Long userId) {
        try {
            Question question = questionService.getQuestionsByRoomRoundandUserId(roomId,roundNumber,userId);
            if (question != null) {
                QuestionGetDTO questionGetDTO = QuestionDTOMapper.INSTANCE.convertEntitytoQuestionGetDTO(question);
                return ResponseEntity.ok(questionGetDTO);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }

    }
}
