package me.ketansingh.mynotes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class NotesProvider extends ContentProvider {
    private static final String AUTHORITY= "me.ketansingh.mynotes.notesprovider";
    private static final String BASE_PATH= "notes";
    public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);
    private static final int NOTES=1;
    private static final int NOTES_ID=2;
    public static String CONTENT_ITEM_TYPE="Note";
    private static final UriMatcher urimatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        urimatcher.addURI(AUTHORITY,BASE_PATH,NOTES);
        urimatcher.addURI(AUTHORITY,BASE_PATH+"/#",NOTES_ID);
    }
    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        DBOpenHelper helper=new DBOpenHelper(getContext());
        db=helper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if(urimatcher.match(uri)==NOTES_ID){
            selection=DBOpenHelper.NOTE_ID + "=" +uri.getLastPathSegment();

        }

        return db.query(DBOpenHelper.TABLE_NOTES,DBOpenHelper.ALL_COLUMNS,selection,null,null,null
        ,DBOpenHelper.NOTE_CREATED+" DESC");

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id=db.insert(DBOpenHelper.TABLE_NOTES,null,values);

        return Uri.parse(BASE_PATH+"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(DBOpenHelper.TABLE_NOTES,selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return db.update(DBOpenHelper.TABLE_NOTES,values,selection,selectionArgs);
    }
}
