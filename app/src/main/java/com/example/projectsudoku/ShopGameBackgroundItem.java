package com.example.projectsudoku;


public class ShopGameBackgroundItem extends ShopProduct{

    private int itemImage;

    public ShopGameBackgroundItem(int itemImage, String itemName, String itemDesc, int itemPrice) {
        super(itemName, itemDesc, itemPrice);
        this.itemImage = itemImage;
    }

    public int getItemImage() {
        return this.itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }


    @Override
    public String toString() {
        return "ShopGameBackgroundItem{" +
                "itemImage=" + itemImage +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", price=" + price +
                '}';
    }

}
