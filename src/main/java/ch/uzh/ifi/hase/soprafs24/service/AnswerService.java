package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Answer;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.AnswerRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnswerService {

  private final Logger log = LoggerFactory.getLogger(AnswerService.class);

  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;
  private final RoomRepository roomRepository;
  private final UserService userService;

  @Autowired
  public AnswerService(@Qualifier("answerRepository") AnswerRepository answerRepository, QuestionRepository questionRepository,
                       RoomRepository roomRepository, UserService userService) {
    this.answerRepository = answerRepository;
    this.questionRepository = questionRepository;
    this.roomRepository = roomRepository;
    this.userService = userService;
  }

    public Long calculatePoint(Answer answer) {
        answerRepository.save(answer);
        Optional<Question> question = questionRepository.findById(answer.getQuestionId());
        if(!question.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The question was not found!");
        }

        Optional<Room> room = roomRepository.findById(question.get().getRoomId());
        if (!room.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The room was not found!");
        }

        Long playerAmount = room.get().getPlayerAmount();
        String[] playerIds = room.get().getPlayerIds().split(",");

        // check if all users have submitted the answers
        while (playerAmount != answerRepository.countByQuestionId(answer.getQuestionId())) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // all users have submitted the answers, calculate the rank and reward the points
        Long point = calculateRankAndPoint(answer, playerIds, question.get());
        return point;
    }

    private Long calculateRankAndPoint(Answer answer, String[] playerIds, Question question) {
        List<Answer> answers = answerRepository.findByQuestionId(answer.getQuestionId());
        Collections.sort(answers, (a1, a2) -> Double.compare(Math.abs(a1.getGuessedPrice() - question.getAnswer()),
                Math.abs(a2.getGuessedPrice() - question.getAnswer())));

        Long userId = answer.getUserId();
        Optional<User> optionalUser = userService.getUserById(userId);
        if(!optionalUser.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user was not found!");
        }
        User user = optionalUser.get();

        for (int i = 0; i < Math.min(3, playerIds.length); i++) {
            if (userId.equals(answers.get(i).getUserId())) {
                if (i == 0) {
                    return 100L;
                } else if (i == 1) {
                    return 70L;
                } else if (i == 2) {
                    return 40L;
                }
            }
        }
        return 20L;
    }
}
