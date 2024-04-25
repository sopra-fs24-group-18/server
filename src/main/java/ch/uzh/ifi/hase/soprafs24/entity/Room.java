package ch.uzh.ifi.hase.soprafs24.entity;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
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

    @Column
    private Long currentRound;

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

    public Long getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(Long currentRound) {
        this.currentRound = currentRound;
    }

    public String getReadyIds() {
        return readyIds;
    }

    public void setReadyIds(String readyIds) {
        this.readyIds = readyIds;
    }


}