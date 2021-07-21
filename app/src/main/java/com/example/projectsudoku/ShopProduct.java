package com.example.projectsudoku;

public class ShopProduct {

    protected String name;
    protected String desc;
    protected int price;

    public ShopProduct(String name, String desc, int price) {
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ShopProduct{" +
                "name='" + this.name + '\'' +
                ", desc='" + this.desc + '\'' +
                ", price=" + this.price +
                '}';
    }
}
