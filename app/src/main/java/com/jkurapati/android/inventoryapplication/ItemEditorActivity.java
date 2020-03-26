package com.jkurapati.android.inventoryapplication;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jkurapati.android.inventoryapplication.db.dao.ItemRepository;

import java.time.LocalDate;
import java.util.Locale;

import static com.jkurapati.android.inventoryapplication.Item.DATE_FORMATTER;

public class ItemEditorActivity extends AppCompatActivity {
    private static final String TAG = ItemEditorActivity.class.getSimpleName();
    private ItemRepository itemRepository;
    private boolean isExistingItem;
    private long existingItemId;
    private LocalDate purchaseDate;
    private LocalDate expirationDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemRepository = new ItemRepository(getApplication());
        isExistingItem = false;
        purchaseDate = LocalDate.now();
        expirationDate = LocalDate.now();
        setContentView(R.layout.activity_editor);
        Toolbar myToolbar = findViewById(R.id.editor_toolbar);
        setSupportActionBar(myToolbar);
        Uri uri = getIntent().getData();
        if (uri != null) {
            isExistingItem = true;
            existingItemId = ContentUris.parseId(uri);
            populateExistingItem();
        }
        setupListeners();
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
            setFieldValue(R.id.edit_item_purchaseDate_value, item.getPurchaseDate().format(DATE_FORMATTER));
            setFieldValue(R.id.edit_item_expirationDate_value, item.getExpirationDate().format(DATE_FORMATTER));
            expirationDate = item.getExpirationDate();
            purchaseDate = item.getPurchaseDate();
        });

    }

    private boolean saveOrUpdateItem() {
        String name = getFieldValue(R.id.edit_item_name_value);
        int quantity = Integer.parseInt(getFieldValue(R.id.edit_item_quantity_value));
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

    private void setupListeners() {
        datePickerDialog(findViewById(R.id.edit_item_expirationDate_value));
        datePickerDialog(findViewById(R.id.edit_item_purchaseDate_value));
    }

    private void datePickerDialog(TextView editText) {
        editText.setInputType(InputType.TYPE_NULL);
        editText.setOnClickListener(v -> {
            LocalDate localDate;
            switch (editText.getId()) {
                case R.id.edit_item_expirationDate_value:
                    localDate = expirationDate;
                    break;
                case R.id.edit_item_purchaseDate_value:
                    localDate = purchaseDate;
                    break;
                default:
                    throw new IllegalStateException("No implementation for field to set local date.");
            }
            int day = localDate.getDayOfMonth();
            int month = localDate.getMonthValue() - 1;
            int year = localDate.getYear();
            // date picker dialog
            DatePickerDialog picker = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> setupDateDialog(editText, year1, monthOfYear, dayOfMonth), year, month, day);
            picker.show();
        });
    }

    private void setupDateDialog(TextView editText, int year1, int monthOfYear, int dayOfMonth) {
        final LocalDate vlocalDate = LocalDate.of(year1, monthOfYear + 1, dayOfMonth);
        String formattedDate = vlocalDate.format(DATE_FORMATTER);
        editText.setText(formattedDate);
        switch (editText.getId()) {
            case R.id.edit_item_expirationDate_value:
                expirationDate = vlocalDate;
                break;
            case R.id.edit_item_purchaseDate_value:
                purchaseDate = vlocalDate;
                break;
            default:
                throw new IllegalStateException("No implementation for field to set local date.");
        }
    }

    private String getFieldValue(int id) {
        TextView widgetText = findViewById(id);
        return widgetText.getText().toString().trim();
    }

    private void setFieldValue(int id, String valueToSet) {
        TextView widgetText = findViewById(id);
        widgetText.setText(valueToSet);
    }
}
