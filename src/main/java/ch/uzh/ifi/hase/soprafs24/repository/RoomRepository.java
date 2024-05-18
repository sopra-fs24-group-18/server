package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("roomRepository")
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByRoomCode(String roomCode);

    void deleteById(Long id);
}
