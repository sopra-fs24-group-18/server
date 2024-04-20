package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Resource;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnswerService {

  private final Logger log = LoggerFactory.getLogger(AnswerService.class);

  private final AnswerRepository answerRepository;
  private final QuestionRepository questionRepository;
  private final RoomRepository roomRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

    @Resource
    private PlatformTransactionManager platformTransactionManager;

  @Autowired
  public AnswerService(@Qualifier("answerRepository") AnswerRepository answerRepository, QuestionRepository questionRepository,
                       RoomRepository roomRepository, UserRepository userRepository, ItemRepository itemRepository) {
    this.answerRepository = answerRepository;
    this.questionRepository = questionRepository;
    this.roomRepository = roomRepository;
    this.userRepository = userRepository;
    this.itemRepository = itemRepository;
  }

  public void saveAnswer(Answer answer){
      answerRepository.save(answer);
      answerRepository.flush();
  }

  public Long calculatePoints(Answer answer) {
      Optional<Question> optionalQuestion = questionRepository.findById(answer.getQuestionId());
      if(!optionalQuestion.isPresent()){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The question was not found!");
      }
      Question question = optionalQuestion.get();

      Optional<Room> room = roomRepository.findById(question.getRoomId());
      if (!room.isPresent()) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The room was not found!");
      }

      String[] playerIds = room.get().getPlayerIds().split(",");
      Long playerAmount = Long.valueOf(playerIds.length);

      // check if all users have submitted the answers
      while (!playerAmount.equals(answerRepository.countByQuestionId(answer.getQuestionId()))) {
          try {
              Thread.sleep(1000);
          } catch (InterruptedException e) {
              e.printStackTrace();
          }
      }
      Long point = 0L;
      // all users have submitted the answers, calculate the rank and reward the points
      if(question.getGameMode().equals(GameMode.GUESSING)){
          point = rankGuessMode(answer, playerIds, question.getAnswer());
      }
      else if(question.getGameMode().equals(GameMode.BUDGET)){
          point = rankBudgetMode(answer, playerIds, question.getBudget());
      }

      return point;
  }

    private Long rankGuessMode(Answer answer, String[] playerIds, Float realPrice) {
        List<Answer> answers = answerRepository.findByQuestionId(answer.getQuestionId());
        Collections.sort(answers, (a1, a2) -> Float.compare(Math.abs(a1.getGuessedPrice() - realPrice),
                Math.abs(a2.getGuessedPrice() - realPrice)));

        Long userId = answer.getUserId();
        // Find the user's rank in the sorted list
        int rank = -1;
        for (int i = 0; i < Math.min(3, playerIds.length); i++) {
            if (userId.equals(answers.get(i).getUserId())) {
                rank = i;
                break;
            }
        }

        // Calculate the points based on the user's rank
        Long points = reward(rank);

        // Update the user's score
        updateUserScore(userId, points);

        return points;
    }

    private Long rankBudgetMode(Answer answer, String[] playerIds, Float budget) {
        List<Answer> answers = answerRepository.findByQuestionId(answer.getQuestionId());
        Map<Long, Float> sumPriceMap = new HashMap<>();
        for (Answer ans : answers) {
            List<Long> itemIds = Arrays.stream(ans.getChosenItemList().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());

            // Retrieve the items from the database using the item ids
            List<Item> items = itemRepository.findAllByIdIn(itemIds);

            // Calculate the sum of prices for the items
            Float sumPrice = items.stream()
                    .map(Item::getPrice)
                    .reduce(0F, Float::sum);

            // Store the sum of prices for the user in the map
            sumPriceMap.put(ans.getUserId(), sumPrice);
        }

        // Sort the map by sum of prices in ascending order
        List<Map.Entry<Long, Float>> sortedEntries = sumPriceMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Float>comparingByValue().reversed())
                .collect(Collectors.toList());

        Long userId = answer.getUserId();
        // Find the user's rank in the sorted list
        int rank = -1;
        for (int i = 0; i < sortedEntries.size(); i++) {
            if (sortedEntries.get(i).getKey().equals(userId)) {
                rank = i;
                break;
            }
        }

        // Calculate the points based on the user's rank
        Long points = reward(rank);

        // Update the user's score
        updateUserScore(userId, points);

        return points;
    }

    private Long reward(int rank) {
        if (rank == 0) {
            return 100L;
        } else if (rank == 1) {
            return 70L;
        } else if (rank == 2) {
            return 40L;
        } else {
            return 0L;
        }
    }

    private void updateUserScore(Long userId, Long points) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user was not found!");
        }
        User user = optionalUser.get();

        Long oldScore = user.getScore();
        user.setScore(oldScore + points);
        userRepository.save(user);
    }
}
