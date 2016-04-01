package me.ketansingh.mynotes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {
    private String action;
    private EditText editor;
    private String oldText;
    private String noteFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        editor=(EditText)findViewById(R.id.editText);
        Intent intent=getIntent();
        Uri uri=intent.getParcelableExtra(NotesProvider.CONTENT_ITEM_TYPE);
        if(uri == null) {
            action = Intent.ACTION_INSERT;
            setTitle(getString(R.string.new_note));
        }
        else {
            action = Intent.ACTION_EDIT;
            noteFilter=DBOpenHelper.NOTE_ID+"="+uri.getLastPathSegment();

            Cursor cursor=getContentResolver().query(uri,DBOpenHelper.ALL_COLUMNS
            ,noteFilter,null,null);
            cursor.moveToFirst();
            oldText=cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
            editor.setText(oldText);
            editor.requestFocus();

        }
    }
    private void finishEditing(){
        String newText=editor.getText().toString().trim();

        switch(action){
            case Intent.ACTION_INSERT:
                if(newText.length() == 0){
                    setResult(RESULT_CANCELED);
                }
                else {
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if(newText.length() == 0){
                    deleteNote();
                }
                else if(oldText.equals(newText)){
                    setResult(RESULT_CANCELED);
                }
                else {
                    UpdateNote(newText);
                }
        }
    }

    private void UpdateNote(String newText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, newText);
        getContentResolver().update(NotesProvider.CONTENT_URI, values, noteFilter, null);
        Toast.makeText(this, getString(R.string.note_updaed), Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
    }

    private void deleteNote() {
        getContentResolver().delete(NotesProvider.CONTENT_URI,noteFilter,null);
    }

    private void insertNote(String newText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, newText);
        getContentResolver().insert(NotesProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_save_note:
                finishEditing();
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_delete_note:
                deleteNote();
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals(Intent.ACTION_EDIT))
        getMenuInflater().inflate(R.menu.menu_editor,menu);
        else
        getMenuInflater().inflate(R.menu.menu_editor_new,menu);
        return true;
    }
}
