package com.jkurapati.android.inventoryapplication.db;

import android.provider.BaseColumns;

public final class ItemContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ItemContract() {}

    /* Inner class that defines the table contents */
    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final  String COLUMN_NAME_NAME = "name";
        public static final  String COLUMN_NAME_QUANTITY = "quantity";
        public static final  String COLUMN_NAME_EXPIRATION_DATE = "expirationDate";
        public static final  String COLUMN_NAME_PURCHASE_DATE = "purchaseDate";
    }
}
