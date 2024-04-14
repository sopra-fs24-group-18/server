package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("itemRepository")
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Add custom query methods if needed
    @Query("SELECT i.id FROM Item i WHERE i.imageURL <> 'default' ORDER BY RAND() LIMIT 1")
    Long findIdByNonDefaultImage_guess();

    @Query("SELECT i.id FROM Item i WHERE i.imageURL <> 'default' ORDER BY RAND() LIMIT 9")
    List<Long> findIdsByNonDefaultImage_budget();
}