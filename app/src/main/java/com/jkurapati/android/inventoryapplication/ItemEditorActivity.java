package com.jkurapati.android.inventoryapplication;

import android.content.ContentValues;
import android.database.Cursor;
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
        Uri uri = getIntent().getData();
        if (uri != null) {
            populateExistingItem(uri);
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
            return saveItem();
        }
        // If we got here, the user's action was not recognized.
        // Invoke the superclass to handle it.
        return super.onOptionsItemSelected(item);
    }

    private void populateExistingItem(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null);
        final int nameIdx = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_NAME);
        final int quantityIdx = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY);
        final int PurchaseDateIdx = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_PURCHASE_DATE);
        final int ExpirationDateIdx = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_EXPIRATION_DATE);
        if (cursor.moveToNext()) {
            EditText name = findViewById(R.id.edit_item_name_value);
            name.setText(cursor.getString(nameIdx));
            EditText quantity = findViewById(R.id.edit_item_quantity_value);
            quantity.setText(cursor.getString(quantityIdx));
            EditText purchaseDate = findViewById(R.id.edit_item_purchaseDate_value);
            purchaseDate.setText(cursor.getString(PurchaseDateIdx));
            EditText expiryDate = findViewById(R.id.edit_item_expirationDate_value);
            expiryDate.setText(cursor.getString(ExpirationDateIdx));
        }
    }

    private boolean saveItem() {
        String name = extractFieldValue(R.id.edit_item_name_value);
        int quantity = Integer.parseInt(extractFieldValue(R.id.edit_item_quantity_value));
        String purchaseDate = extractFieldValue(R.id.edit_item_purchaseDate_value);
        String expirationDate = extractFieldValue(R.id.edit_item_expirationDate_value);

        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_NAME_NAME, name);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY, quantity);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_PURCHASE_DATE, purchaseDate);
        values.put(ItemContract.ItemEntry.COLUMN_NAME_EXPIRATION_DATE, expirationDate);

        Uri uri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);

        if (uri == null) {
            Toast.makeText(this, "Could not save item", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, String.format(Locale.getDefault(), "item saved to db %s", uri), Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private String extractFieldValue(int id) {
        EditText widgetText = findViewById(id);
        return widgetText.getText().toString().trim();
    }
}
