package com.example.doan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseRegister extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DK.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseRegister(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DK (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "full_name TEXT NOT NULL," +
                "phone TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS DK");
        onCreate(db);
    }

    public boolean registerUser(String fullName, String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra trùng số điện thoại
        Cursor cursor = db.rawQuery("SELECT * FROM DK WHERE phone = ?", new String[]{phone});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // SĐT đã tồn tại
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("full_name", fullName);
        values.put("phone", phone);
        values.put("password", password);

        long result = db.insert("DK", null, values);
        return result != -1;
    }

    public boolean loginUser(String phone, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM DK WHERE phone = ? AND password = ?",
                new String[]{phone, password}
        );
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}
