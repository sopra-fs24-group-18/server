package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Answer;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.rest.dto.answer.AnswerPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnswerDTOMapper {

  AnswerDTOMapper INSTANCE = Mappers.getMapper(AnswerDTOMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(source = "questionId", target = "questionId")
  @Mapping(source = "userId", target = "userId")
  @Mapping(source = "guessedPrice", target = "guessedPrice")
  @Mapping(source = "chosenItemList", target = "chosenItemList")
  Answer convertAnswerPostDTOtoEntity(AnswerPostDTO answerPostDTO);
}