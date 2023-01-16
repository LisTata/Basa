package com.company;

import java.util.HashSet;
import java.util.Set;

public class Order {

    private Long id;
    private String orderDate;
    private String deliveryDate;
    private String status;
    private Customer customer;
    Set<Product> products = new HashSet<>();

    public Order(Long id, String orderDate, String deliveryDate, String status) {

        this.id
                = id;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
        this.status = status;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Set<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", deliveryDate=" + deliveryDate +
                ", status='" + status + '\'' +
                ", customer=" + customer +
                ", products=" + products +
                '}';
    }
}

