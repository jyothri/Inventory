package com.jkurapati.android.inventoryapplication;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jkurapati.android.inventoryapplication.db.InventoryDbHelper;
import com.jkurapati.android.inventoryapplication.db.ItemContract;

import java.util.Locale;

public class ItemEditorActivity extends AppCompatActivity {
    private static final String TAG = ItemEditorActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Toolbar myToolbar = findViewById(R.id.editor_toolbar);
        setSupportActionBar(myToolbar);
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
            return saveItem();
        }
        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    private boolean saveItem() {
        String name = extractFieldValue(R.id.edit_item_name_value);
        int quantity = Integer.parseInt(extractFieldValue(R.id.edit_item_quantity_value));
        String purchaseDate = extractFieldValue(R.id.edit_item_purchaseDate_value);
        String expirationDate = extractFieldValue(R.id.edit_item_expirationDate_value);
        Item itemToSave = new Item(name, quantity, purchaseDate, expirationDate);

        InventoryDbHelper inventoryDbHelper = new InventoryDbHelper(this);
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_NAME_NAME, itemToSave.getName());
        values.put(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY, itemToSave.getQuantity());
        values.put(ItemContract.ItemEntry.COLUMN_NAME_PURCHASE_DATE, itemToSave.getPurchaseDate());
        values.put(ItemContract.ItemEntry.COLUMN_NAME_EXPIRATION_DATE, itemToSave.getExpirationDate());
        long id = db.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);

        if (id < 0) {
            Toast.makeText(this, "Could not save item", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, String.format(Locale.getDefault(), "item saved to db %d", id), Toast.LENGTH_LONG).show();
            return true;
        }
    }


    private String extractFieldValue(int id) {
        EditText widgetText = findViewById(id);
        return widgetText.getText().toString().trim();
    }
}
