package com.example.shopprod;

public class Product {
    private Long product_id;
    private String name;
    private Double price;
    private String serial_number;
    private String Description;
    private String qty;
    private String lastupdate;
    private byte[] img;

    public Product() {
    }

    public Product(Long product_id, String name, Double price, String serial_number, String description, String qty, String lastupdate, byte[] img) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.serial_number = serial_number;
        this.Description = description;
        this.qty = qty;
        this.lastupdate = lastupdate;
        this.img = img;
    }

    public Product(String name, Double price, byte[] img,String qty) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.qty=qty;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }
}
