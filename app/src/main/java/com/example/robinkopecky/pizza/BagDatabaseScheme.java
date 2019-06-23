package com.example.robinkopecky.pizza;

import android.provider.BaseColumns;

public interface BagDatabaseScheme extends BaseColumns {


    String TABLE_NAME = "pizzaBag";

    String _NAME = "name";
    String _DESCRIPTION = "description";
    String _PRICE = "price";


    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + _NAME + " TEXT,"
            + _DESCRIPTION + " TEXT,"
            + _PRICE + " TEXT"
            + ")";

    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
