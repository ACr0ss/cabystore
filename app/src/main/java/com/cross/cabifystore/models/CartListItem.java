package com.cross.cabifystore.models;

public class CartListItem {
    String itemName;
    String quantity;
    String totalPrice;

    public CartListItem(String itemName, String quantity, String totalPrice) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
