package com.example.robinkopecky.pizza;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BagDatabaseHelper extends SQLiteOpenHelper {

    private static final String NAME = "pizza_bag";
    public static final int VERSION = 1;

    public BagDatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(BagDatabaseScheme.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(BagDatabaseScheme.DROP_TABLE);
        onCreate(sqLiteDatabase);

    }
}