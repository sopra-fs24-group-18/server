package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.entity.*;
import ch.uzh.ifi.hase.soprafs24.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

  public List<Long> calculatePoints(Answer answer) {
      Optional<Question> optionalQuestion = questionRepository.findById(answer.getQuestionId());
      if(!optionalQuestion.isPresent()){
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The question was not found!");
      }
      Question question = optionalQuestion.get();

      String[] playerIds;
      Long playerAmount = 0L;
      Long answeredPlayers = 0L;
      do{
          Optional<Room> optionalRoom = roomRepository.findById(question.getRoomId());
          if (!optionalRoom.isPresent()) {
              throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The room was not found!");
          }

          playerIds = optionalRoom.get().getPlayerIds().split(",");
          playerAmount = Long.valueOf(playerIds.length);

          Set<Long> userIds = new HashSet<>();
          List<Answer> answers = answerRepository.findByQuestionId(question.getId());
          for (Answer ans : answers) {
              userIds.add(ans.getUserId());
          }
          answeredPlayers = Long.valueOf(userIds.size());
      }while (!playerAmount.equals(answeredPlayers));

      Long point = 0L;
      // all users have submitted the answers, now calculate the rank and reward the points
      if(question.getGameMode().equals(GameMode.GUESSING)){
          point = rankGuessMode(answer, playerIds, question.getAnswer());
      }
      else if(question.getGameMode().equals(GameMode.BUDGET)){
          point = rankBudgetMode(answer, playerIds, question.getBudget());
      }

      // Update the user's score
      Long bonus = updateUserScore(answer.getUserId(), point);

      List<Long> list = new ArrayList<>();
      list.add(point);
      list.add(bonus);

      return list;
  }

    private Long rankGuessMode(Answer answer, String[] playerIds, Float realPrice) {
        // using Set to prevent user double-click the submit button and submit same answers for multiple times
        Set<Answer> answersSet = new HashSet<>(answerRepository.findByQuestionId(answer.getQuestionId()));

        // convert Set to List in order to sort the answer entries
        List<Answer> answers = new ArrayList<>(answersSet);

        Collections.sort(answers, (a1, a2) -> Float.compare(Math.abs(a1.getGuessedPrice() - realPrice),
                Math.abs(a2.getGuessedPrice() - realPrice)));

        Long userId = answer.getUserId();
        // Find the user's rank in the sorted list
        int rank = -1;
        for (int i = 0; i < answers.size(); i++) {
            if (userId.equals(answers.get(i).getUserId())) {
                rank = i;
                break;
            }
        }

        // Check if there are previous users with the same score
        if (rank != -1) {
            for (int i = rank - 1; i >= 0; i--) {
                if (Math.abs(answers.get(i).getGuessedPrice() - realPrice) == Math.abs(answers.get(rank).getGuessedPrice() - realPrice)) {
                    rank = i;
                } else {
                    break;
                }
            }
        }

        // Calculate the points based on the user's rank
        Long points = reward(rank);

        return points;
    }

    private Long rankBudgetMode(Answer answer, String[] playerIds, Float budget) {
        // using Set to prevent user double-click the submit button and submit same answers for multiple times
        Set<Answer> answersSet = new HashSet<>(answerRepository.findByQuestionId(answer.getQuestionId()));

        // convert Set to List in order to sort the answer entries
        List<Answer> answers = new ArrayList<>(answersSet);

        Map<Long, Float> sumPriceMap = new HashMap<>();
        for (Answer ans : answers) {
            Float sumPrice = calculateTotalPrice(ans);

            // Store the sum of prices for the user in the map
            sumPriceMap.put(ans.getUserId(), sumPrice);
        }

        // Sort the map by sum of prices in ascending order
        List<Map.Entry<Long, Float>> sortedEntries = sumPriceMap.entrySet().stream()
                .sorted((entry1, entry2) -> {
                    // Calculate the absolute difference between budget and sum price
                    Float diff1 = Math.abs(entry1.getValue() - budget);
                    Float diff2 = Math.abs(entry2.getValue() - budget);
                    // Compare absolute differences
                    return diff1.compareTo(diff2);
                })
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

        // Check if there are previous users with the same score
        if (rank != -1) {
            for (int i = rank - 1; i >= 0; i--) {
                if (Math.abs(sortedEntries.get(i).getValue() - budget) == Math.abs(sortedEntries.get(rank).getValue() - budget)) {
                    rank = i;
                } else {
                    break;
                }
            }
        }

        // Calculate the points based on the user's rank
        Long points = reward(rank);

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

    public Long updateUserScore(Long userId, Long points) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user was not found!");
        }
        User user = optionalUser.get();
        Long oldScore = user.getScore();
        Long newScore = oldScore + points;

        Long bonus = 0L;

        if(user.getToolStatus() != null) {
            if (user.getToolStatus().contains(ToolType.BONUS.name()) && points.equals(100L)) {
                bonus = 60L;
                newScore += bonus;
            }
            if (user.getToolStatus().contains(ToolType.GAMBLE.name())) {
                if (points.equals(100L)) {
                    bonus = 2 * oldScore;
                    newScore += bonus;
                }
                else {
                    bonus = -oldScore;
                    newScore = 0L;
                }
            }
        }

        user.setScore(newScore);
        user.setToolList(null);
        user.setToolStatus(null);
        userRepository.save(user);
        return bonus;
    }

    public Float getRealPrice(Answer answer) {
        Optional<Question> optionalQuestion = questionRepository.findById(answer.getQuestionId());
        if(!optionalQuestion.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The question was not found!");
        }
        Question question = optionalQuestion.get();
        return question.getAnswer();
    }

    public Float calculateTotalPrice(Answer answer) {
      if(answer.getChosenItemList() == null || answer.getChosenItemList().length() == 0){
          return 0.0F;
      }
      List<Long> itemIds = Arrays.stream(answer.getChosenItemList().split(","))
              .map(Long::valueOf)
              .collect(Collectors.toList());

      // Retrieve the items from the database using the item ids
      List<Item> items = itemRepository.findAllByIdIn(itemIds);

      // Calculate the sum of prices for the items
      Float sumPrice = items.stream()
              .map(Item::getPrice)
              .reduce(0F, Float::sum);
      return sumPrice;
    }
}
