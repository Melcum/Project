package com.example.projectsudoku;

public class InventoryBackgroundItem {

    private int itemImage;
    private String itemName;
    private String itemDesc;

    public InventoryBackgroundItem(int itemImage, String itemName, String itemDesc) {
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
    }

    public int getItemImage() {
        return this.itemImage;
    }

    public void setItemImage(int itemImage) {
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    @Override
    public String toString() {
        return "InventoryBackgroundItem{" +
                "itemImage=" + this.itemImage +
                ", itemName='" + this.itemName + '\'' +
                ", itemDesc='" + this.itemDesc + '\'' +
                '}';
    }
}
