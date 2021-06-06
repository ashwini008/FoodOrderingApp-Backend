package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "item")
@NamedNativeQueries({
    @NamedNativeQuery(
        name = "topFiveItemsByRestaurant",
        query = "select * from item where id in "
            + "(select item_id from order_item where order_id in "
            + "(select id from orders where restaurant_id = ? ) "
            + "group by order_item.item_id order by (count(order_item.order_id)) desc LIMIT 5)",
        resultClass = ItemEntity.class)
})
@NamedQueries({
    @NamedQuery(
        name = "getItemByUUID",
        query = "select i from ItemEntity i where i.uuid= :uuid"
    )
})
public class ItemEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 200)
    @Column(name = "uuid", unique = true)
    private String uuid;

    @NotNull
    @Size(max = 30)
    @Column(name = "item_name")
    private String itemName;

    @NotNull
    @Column(name = "price")
    private Integer price;

    @NotNull
    @Size(max = 10)
    @Column(name = "type")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
