package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("itemRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAll();

    @Query("SELECT i FROM Item i WHERE i.imageURL <> 'default' AND i.imageURL <> ''")
    List<Item> findItemWithImage();

    List<Item> findAllByIdIn(List<Long> itemIds);
}