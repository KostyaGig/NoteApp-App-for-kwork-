package com.kostya_ubutnu.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.kostya_ubutnu.notes.models.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;

public class ActionWithNoteActivity extends AppCompatActivity {

   private Toolbar toolbar;
   private Note clickedNote;

   private EditText fieldTitle,fieldText;
   private Button actionBtn;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_with_note);

        initToolbar();

        fieldTitle = findViewById(R.id.field_title);
        fieldText  = findViewById(R.id.field_text);

        actionBtn = findViewById(R.id.action_btn);

        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        clickedNote = (Note) getIntent().getSerializableExtra(MainActivity.EXTRA_NOTE);

            if (clickedNote != null) {
                toolbar.setTitle(getResources().getString(R.string.edit_note_text));
                fieldTitle.setText(clickedNote.getTitle());
                fieldText.setText(clickedNote.getText());
                actionBtn.setText(getResources().getString(R.string.update_note_text));

            } else {
                toolbar.setTitle(getResources().getString(R.string.add_note_text));
            }


    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            Log.d("toolbaraction","!= null");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void saveNote(){
        if (clickedNote == null) {
            Log.d("dateformat", "clicked note = null");
        } else {
            Log.d("dateformat","clicked note != null");
        }
        String title = fieldTitle.getText().toString().trim();
        String text = fieldText.getText().toString().trim();
        Intent dataIntent = new Intent();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)){
            Toast.makeText(this, "Заполните пустые поля", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(getCurrentDate(),text,title);

        if (clickedNote != null){
            Log.d("dateformat","id clicked note = " + clickedNote.getId());
            note.setId(clickedNote.getId());
            Log.d("dateformat","sending note id  = " + note.getId());
        }

        dataIntent.putExtra(MainActivity.EXTRA_NOTE,note);
        setResult(RESULT_OK,dataIntent);
        finish();
    }


    private String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        String date = dateFormat.format(calendar.getTime());
        Log.d("dateformat",date);
        return date;
    }
}
