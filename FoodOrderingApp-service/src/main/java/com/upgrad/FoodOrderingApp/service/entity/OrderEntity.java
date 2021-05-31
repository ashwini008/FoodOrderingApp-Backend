package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

// Order Entity to map orders DB

@Entity
@Table(name = "orders")
@NamedQueries({
    @NamedQuery(name = "getOrdersByAddress", query = "SELECT o FROM OrderEntity o WHERE o.address.uuid = :addressUUID")
})
public class OrderEntity implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name="bill")
    @NotNull
    private double bill;

    @Column(name = "discount")
    private double discount;

    @Column(name = "date")
    @NotNull
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    @NotNull
    private AddressEntity address;

    public OrderEntity() { }

    public OrderEntity(@NotNull String uuid, @NotNull double bill, double discount, @NotNull Date date,
       @NotNull AddressEntity addressEntity) {
        this.uuid = uuid;
        this.bill = bill;
        this.discount = discount;
        this.date = date;
        this.address = addressEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }
}