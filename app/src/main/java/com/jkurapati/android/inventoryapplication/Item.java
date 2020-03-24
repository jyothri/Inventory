package com.jkurapati.android.inventoryapplication;

import java.util.Objects;

public class Item {
    private int id;
    private String name;
    private int quantity;
    private String expirationDate;
    private String purchaseDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return id == item.id &&
                quantity == item.quantity &&
                name.equals(item.name) &&
                expirationDate.equals(item.expirationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, quantity, expirationDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
