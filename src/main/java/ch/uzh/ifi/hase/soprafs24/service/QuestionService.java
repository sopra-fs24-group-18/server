package ch.uzh.ifi.hase.soprafs24.service;


import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Item;
import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static ch.uzh.ifi.hase.soprafs24.constant.GameMode.GUESSING;

@Service
@Transactional
public class QuestionService {
    private final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final QuestionRepository questionRepository;

    private final ItemRepository itemRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ItemRepository itemRepository) {
        this.questionRepository = questionRepository;
        this.itemRepository = itemRepository;
    }

    private List<Item> getAllItems() {
        return itemRepository.findAll(); // Assuming itemRepository exists
    }


    public void createGuessingQuestions(Long roomId){
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

            // Create a Question object for the current round
            Question newQuestion = new Question();
            newQuestion.setRoomId(roomId);
            newQuestion.setGameMode(GameMode.GUESSING);
            newQuestion.setItemId(selectedItem.getId());
            newQuestion.setItemImage(selectedItem.getImageURL());
            newQuestion.setRoundNumber(round);
            newQuestion.setAnswer(selectedItem.getPrice()); // Set the item's price as the answer
            // Save the Question object to the repository
            questionRepository.save(newQuestion);
        }

    }

    public void createBudgetQuestions(Long roomId) {
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
            for (int i = round * numItems -9; i < round * numItems; i++) {
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
            budget = (float)Math.ceil(budget);

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
    public Question getQuestionsByRoomRound(Long roomId,int roundNumber) {
        return questionRepository.findAllByRoomIdAndRoundNumber(roomId, roundNumber);
    }
}
