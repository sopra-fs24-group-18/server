package ch.uzh.ifi.hase.soprafs24.rest.dto.room;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

public class RoomGetDTO {
    private Long id;

    private String roomCode;

    private String name;

    private String ownerName;

    private Long roundAmount;

    private GameMode gameMode;

    private Long playerAmount;

    private String playerNames;

    private String readyIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Long getRoundAmount() {
        return roundAmount;
    }

    public void setRoundAmount(Long roundAmount) {
        this.roundAmount = roundAmount;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Long getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(Long playerAmount) {
        this.playerAmount = playerAmount;
    }

    public String getPlayerNames() {
        return playerNames;
    }

    public void setPlayerNames(String playerNames) {
        this.playerNames = playerNames;
    }

    public String getReadyIds() {
        return readyIds;
    }

    public void setReadyIds(String readyIds) {
        this.readyIds = readyIds;
    }
}
