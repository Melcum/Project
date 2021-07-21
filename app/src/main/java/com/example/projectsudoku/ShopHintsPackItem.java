package com.example.projectsudoku;

public class ShopHintsPackItem extends ShopProduct{

    private int hintsNumber;

    public ShopHintsPackItem(String name, String desc, int price, int hintsNumber) {
        super(name, desc, price);
        this.hintsNumber = hintsNumber;
    }

    public int getHintsNumber() {
        return this.hintsNumber;
    }

    public void setHintsNumber(int hintsNumber) {
        this.hintsNumber = hintsNumber;
    }

    @Override
    public String toString() {
        return "ShopHintsPackItem{" +
                "hintsNumber=" + this.hintsNumber +
                ", name='" + this.name + '\'' +
                ", desc='" + this.desc + '\'' +
                ", price=" + this.price +
                '}';
    }
}
