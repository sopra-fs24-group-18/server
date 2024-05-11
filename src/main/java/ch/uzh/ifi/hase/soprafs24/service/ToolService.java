package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import ch.uzh.ifi.hase.soprafs24.entity.Tool;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.RoomRepository;
import ch.uzh.ifi.hase.soprafs24.repository.ToolRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ToolService {

    private final Logger log = LoggerFactory.getLogger(ToolService.class);

    private final ToolRepository toolRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final UserService userService;

    @Autowired
    public ToolService(@Qualifier("toolRepository") ToolRepository toolRepository, UserRepository userRepository,
                       RoomRepository roomRepository, UserService userService) {
        this.toolRepository = toolRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.userService = userService;
    }

    public List<Tool> getTools() {
        return this.toolRepository.findAll();
    }

    public void useTool(Long toolId, Long roomId, Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);
        User user = optionalUser.get();

        Optional<Tool> optionalTool = toolRepository.findById(toolId);
        Tool tool = optionalTool.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tool not found"));

        if (user.getScore() < tool.getPrice()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have enough points to buy the tool!");
        }
        user.setScore(user.getScore() - tool.getPrice());

        String toolList = user.getToolList() == null ? tool.getType().name() : user.getToolList() + "," + tool.getType();
        user.setToolList(toolList);

        String toolStatus = user.getToolStatus();

        switch (tool.getType()) {
            case HINT:
            case Defense:
            case Boost:
            case Gamble:
                toolStatus = toolStatus == null ? ToolType.HINT.name() : user.getToolStatus() + "," + ToolType.HINT;
                user.setToolStatus(toolStatus);
                break;

            case BLUR:
                // Find the room containing the current user
                Optional<Room> optionalRoom = roomRepository.findById(roomId);
                Room room = optionalRoom.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

                // Get player IDs in the room
                String[] playerIds = room.getPlayerIds().split(",");

                // Update tool status for other players in the room
                for (String playerId : playerIds) {
                    if (!playerId.equals(userId.toString())) {
                        Optional<User> optionalPlayer = userService.getUserById(Long.parseLong(playerId));
                        optionalPlayer.ifPresent(player -> {
                            String blurToolStatus = player.getToolStatus() == null ? ToolType.BLUR.name() : player.getToolStatus() + "," + ToolType.BLUR;
                            player.setToolStatus(blurToolStatus);
                            userRepository.save(player);
                        });
                    }
                }
                break;
        }
        userRepository.save(user);
    }

    public List<String> getUserTools(Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);
        User user = optionalUser.get();
        String toolList = user.getToolList();
        if(toolList != null && !toolList.isBlank()){
            String[] tools = toolList.split(",");
            return Arrays.asList(tools);
        }
        return null;
    }
}
