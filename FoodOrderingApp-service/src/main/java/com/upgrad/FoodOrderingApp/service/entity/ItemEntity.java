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

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private List<RestaurantEntity> restaurants;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private List<OrderEntity> orders;

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

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public List<RestaurantEntity> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantEntity> restaurants) {
        this.restaurants = restaurants;
    }
}
