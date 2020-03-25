package com.jkurapati.android.inventoryapplication.db.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jkurapati.android.inventoryapplication.Item;

import java.util.List;

public class ItemRepository {
    private ItemDao itemDao;
    private LiveData<List<Item>> mAllItems;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public ItemRepository(Application application) {
        ItemRoomDatabase db = ItemRoomDatabase.getDatabase(application);
        itemDao = db.itemDao();
        mAllItems = itemDao.getItems();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Item>> getAllItems() {
        return mAllItems;
    }

    public LiveData<Item> getItemById(long id) {
        MutableLiveData<Item> itemLiveData = new MutableLiveData<>();
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemLiveData.postValue(itemDao.selectById(id)));
        return itemLiveData;
    }

    public void insert(Item item) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.insert(item));
    }

    public void updateItem(Item item) {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.updateItems(item));
    }

    public void deleteAll() {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.deleteAll());
    }
}
