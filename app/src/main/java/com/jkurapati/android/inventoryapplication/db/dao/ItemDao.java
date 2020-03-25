package com.jkurapati.android.inventoryapplication.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jkurapati.android.inventoryapplication.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(Item item);

    @Update
    void updateItems(Item... items);

    @Query("DELETE FROM " + Item.TABLE_NAME)
    void deleteAll();

    @Query("SELECT * from " + Item.TABLE_NAME + " ORDER BY " + Item.COLUMN_EXPIRATION_DATE + " ASC")
    LiveData<List<Item>> getItems();

    /**
     * Select an item by the ID.
     *
     * @param id The row ID.
     */
    @Query("SELECT * FROM " + Item.TABLE_NAME + " WHERE " + Item.COLUMN_ID + " = :id")
    Item selectById(long id);

}
