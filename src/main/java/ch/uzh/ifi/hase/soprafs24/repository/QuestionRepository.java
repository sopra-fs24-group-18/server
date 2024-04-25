package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("questionRepository")
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Question findAllByRoomIdAndRoundNumber(long roomId,int roundNumber);
}
