package com.jkurapati.android.inventoryapplication.db.dao;

import android.app.Application;
import android.text.TextUtils;

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
        validateItem(item);
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.insert(item));
    }

    public void updateItem(Item item) {
        validateItem(item);
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.updateItems(item));
    }

    public void deleteAll() {
        ItemRoomDatabase.databaseWriteExecutor.execute(() -> itemDao.deleteAll());
    }

    private void validateItem(Item item) {
        if (TextUtils.isEmpty(item.getName())) {
            throw new IllegalArgumentException("item name cannot be blank.");
        }
        int quantity = item.getQuantity();
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity should be a positive integer.");
        }
        // if purchaseDate is present, validate that it is not after expiry date.
        // if purchaseDate is present, validate that it is not after current date.
        if (item.getExpirationDate().isBefore(item.getPurchaseDate())) {
            throw new IllegalArgumentException("Purchase date cannot be before Expiration Date.");
        }

//        if (item.getExpirationDate().isBefore(LocalDate.now())) {
//            throw new IllegalArgumentException("Item already expired.");
//        }
    }
}
