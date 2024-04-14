package ch.uzh.ifi.hase.soprafs24.entity;

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
@Table(name = "ITEM")
public class Item implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String itemTitle;

    @Column(nullable = false)
    private String imageURL;//default number "default"

    @Column(nullable = false)
    private float price;

    @Column(nullable = false)
    private float leftRange;

    @Column(nullable = false)
    private float rightRange;

    @Column(nullable = true)
    private String itemDesc;

    @Column(nullable = true)
    private String itemCat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemCat() {
        return itemCat;
    }

    public void setItemCat(String itemCat) {
        this.itemCat = itemCat;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getLeftRange() {
        return leftRange;
    }

    public void setLeftRange(float leftRange) {
        this.leftRange = leftRange;
    }

    public float getRightRange() {
        return rightRange;
    }

    public void setRightRange(float rightRange) {
        this.rightRange = rightRange;
    }






}
