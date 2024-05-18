package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.GameMode;
import ch.uzh.ifi.hase.soprafs24.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RoomRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private RoomRepository roomRepository;

  @Test
  public void findByRoomCode_success() {
    // given
    Room room = new Room();
    room.setRoomCode("123456");
    room.setName("testRoom");
    room.setGameMode(GameMode.BUDGET);
    room.setPlayerAmount(5L);
    room.setOwnerId(4L);
    room.setRoundAmount(6L);
    room.setPlayerIds("4,1");

    entityManager.persist(room);
    entityManager.flush();

    // when
    Room found = roomRepository.findByRoomCode(room.getRoomCode());

    // then
    assertNotNull(found.getId());
    assertEquals(found.getName(), room.getName());
    assertEquals(found.getGameMode(), room.getGameMode());
    assertEquals(found.getPlayerAmount(), room.getPlayerAmount());
    assertEquals(found.getOwnerId(), room.getOwnerId());
  }
}
