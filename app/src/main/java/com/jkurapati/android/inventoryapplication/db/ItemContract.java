package com.jkurapati.android.inventoryapplication.db;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ItemContract {
    public static final String CONTENT_AUTHORITY = "com.jkurapati.android.inventory";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int ITEMS = 100;
    public static final int ITEM_ID = 101;

    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_ITEMS, ITEMS);
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_ITEMS + "/#", ITEM_ID);
    }

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ItemContract() {
    }

    /* Inner class that defines the table contents */
    public static class ItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);
        public static final String TABLE_NAME = "items";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_QUANTITY = "quantity";
        public static final String COLUMN_NAME_EXPIRATION_DATE = "expirationDate";
        public static final String COLUMN_NAME_PURCHASE_DATE = "purchaseDate";
    }
}
