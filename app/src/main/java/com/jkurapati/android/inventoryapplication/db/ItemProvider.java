package com.jkurapati.android.inventoryapplication.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.jkurapati.android.inventoryapplication.db.ItemContract.ITEMS;
import static com.jkurapati.android.inventoryapplication.db.ItemContract.ITEM_ID;
import static com.jkurapati.android.inventoryapplication.db.ItemContract.ItemEntry.TABLE_NAME;
import static com.jkurapati.android.inventoryapplication.db.ItemContract.uriMatcher;

public class ItemProvider extends ContentProvider {
    private static final String LOG_TAG = ItemProvider.class.getSimpleName();
    private InventoryDbHelper inventoryDbHelper;

    @Override
    public boolean onCreate() {
        inventoryDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.i(LOG_TAG, "Reading from ItemProvider");
        SQLiteDatabase db = inventoryDbHelper.getReadableDatabase();
        int match = ItemContract.uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return db.query(TABLE_NAME, projection, null, null, null, null, sortOrder);
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Cannot query. Unknown Uri " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return ItemContract.ItemEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return ItemContract.ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = ItemContract.uriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertItem(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        long id = db.insert(TABLE_NAME, null, values);
        if (id == -1) {
            // If the ID is -1, then the insertion failed. Log an error and return null.
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = ItemContract.uriMatcher.match(uri);
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        switch (match) {
            case ITEMS:
                return db.delete(TABLE_NAME, selection, selectionArgs);
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = ItemContract.uriMatcher.match(uri);
        SQLiteDatabase db = inventoryDbHelper.getWritableDatabase();
        switch (match) {
            case ITEMS:
                return db.update(TABLE_NAME, values, selection, selectionArgs);
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.update(TABLE_NAME, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
}
