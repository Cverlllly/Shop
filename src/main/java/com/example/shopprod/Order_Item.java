package com.example.shopprod;

public class Order_Item {
    private Long order_item;
    private Order order;
    private Product prod;
    private Integer qty;

    public Order_Item(Long order_item, Order order, Product prod, Integer qty) {
        this.order_item = order_item;
        this.order = order;
        this.prod = prod;
        this.qty = qty;
    }

    public Order_Item() {
    }

    public Long getOrder_item() {
        return order_item;
    }

    public void setOrder_item(Long order_item) {
        this.order_item = order_item;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProd() {
        return prod;
    }

    public void setProd(Product prod) {
        this.prod = prod;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
