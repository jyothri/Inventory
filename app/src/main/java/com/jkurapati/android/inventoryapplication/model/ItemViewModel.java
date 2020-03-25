package com.jkurapati.android.inventoryapplication.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.jkurapati.android.inventoryapplication.Item;
import com.jkurapati.android.inventoryapplication.db.dao.ItemRepository;

import java.util.List;

public class ItemViewModel extends AndroidViewModel {
    private final ItemRepository itemRepository;
    private LiveData<List<Item>> items;
    private Application application;

    public ItemViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        itemRepository = new ItemRepository(application);
    }

    public LiveData<List<Item>> getItems() {
        if (items == null) {
            items = itemRepository.getAllItems();
        }
        return items;
    }
}
