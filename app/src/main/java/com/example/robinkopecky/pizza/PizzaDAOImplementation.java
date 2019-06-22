package com.example.robinkopecky.pizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class PizzaDAOImplementation implements IPizzaDAO {
    private Context context;
    public PizzaDAOImplementation(Context context){
        this.context = context;
    }


    @Override
    public void insertPizza(Pizza pizza) {

        PizzaDatabaseHelper pizzaDatabaseHelper = new PizzaDatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = pizzaDatabaseHelper.getWritableDatabase();

        try {
            long id = sqLiteDatabase.insert(PizzaDatabaseScheme.TABLE_NAME, null, entityToContentValues(pizza));
            pizza.setId(id);

        } finally {
            sqLiteDatabase.close();
        }

    }




    @Override
    public void updatePizza(Pizza pizza) {
        PizzaDatabaseHelper pizzaDatabaseHelper = new PizzaDatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = pizzaDatabaseHelper.getWritableDatabase();


        try {
            int numberOfRowsUpdated = sqLiteDatabase.update(PizzaDatabaseScheme.TABLE_NAME,
                    entityToContentValues(pizza), PizzaDatabaseScheme._ID + "=? ",
                    new String[]{String.valueOf(pizza.getId())});
        } finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public void deletePizza(Pizza pizza) {
        PizzaDatabaseHelper pizzaDatabaseHelper = new PizzaDatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = pizzaDatabaseHelper.getWritableDatabase();

        try {
            int numberOfRowsDeleted = sqLiteDatabase.delete(PizzaDatabaseScheme.TABLE_NAME, PizzaDatabaseScheme._ID + "=? ",
                    new String[]{String.valueOf(pizza.getId())});
        } finally {
            sqLiteDatabase.close();
        }

    }

    @Override
    public List<Pizza> getAllPizza() {
        List<Pizza> result = new ArrayList<>();

        PizzaDatabaseHelper pizzaDatabaseHelper = new PizzaDatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = pizzaDatabaseHelper.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.query(PizzaDatabaseScheme.TABLE_NAME, null, null,
                null, null, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    result.add(cursorToEntity(cursor));
                }
            }
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }
        return result;
    }



    @Override
    public Pizza getPizzaByID(long id) {

        Pizza result = null;


        PizzaDatabaseHelper pizzaDatabaseHelper = new PizzaDatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = pizzaDatabaseHelper.getWritableDatabase();
//todo
        Cursor cursor = sqLiteDatabase.query(PizzaDatabaseScheme.TABLE_NAME, null, PizzaDatabaseScheme._ID + "=? ",
                new String[]{String.valueOf(id)}, null, null, null);


        try {
            if (cursor != null && cursor.getCount() > 0){
                if (cursor.moveToNext()){
                    result = cursorToEntity(cursor);
                }
            }
        } finally {
            cursor.close();
            sqLiteDatabase.close();
        }

        return result;
    }

    @Override
    public void markAsFavorite(long id, boolean favorite) {

        PizzaDatabaseHelper pizzaDatabaseHelper = new PizzaDatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = pizzaDatabaseHelper.getWritableDatabase();

        try {
            ContentValues contentValues = new ContentValues();
            if (favorite){
                contentValues.put(PizzaDatabaseScheme._FAVORITE, 1);
            } else {
                contentValues.put(PizzaDatabaseScheme._FAVORITE, 0);
            }

            int numberOfRowsUpdated
                    = sqLiteDatabase
                    .update(PizzaDatabaseScheme.TABLE_NAME,
                            contentValues,
                            PizzaDatabaseScheme._ID + "=? ",
                            new String[]{String.valueOf(id)});

        } finally {
            sqLiteDatabase.close();
        }

    }

    @Override
    public List<Pizza> getFavoritePizza() {

        PizzaDatabaseHelper pizzaDatabaseHelper = new PizzaDatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = pizzaDatabaseHelper.getWritableDatabase();
        return null;
    }



    private ContentValues entityToContentValues(Pizza pizza) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PizzaDatabaseScheme._NAME, pizza.getName());
        System.out.print(pizza.getName());
        contentValues.put(PizzaDatabaseScheme._DESCRIPTION, pizza.getDescription());
        contentValues.put(PizzaDatabaseScheme._PRICE, pizza.getPrice());
        contentValues.put(PizzaDatabaseScheme._IMAGE, pizza.getImage());
        if (pizza.isFavorite()) {
            contentValues.put(PizzaDatabaseScheme._FAVORITE,1);
        } else {
            contentValues.put(PizzaDatabaseScheme._FAVORITE,0);
        }

        return contentValues;

    }

    private Pizza cursorToEntity(Cursor cursor) {
        Pizza pizza = new Pizza();

        pizza.setId(cursor.getLong(cursor.getColumnIndex(PizzaDatabaseScheme._ID)));
        pizza.setName(cursor.getString(cursor.getColumnIndex(PizzaDatabaseScheme._NAME)));
        pizza.setDescription(cursor.getString(cursor.getColumnIndex(PizzaDatabaseScheme._DESCRIPTION)));
        pizza.setPrice(cursor.getString(cursor.getColumnIndex(PizzaDatabaseScheme._PRICE)));
        pizza.setImage(cursor.getString(cursor.getColumnIndex(PizzaDatabaseScheme._IMAGE)));

        int favorie = cursor.getInt(cursor.getColumnIndex(PizzaDatabaseScheme._FAVORITE));

        if (favorie == 1){
            pizza.setFavorite(true);
        }

        return pizza;

    }
}
