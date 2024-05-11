package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.Tool;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.room.RoomPostDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.RoomDTOMapper;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.UserDTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.RoomService;
import ch.uzh.ifi.hase.soprafs24.service.ToolService;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ToolController {

  private final ToolService toolService;

  ToolController(ToolService toolService) {
    this.toolService = toolService;
  }

  @GetMapping("/tools")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<Tool> getAllTools() {
      return toolService.getTools();
  }

  @PostMapping("/tools/{toolId}/{roomId}/{userId}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseEntity<?> useTool(@PathVariable Long toolId, @PathVariable Long roomId, @PathVariable Long userId){
      toolService.useTool(toolId, roomId, userId);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Buy and use the tool successfully!");
  }

    @GetMapping("/tools/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getUserTools(@PathVariable Long userId) {
        return toolService.getUserTools(userId);
    }
}
