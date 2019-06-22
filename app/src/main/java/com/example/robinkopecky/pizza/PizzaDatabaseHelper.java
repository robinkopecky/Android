package com.example.robinkopecky.pizza;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PizzaDatabaseHelper extends SQLiteOpenHelper {

    private static final String NAME = "pizza_db";
    public static final int VERSION = 4;

    public PizzaDatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(PizzaDatabaseScheme.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(PizzaDatabaseScheme.DROP_TABLE);
        onCreate(sqLiteDatabase);

    }
}
