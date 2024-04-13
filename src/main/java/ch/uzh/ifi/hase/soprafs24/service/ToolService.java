package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.ToolType;
import ch.uzh.ifi.hase.soprafs24.entity.Tool;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.ToolRepository;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ToolService {

    private final Logger log = LoggerFactory.getLogger(ToolService.class);

    private final ToolRepository toolRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    @Autowired
    public ToolService(@Qualifier("toolRepository") ToolRepository toolRepository, UserRepository userRepository, UserService userService) {
        this.toolRepository = toolRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<Tool> getTools() {
        return this.toolRepository.findAll();
    }

    public void useTool(Long toolId, Long userId) {
        Optional<User> optionalUser = userService.getUserById(userId);
        User user = optionalUser.get();

        Optional<Tool> optionalTool = toolRepository.findById(toolId);
        Tool tool = optionalTool.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tool not found"));

        if (user.getScore() < tool.getPrice()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have enough points to buy the tool!");
        }
        user.setScore(user.getScore() - tool.getPrice());

        switch (tool.getType()) {
            case HINT:
                String addTool = user.getToolStatus() == null ? ToolType.HINT.name() : user.getToolStatus() + "," + ToolType.HINT;
                user.setToolStatus(addTool);
                userRepository.save(user);
                break;
            case BLUR:
                List<User> players = userService.getUsers().stream()
                        .filter(player -> !player.getId().equals(userId))
                        .collect(Collectors.toList());
                for (User player : players) {
                    String add = player.getToolStatus() == null ? ToolType.BLUR.name() : player.getToolStatus() + "," + ToolType.BLUR;
                    player.setToolStatus(add);
                }
                userRepository.saveAll(players);
        }
    }
}
