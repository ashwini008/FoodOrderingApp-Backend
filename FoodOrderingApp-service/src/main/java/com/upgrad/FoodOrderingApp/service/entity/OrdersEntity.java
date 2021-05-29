package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

//this class represents the orders table in the db
@Entity
@Table(name = "orders", uniqueConstraints = {@UniqueConstraint(columnNames = {"uuid"})})
@NamedQueries({
        @NamedQuery(name = "getOrdersByCustomers", query = "SELECT o FROM OrdersEntity o WHERE o.customer = :customer ORDER BY o.date DESC "),
        @NamedQuery(name = "getOrdersByRestaurant",query = "SELECT o FROM OrdersEntity o WHERE o.restaurant = :restaurant"),
        @NamedQuery(name = "getOrdersByAddress",query = "SELECT o FROM OrdersEntity o WHERE o.address = :address")
})

public class OrdersEntity implements Serializable {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    @Column(name = "discount")
    private double discount;

    @Column(name = "date")
    @NotNull
    private Timestamp date;

    @ManyToOne(fetch =FetchType.EAGER)
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    //waiting for customerEntity,AddressEntity, Resturant Entity
/*
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @NotNull
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @NotNull
    private AddressEntity address;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    private RestaurantEntity restaurant;
*/

    public OrdersEntity() {

    }

    public OrdersEntity(String uuid, double bill, CouponEntity coupon, double discount, Timestamp date, PaymentEntity payment) {
        this.uuid = uuid;
        this.bill = bill;
        this.coupon = coupon;
        this.discount = discount;
        this.date = date;
        this.payment = payment;
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

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }
}
