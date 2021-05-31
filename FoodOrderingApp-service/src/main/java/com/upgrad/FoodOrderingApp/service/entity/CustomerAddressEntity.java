package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

// Customer address entity to map to customer_address DB

@Entity
@Table(name = "customer_address")
@NamedQueries({
    @NamedQuery(name = "customerAddress", query = "select ca from CustomerAddressEntity ca where ca.customer.id = :customerId order by ca.address.id desc"),
    @NamedQuery(name = "customerAddByIDs", query = "select ca from CustomerAddressEntity ca where ca.customer.id = :customerId and ca.address.id = :addressId")
})
public class CustomerAddressEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

}
