package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findAll_success() {
        // given
        Item item1 = new Item();
        item1.setItemTitle("Item 1");
        item1.setImageURL("url1");
        item1.setPrice(10.0f);
        entityManager.persist(item1);

        Item item2 = new Item();
        item2.setItemTitle("Item 2");
        item2.setImageURL("url2");
        item2.setPrice(20.0f);
        entityManager.persist(item2);

        entityManager.flush();

        // when
        List<Item> items = itemRepository.findAll();

        // then
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getItemTitle());
        assertEquals("Item 2", items.get(1).getItemTitle());
    }

    @Test
    public void findAllByIdIn_success() {
        // given
        Item item1 = new Item();
        item1.setItemTitle("Item 1");
        item1.setImageURL("url1");
        item1.setPrice(10.0f);
        entityManager.persist(item1);

        Item item2 = new Item();
        item2.setItemTitle("Item 2");
        item2.setImageURL("url2");
        item2.setPrice(20.0f);
        entityManager.persist(item2);

        entityManager.flush();

        // when
        List<Item> items = itemRepository.findAllByIdIn(Arrays.asList(item1.getId(), item2.getId()));

        // then
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Item 1", items.get(0).getItemTitle());
        assertEquals("Item 2", items.get(1).getItemTitle());
    }
    @Test
    public void findItemWithImage_success() {
        // given
        Item item1 = new Item();
        item1.setItemTitle("Item 1");
        item1.setImageURL("");
        item1.setPrice(10.0f);
        entityManager.persist(item1);

        Item item2 = new Item();
        item2.setItemTitle("Item 2");
        item2.setImageURL("url2");
        item2.setPrice(20.0f);
        entityManager.persist(item2);

        Item item3 = new Item();
        item3.setItemTitle("Item 3");
        item3.setImageURL("default");
        item3.setPrice(20.0f);
        entityManager.persist(item3);

        entityManager.flush();

        // when
        List<Item> items = itemRepository.findItemWithImage();

        // then
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("Item 2", items.get(0).getItemTitle());
    }
}