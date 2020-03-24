package com.jkurapati.android.inventoryapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jkurapati.android.inventoryapplication.db.InventoryDbHelper;
import com.jkurapati.android.inventoryapplication.db.ItemContract;
import com.jkurapati.android.inventoryapplication.helper.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView emptyView;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.items_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        List<Item> items = getItems();// new ArrayList<>();
        if (items.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        itemsAdapter = new ItemsAdapter(items);
        recyclerView.setAdapter(itemsAdapter);
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_populate_db:
                Log.d(TAG, "Populating DB with dummy values");
                populateDatabase();
                return true;

            case R.id.action_delete_rows:
                Log.d(TAG, "Delete all rows from DB.");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            Item item = new Item();
            item.setName("name: " + i);
            item.setQuantity(i);
            item.setExpirationDate("expiry: " + i);
            item.setPurchaseDate("purchase: " + i);
            items.add(item);
        }
        return items;
    }

    private void displayDatabaseInfo() {
        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase readableDatabase = inventoryDbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME, null);
        try {
            int count = cursor.getCount();
            emptyView.setText("No. of rows in the DB: " + count);
        } finally {
            cursor.close();
            readableDatabase.close();
        }
    }

    private void populateDatabase() {
        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        for (Item item : getItems()) {
            ContentValues values = new ContentValues();
            values.put(ItemContract.ItemEntry.COLUMN_NAME_NAME, item.getName());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY, item.getQuantity());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_PURCHASE_DATE, item.getPurchaseDate());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_EXPIRATION_DATE, item.getExpirationDate());
            db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);
        }
    }
}
