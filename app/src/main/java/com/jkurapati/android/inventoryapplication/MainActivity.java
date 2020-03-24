package com.jkurapati.android.inventoryapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.jkurapati.android.inventoryapplication.db.InventoryDbHelper;
import com.jkurapati.android.inventoryapplication.db.ItemContract;
import com.jkurapati.android.inventoryapplication.helper.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private TextView emptyView;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;

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
        recyclerView = findViewById(R.id.items_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        List<Item> items = getItems();
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
                deleteAllItems();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase readableDatabase = inventoryDbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME, null);
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
            readableDatabase.close();
        }
        return items;
    }

    private void displayDatabaseInfo() {
        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase readableDatabase = inventoryDbHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery("SELECT * FROM " + ItemContract.ItemEntry.TABLE_NAME, null);
        try {
            int count = cursor.getCount();
            emptyView.setText(getString(R.string.rows_in_db, count));
        } finally {
            cursor.close();
            readableDatabase.close();
        }
    }

    private void populateDatabase() {
        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        for (int i = 1; i < 11; i++) {
            ContentValues values = new ContentValues();
            Item item = new Item("name: " + i, i, "expiry date: " + i, "purchase date: " + i);
            values.put(ItemContract.ItemEntry.COLUMN_NAME_NAME, item.getName());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY, item.getQuantity());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_PURCHASE_DATE, item.getPurchaseDate());
            values.put(ItemContract.ItemEntry.COLUMN_NAME_EXPIRATION_DATE, item.getExpirationDate());
            db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);
        }
        Toast.makeText(this, "dummy items populated", Toast.LENGTH_LONG).show();
    }

    private void deleteAllItems() {
        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(ItemContract.ItemEntry.TABLE_NAME, null, null);
        Toast.makeText(this, String.format(Locale.getDefault(), "items deleted: %d", rowsDeleted), Toast.LENGTH_LONG).show();
    }
}
