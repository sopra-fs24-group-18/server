package ch.uzh.ifi.hase.soprafs24.entity;

import javax.persistence.*;
import java.io.Serializable;

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
