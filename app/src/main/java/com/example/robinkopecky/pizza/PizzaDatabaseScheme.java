package com.example.robinkopecky.pizza;

import android.provider.BaseColumns;

public interface PizzaDatabaseScheme extends BaseColumns {

    String TABLE_NAME = "pizzas";

    String _NAME = "name";
    String _DESCRIPTION = "description";
    String _IMAGE = "image";
    String _PRICE = "price";
    String _FAVORITE = "favorite";


    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY,"
            + _NAME + " TEXT,"
            + _DESCRIPTION + " TEXT,"
            + _FAVORITE + " INTEGER,"
            + _PRICE + " TEXT,"
            + _IMAGE + " TEXT"
            + ")";

    String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;




}
