package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.service.ToolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.concurrent.TimeUnit;

import java.util.*;

import static ch.uzh.ifi.hase.soprafs24.constant.GameMode.GUESSING;

@Service
@Transactional
public class QuestionService {
    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    private final ItemRepository itemRepository;

    private final ToolService toolService;
    private final RoomService roomService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ItemRepository itemRepository, ToolService toolService, RoomService roomService) {
        this.questionRepository = questionRepository;
        this.itemRepository = itemRepository;
        this.toolService = toolService;
        this.roomService = roomService;
    }

    private List<Item> getAllItems() {
        return itemRepository.findAll(); // Assuming itemRepository exists
    }


    public void createGuessingQuestions(Long roomId) {
        // make sure the room exist
        Room room = roomService.findById(roomId);

        // Retrieve all items from the database
        List<Item> allItems = getAllItems();
        // Shuffle the list of items to randomize the order
        Collections.shuffle(allItems);
        // Create a list to store selected items for each round
        List<Item> selectedItems = new ArrayList<>();

        // Loop through each round and select a unique item for each round
        for (int round = 1; round <= 3; round++) {
            // Ensure that there are enough items for each round
            if (allItems.size() < round) {
                throw new RuntimeException("Not enough items in the database for all rounds");
            }

            // Select an item for the current round
            Item selectedItem = allItems.get(round - 1); // Adjust index to start from 0
            selectedItems.add(selectedItem);

            //generate random number between 0.2-0.4 to set guessing price range bar
            double maxScale = 0.2 + Math.random() * 0.2;
            double minScale = 0.2 + Math.random() * 0.2;
            float selectedItemPrice = selectedItem.getPrice();
            int leftRange = (int) Math.floor((1 - minScale) * selectedItemPrice);
            int rightRange = (int) Math.ceil((1 + maxScale) * selectedItemPrice);

            // Create a Question object for the current round
            Question newQuestion = new Question();
            newQuestion.setRoomId(roomId);
            newQuestion.setGameMode(GameMode.GUESSING);
            newQuestion.setItemId(selectedItem.getId());
            newQuestion.setItemImage(selectedItem.getImageURL());
            newQuestion.setRoundNumber(round);
            newQuestion.setAnswer(selectedItemPrice); // Set the item's price as the answer
            newQuestion.setLeftRange(leftRange);
            newQuestion.setRightRange(rightRange);
            // Save the Question object to the repository
            questionRepository.save(newQuestion);
        }

    }

    public void createBudgetQuestions(Long roomId) {
        // make sure the room exist
        Room room = roomService.findById(roomId);
        // Retrieve all items from the database (can be optimized later)
        List<Item> allItems = getAllItems();
        // Shuffle the list of items to randomize the order
        Collections.shuffle(allItems);

        // Loop through each round (3 rounds)
        for (int round = 1; round <= 3; round++) {

            int numItems = 9; //9 items per round
            // Ensure there are enough items left in the list
            if (allItems.size() < numItems) {
                throw new RuntimeException("Not enough items in the database for all rounds");
            }

            // Create a list to store selected items for this round
            List<Item> selectedItems = new ArrayList<>();

            // Select a random subset of items from the remaining ones
            for (int i = round * numItems - 9; i < round * numItems; i++) {
                Item selectedItem = allItems.get(i);
                selectedItems.add(selectedItem);
            }

            //randomly determine the item number to calculate budget
            int budgetItemNum = (int) (Math.random() * 9) + 1;//at least one item
            //shuffle selected items order
            Collections.shuffle(selectedItems);
            float budget = 0;
            // Calculate the budget by summing the prices of selected items and rounding up
            for (int i = 0; i < budgetItemNum; i++) {
                float price = selectedItems.get(i).getPrice();
                budget += price;
            }
            budget = (float) Math.ceil(budget);

            // Create a comma-separated string containing item IDs for the question
            StringBuilder itemList = new StringBuilder();
            StringBuilder itemImageList = new StringBuilder();
            for (Item item : selectedItems) {
                itemList.append(item.getId()).append(",");
                itemImageList.append(item.getImageURL()).append(",");
            }
            itemList.deleteCharAt(itemList.length() - 1); // Remove the trailing comma
            itemImageList.deleteCharAt(itemImageList.length() - 1);

            // Create a Question object for the current round
            Question newQuestion = new Question();
            newQuestion.setRoomId(roomId);
            newQuestion.setGameMode(GameMode.BUDGET);
            newQuestion.setItemList(itemList.toString());
            newQuestion.setItemImageList(itemImageList.toString());
            newQuestion.setRoundNumber(round);
            newQuestion.setBudget(budget);

            // Save the Question object to the repository
            questionRepository.save(newQuestion);
        }
    }

    public Question getQuestionsByRoomRound(Long roomId, int roundNumber) {
        return questionRepository.findAllByRoomIdAndRoundNumber(roomId, roundNumber);
    }


    public Question getQuestionsByRoomRoundandUserId(Long roomId, int roundNumber, Long userId) {
        Question originQuestion = questionRepository.findAllByRoomIdAndRoundNumber(roomId, roundNumber);

        // Create a new instance of Question
        Question modifiedQuestion = new Question();
        // Copy properties from the original question to the new question
        modifiedQuestion.setId(originQuestion.getId());
        modifiedQuestion.setRoomId(originQuestion.getRoomId());
        modifiedQuestion.setGameMode(originQuestion.getGameMode());
        modifiedQuestion.setItemId(originQuestion.getItemId());
        modifiedQuestion.setItemImage(originQuestion.getItemImage());
        modifiedQuestion.setRoundNumber(originQuestion.getRoundNumber());
        modifiedQuestion.setAnswer(originQuestion.getAnswer());
        modifiedQuestion.setLeftRange(originQuestion.getLeftRange());
        modifiedQuestion.setRightRange(originQuestion.getRightRange());
        modifiedQuestion.setBlur(originQuestion.getBlur());
        // Get the user's tools
        List<String> userTools = toolService.getUserTools(userId);
        // Check if the user has the HINT tool
        boolean hasHintTool = userTools != null && userTools.contains(ToolType.HINT.name());
        // Get all users in the same room
        List<User> roomUsers = roomService.getUsersByRoomId(roomId);

        // Check if any other users in the room have the BLUR tool
        boolean otherUsersHaveBlurTool = roomUsers.stream()
                .filter(user -> !user.getId().equals(userId)) // Exclude the current user
                .anyMatch(user -> {
                    List<String> tools = toolService.getUserTools(user.getId());
                    return tools != null && tools.contains(ToolType.BLUR.name());
                });

        // Adjust question properties based on tool usage
        // Shrink the range into 0-0.2, directly apply change on left&right range may lead to override the price or make no change
        double maxScale = Math.random() * 0.2;
        double minScale = Math.random() * 0.2;
        float originalPrice = originQuestion.getAnswer();
        if (hasHintTool) {
            // Adjust price range
            int newLeftRange = (int) Math.floor(originalPrice * (1 - minScale));
            int newRightRange = (int) Math.ceil(originalPrice * (1 + maxScale));
            modifiedQuestion.setLeftRange(newLeftRange);
            modifiedQuestion.setRightRange(newRightRange);
        }
        if (otherUsersHaveBlurTool) {
            // Set blur property to true
            modifiedQuestion.setBlur(true);
        }

        return modifiedQuestion;
    }

    public String getReady(Long roomId, Long userId) {
        Room room = roomService.findById(roomId);
        Long requireAmount = room.getPlayerAmount();
        String[] playerIds = room.getPlayerIds().split(",");
        Long actualAmount = Long.valueOf(playerIds.length);
        Long ownerId = room.getOwnerId();

        // Check if the required amount is already met
        if (actualAmount.equals(requireAmount)) {
            //only execute the following part once for each room
            if (room.getGameMode() == GameMode.GUESSING && Objects.equals(userId, ownerId)) {
                createGuessingQuestions(roomId); //create questions
                roomService.resetPlayerScore(roomId); //reset player's score as 100
            }
           else if (room.getGameMode() == GameMode.BUDGET && Objects.equals(userId, ownerId)) {
            createBudgetQuestions(roomId);
            roomService.resetPlayerScore(roomId);
        }
        // Return a message indicating that the game is ready
        return "The game is ready!";
     } else {
        // If not met, wait for the condition to be satisfied
        try {
            // Wait for a certain period and then recheck the condition
            TimeUnit.SECONDS.sleep(1); // Wait for 1 second
            return getReady(roomId, userId); // Recursive call to recheck the condition
        } catch (InterruptedException e) {
            // Handle interruption
            log.error("Thread interrupted while waiting for players to join.", e);
            Thread.currentThread().interrupt();
            return "An error occurred while waiting for players to join.";
          }
        }
    }
}