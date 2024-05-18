package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void findByUsername_success() {
    // given
    User user = new User();
    user.setPassword("123");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);
    user.setToken("1");
    user.setCreationDate(LocalDate.now());

    entityManager.persist(user);
    entityManager.flush();

    // when
    User found = userRepository.findByUsername(user.getUsername());

    // then
    assertNotNull(found.getId());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getToken(), user.getToken());
    assertEquals(found.getStatus(), user.getStatus());
  }

    @Test
    public void findByToken_success() {
        // given
        User user = new User();
        user.setPassword("123");
        user.setUsername("firstname@lastname");
        user.setStatus(UserStatus.OFFLINE);
        user.setToken("666666");
        user.setCreationDate(LocalDate.now());

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByToken(user.getToken());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
    }

    @Test
    public void findAllByIdInOrderByScoreDesc_success() {
        // given
        User user1 = new User();
        user1.setPassword("123");
        user1.setUsername("username1");
        user1.setStatus(UserStatus.OFFLINE);
        user1.setToken("666666");
        user1.setCreationDate(LocalDate.now());
        user1.setScore(30L);

        entityManager.persist(user1);
        entityManager.flush();

        User user2 = new User();
        user2.setPassword("123");
        user2.setUsername("username2");
        user2.setStatus(UserStatus.OFFLINE);
        user2.setToken("111111");
        user2.setCreationDate(LocalDate.now());
        user2.setScore(100L);

        entityManager.persist(user2);
        entityManager.flush();

        // when
        List<Long> list = new ArrayList<>();
        list.add(user1.getId());
        list.add(user2.getId());
        List<User> found = userRepository.findAllByIdInOrderByScoreDesc(list);

        // then
        assertNotNull(found);
        assertEquals(found.size(), 2);
        assertEquals(found.get(0).getUsername(), "username2");
    }
}
