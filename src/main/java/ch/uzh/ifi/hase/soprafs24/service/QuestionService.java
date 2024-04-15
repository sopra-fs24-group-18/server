package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.repository.QuestionRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ItemRepository; // Import ItemRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static ch.uzh.ifi.hase.soprafs24.constant.GameMode.GUESSING;

@Service
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ItemRepository itemRepository; // Inject ItemRepository

    @Autowired
    public QuestionService(QuestionRepository questionRepository, ItemRepository itemRepository) {
        this.questionRepository = questionRepository;
        this.itemRepository = itemRepository;
    }

    public Question create_question_guess(Long roomId){
        Question question = new Question();
        question.setRoomId(roomId);
        // Fetch a random item ID where the image URL is not 'default'
        Long itemId = itemRepository.findIdByNonDefaultImage_guess();
        question.setItemId(itemId);
        question.setGameMode(GUESSING);
        // You might want to set other properties of the question here
        // Save the question in the database
        return questionRepository.save(question);
    }
}