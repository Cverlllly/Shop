package com.example.shopprod;

public class Order {
    private Long order_id;
    private String date_created;
    private Integer confirmation_number;
    private Double amount;
    private Boolean done;
    private MainPageController.User user;

    public Order(Long order_id, String date_created, Integer confirmation_number, Double amount, Boolean done, MainPageController.User user) {
        this.order_id = order_id;
        this.date_created = date_created;
        this.confirmation_number = confirmation_number;
        this.amount = amount;
        this.done = done;
        this.user = user;
    }

    public Order() {

    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public Integer getConfirmation_number() {
        return confirmation_number;
    }

    public void setConfirmation_number(Integer confirmation_number) {
        this.confirmation_number = confirmation_number;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public MainPageController.User getUser() {
        return user;
    }

    public void setUser(MainPageController.User user) {
        this.user = user;
    }
}
