package ch.uzh.ifi.hase.soprafs24.rest.mapper;

import ch.uzh.ifi.hase.soprafs24.entity.Question;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.question.QuestionGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface QuestionDTOMapper {
    QuestionDTOMapper INSTANCE = Mappers.getMapper(QuestionDTOMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "roomId", target = "roomId")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(source = "itemId", target = "itemId")
    @Mapping(source = "itemImage", target = "itemImage")
    @Mapping(source = "itemList", target = "itemList")
    @Mapping(source = "itemImageList", target = "itemImageList")
    @Mapping(source = "roundNumber", target = "roundNumber")
    @Mapping(source = "budget", target = "budget")
    @Mapping(source = "answer", target = "answer")
    QuestionGetDTO convertEntitytoQuestionGetDTO(Question question);
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "roomId", target = "roomId")
    @Mapping(source = "gameMode", target = "gameMode")
    @Mapping(target = "itemId", ignore = true)
    @Mapping(target = "itemImage", ignore = true)
    @Mapping(target = "itemList", ignore = true)
    @Mapping(target = "itemImageList", ignore = true)
    @Mapping(source = "roundNumber", target = "roundNumber")
    @Mapping(target = "budget", ignore = true)
    @Mapping(target = "answer", ignore = true)
    Question convertQuestionGetDTOtoEntity (QuestionGetDTO questionGetDTO);
}
