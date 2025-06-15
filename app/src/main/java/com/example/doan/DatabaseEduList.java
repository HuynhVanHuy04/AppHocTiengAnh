package com.example.doan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.*;
import java.util.*;

public class DatabaseEduList extends SQLiteOpenHelper {
    private static final String DB_NAME = "english_list.db";
    private static String DB_PATH = "";
    private Context context;

    public DatabaseEduList(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        createDatabase();
    }

    private void createDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            this.getReadableDatabase();
            try {
                InputStream is = context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public List<Word> getRandomWords(int limit) {
        List<Word> words = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM list ORDER BY RANDOM() LIMIT ?", new String[]{String.valueOf(limit)});
        while (cursor.moveToNext()) {
            String vietnamese = cursor.getString(cursor.getColumnIndexOrThrow("VN"));
            String english = cursor.getString(cursor.getColumnIndexOrThrow("USA"));
            words.add(new Word(english, vietnamese));
        }
        cursor.close();
        return words;
    }
}
