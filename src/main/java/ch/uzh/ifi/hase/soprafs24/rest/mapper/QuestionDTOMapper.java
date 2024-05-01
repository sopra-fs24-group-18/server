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
    @Mapping(source = "leftRange",target = "leftRange")
    @Mapping(source = "rightRange",target = "rightRange")
    @Mapping(source = "originLeftRange",target = "originLeftRange")
    @Mapping(source = "originRightRange",target = "originRightRange")
    @Mapping(source = "budget",target = "budget")
    @Mapping(source = "blur",target = "blur")
    QuestionGetDTO convertEntitytoQuestionGetDTO(Question question);

}
