package com.jkurapati.android.inventoryapplication;

import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.jkurapati.android.inventoryapplication.db.dao.LocalDateConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

@Entity(tableName = Item.TABLE_NAME)
@TypeConverters(LocalDateConverter.class)
public class Item {

    /**
     * The name of the Cheese table.
     */
    public static final String TABLE_NAME = "item_table";

    /**
     * The name of the ID column.
     */
    public static final String COLUMN_ID = BaseColumns._ID;

    /**
     * The name of the name column.
     */
    static final String COLUMN_NAME = "name";
    static final String COLUMN_QUANTITY = "quantity";
    static final String COLUMN_PURCHASE_DATE = "purchaseDate";

    public static final String COLUMN_EXPIRATION_DATE = "expirationDate";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long id;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @ColumnInfo(name = COLUMN_QUANTITY)
    private int quantity;

    @ColumnInfo(name = COLUMN_EXPIRATION_DATE)
    private LocalDate expirationDate;

    @ColumnInfo(name = COLUMN_PURCHASE_DATE)
    private LocalDate purchaseDate;

    @Ignore
    public Item(String name, int quantity, LocalDate expirationDate, LocalDate purchaseDate) {
        this.name = name;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
    }

    public Item() {

    }

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
