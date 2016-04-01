package me.ketansingh.mynotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME="notes.db";
    private static final int DB_VERSION =1;
    public static final String TABLE_NOTES="notes";
    public static final String NOTE_ID="_id";
    public static final String NOTE_TEXT="noteText";
    public static final String NOTE_CREATED="noteCreated";
    public static final String[] ALL_COLUMNS={NOTE_ID,NOTE_TEXT,NOTE_CREATED};
    private static final String TABLE_CREATE="CREATE TABLE "+TABLE_NOTES+" ("+
            NOTE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,  "+
            NOTE_TEXT+" TEXT, "+
            NOTE_CREATED+ " TEXT default CURRENT_TIMESTAMP"+
            " )";

    public DBOpenHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTES);
        db.execSQL(TABLE_CREATE);

    }
}
