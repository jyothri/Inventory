package com.jkurapati.android.inventoryapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jkurapati.android.inventoryapplication.db.ItemContract;
import com.jkurapati.android.inventoryapplication.helper.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ItemEditorActivity.class);
                startActivity(intent);
            }
        });
        setupRecyclerView();

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
                deleteAllItems();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void setupRecyclerView() {
        List<Item> items = getItemsFromProvider();
        recyclerView = findViewById(R.id.items_recycler_view);
        if (items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            TextView emptyView = findViewById(R.id.empty_view);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            // use a linear layout manager
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            RecyclerView.Adapter itemsAdapter = new ItemsAdapter(items);
            recyclerView.setAdapter(itemsAdapter);
        }
    }

    private List<Item> getItemsFromProvider() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ItemContract.ItemEntry.CONTENT_URI, null, null, null);
        try {
            final int nameIdx = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_NAME);
            final String quantityIdx = ItemContract.ItemEntry.COLUMN_NAME_QUANTITY;
            final String PurchaseDateIdx = ItemContract.ItemEntry.COLUMN_NAME_PURCHASE_DATE;
            final String ExpirationDateIdx = ItemContract.ItemEntry.COLUMN_NAME_EXPIRATION_DATE;
            while (cursor.moveToNext()) {
                items.add(new Item(cursor.getString(nameIdx),
                        cursor.getInt(cursor.getColumnIndex(quantityIdx)),
                        cursor.getString(cursor.getColumnIndex(PurchaseDateIdx)),
                        cursor.getString(cursor.getColumnIndex(ExpirationDateIdx)
                        )));
            }
        } finally {
            cursor.close();
        }
        return items;
    }

    private void populateDatabase() {
        for (int i = 1; i < 11; i++) {
            ContentValues values = new ContentValues();
            Item item = new Item("name: " + i, i, "expiry date: " + i, "purchase date: " + i);
            values.put(ItemContract.ItemEntry.COLUMN_NAME_NAME, item.getName());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY, item.getQuantity());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_PURCHASE_DATE, item.getPurchaseDate());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_EXPIRATION_DATE, item.getExpirationDate());
            getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);
        }
        Toast.makeText(this, "dummy items populated", Toast.LENGTH_LONG).show();
    }

    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(ItemContract.ItemEntry.CONTENT_URI, null, null);
        Toast.makeText(this, String.format(Locale.getDefault(), "items deleted: %d", rowsDeleted), Toast.LENGTH_LONG).show();
    }
}
