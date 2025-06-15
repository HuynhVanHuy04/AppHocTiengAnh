package com.example.doan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.IOException;

public class DatabaseFamily1 extends SQLiteOpenHelper {

    private static String DB_NAME = "learn.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;
    private final Context mContext;

    public DatabaseFamily1(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        copyDatabase();
    }

    private void copyDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            this.getReadableDatabase();
            try {
                InputStream is = mContext.getAssets().open(DB_NAME);
                File dbDir = new File(DB_PATH);
                if (!dbDir.exists()) {
                    dbDir.mkdirs();
                }
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                throw new RuntimeException("Error copying database", e);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Không tạo bảng vì đã có sẵn file .db
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Không nâng cấp cơ sở dữ liệu
    }

    public String[] getRandomWord() {
        String[] word = new String[2];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM Family1 ORDER BY RANDOM() LIMIT 1", null);
            if (cursor != null && cursor.moveToFirst()) {
                word[0] = cursor.getString(cursor.getColumnIndexOrThrow("TA"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return word;
    }

    // In ra tên cột của bảng để debug (tùy chọn)
    public void printColumns() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("PRAGMA table_info(w_eg)", null);
        while (cursor.moveToNext()) {
            String columnName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            System.out.println("Column: " + columnName);
        }
        cursor.close();
        db.close();
    }
}
