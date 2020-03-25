package com.jkurapati.android.inventoryapplication.db.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jkurapati.android.inventoryapplication.Item;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Item.class}, version = 1, exportSchema = false)
public abstract class ItemRoomDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile ItemRoomDatabase INSTANCE;

    static ItemRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ItemRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ItemRoomDatabase.class, "item_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ItemDao itemDao();
}
