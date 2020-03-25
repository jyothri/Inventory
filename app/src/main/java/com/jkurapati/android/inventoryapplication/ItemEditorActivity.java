package com.jkurapati.android.inventoryapplication;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jkurapati.android.inventoryapplication.db.dao.ItemRepository;

import java.util.Locale;

public class ItemEditorActivity extends AppCompatActivity {
    private static final String TAG = ItemEditorActivity.class.getSimpleName();
    private ItemRepository itemRepository;
    private boolean isExistingItem;
    private long existingItemId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemRepository = new ItemRepository(getApplication());
        isExistingItem = false;
        setContentView(R.layout.activity_editor);
        Toolbar myToolbar = findViewById(R.id.editor_toolbar);
        setSupportActionBar(myToolbar);
        Uri uri = getIntent().getData();
        if (uri != null) {
            isExistingItem = true;
            existingItemId = ContentUris.parseId(uri);
            populateExistingItem();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            Log.d(TAG, "save item to DB");
            if (saveOrUpdateItem()) {
                finish();
            }
            return true;
        }
        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    private void populateExistingItem() {
        itemRepository.getItemById(existingItemId).observe(this, item -> {
            setFieldValue(R.id.edit_item_name_value, item.getName());
            setFieldValue(R.id.edit_item_quantity_value, String.valueOf(item.getQuantity()));
            setFieldValue(R.id.edit_item_purchaseDate_value, item.getPurchaseDate());
            setFieldValue(R.id.edit_item_expirationDate_value, item.getExpirationDate());
        });
    }

    private boolean saveOrUpdateItem() {
        String name = getFieldValue(R.id.edit_item_name_value);
        int quantity = Integer.parseInt(getFieldValue(R.id.edit_item_quantity_value));
        String purchaseDate = getFieldValue(R.id.edit_item_purchaseDate_value);
        String expirationDate = getFieldValue(R.id.edit_item_expirationDate_value);
        Item item = new Item(name, quantity, expirationDate, purchaseDate);
        if (!isExistingItem) {
            itemRepository.insert(item);
            Toast.makeText(this, String.format(Locale.getDefault(), "item saved to db: %s", item.getId()), Toast.LENGTH_LONG).show();
            return true;
        } else {
            item.setId(existingItemId);
            itemRepository.updateItem(item);
            Toast.makeText(this, String.format(Locale.getDefault(), "item updated to db: %s", item.getId()), Toast.LENGTH_LONG).show();
            return true;
        }

    }

    private String getFieldValue(int id) {
        EditText widgetText = findViewById(id);
        return widgetText.getText().toString().trim();
    }

    private void setFieldValue(int id, String valueToSet) {
        EditText widgetText = findViewById(id);
        widgetText.setText(valueToSet);
    }
}
