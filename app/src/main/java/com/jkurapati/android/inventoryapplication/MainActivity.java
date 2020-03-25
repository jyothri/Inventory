package com.jkurapati.android.inventoryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jkurapati.android.inventoryapplication.db.dao.ItemRepository;
import com.jkurapati.android.inventoryapplication.helper.ItemsAdapter;
import com.jkurapati.android.inventoryapplication.model.ItemViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private ItemViewModel itemViewModel;
    private ItemRepository itemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        itemRepository = new ItemRepository(getApplication());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ItemEditorActivity.class);
            startActivity(intent);
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
        recyclerView = findViewById(R.id.items_recycler_view);
        // use a linear layout manager
        final ItemsAdapter itemsAdapter = new ItemsAdapter(this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        // Update the cached copy of the words in the adapter.
        itemViewModel.getItems().observe(this, (items) -> {
            if (items.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                findViewById(R.id.empty_view).setVisibility(View.GONE);
            }
            itemsAdapter.setItems(items);
        });
    }

    private void populateDatabase() {
        for (int i = 1; i < 11; i++) {
            Item item = new Item("name: " + i, i, "expiry date: " + i, "purchase date: " + i);
            itemRepository.insert(item);
        }
        Toast.makeText(this, "dummy items populated", Toast.LENGTH_LONG).show();
    }

    private void deleteAllItems() {
        itemRepository.deleteAll();
        Toast.makeText(this, "items deleted", Toast.LENGTH_LONG).show();
    }
}
