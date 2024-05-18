package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ROOM")
@SequenceGenerator(name = "room_seq", sequenceName = "room_sequence", allocationSize = 1)
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Long roundAmount;

    @Column(nullable = false)
    private GameMode gameMode;

    @Column(nullable = false)
    private Long playerAmount;

    @Column(nullable = false)
    private String playerIds;

    @Column (nullable = true)
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public String getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(String playerIds) {
        this.playerIds = playerIds;
    }

    public String getReadyIds() {
        return readyIds;
    }

    public void setReadyIds(String readyIds) {
        this.readyIds = readyIds;
    }

}