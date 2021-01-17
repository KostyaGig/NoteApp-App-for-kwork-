package com.kostya_ubutnu.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.models.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;

public class ActionWithNoteActivity extends AppCompatActivity {

   private Toolbar toolbar;
   private Note clickedNote;

   private EditText fieldTitle,fieldText;
//   private Button actionBtn;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_with_note);

        initToolbar();

        fieldTitle = findViewById(R.id.field_title);
        fieldText  = findViewById(R.id.field_text);

//        actionBtn = findViewById(R.id.action_btn);

//        actionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveNote();
//            }
//        });

        clickedNote = (Note) getIntent().getSerializableExtra(MainActivity.EXTRA_NOTE);

            if (clickedNote != null) {
                toolbar.setTitle(getResources().getString(R.string.edit_note_text));
                fieldTitle.setText(clickedNote.getTitle());
                fieldText.setText(clickedNote.getText());
//                actionBtn.setText(getResources().getString(R.string.update_note_text));

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        } else if (item.getItemId() == R.id.save_note){
            saveNote();
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void saveNote(){
        Note note;

        if (clickedNote == null) {
            Log.d("dateformat", "clicked note = null");
        } else {
            Log.d("dateformat","clicked note != null");
        }
        String title = fieldTitle.getText().toString().trim();
        String text = fieldText.getText().toString().trim();
        TimePicker picker = findViewById(R.id.field_timer);
        Intent dataIntent = new Intent();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)){
            Toast.makeText(this, "Введите название заметки и ее текст!", Toast.LENGTH_SHORT).show();
            return;
        }

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
            calendar.set(Calendar.MINUTE,picker.getMinute());

            int hour = picker.getHour();
            int minute = picker.getMinute();

            String hourS = String.valueOf(hour);
            String minuteS = String.valueOf(minute);


            long selectedTime = calendar.getTimeInMillis();

            if (hour > 12){
                hourS = String.valueOf(hour - 12);
            }

            if (minute < 10){
                minuteS = "0" + minute;
            }

            Log.d("NoteTimer","Curren time = " + selectedTime);
            note = new Note(getCurrentDate(),title,text,hourS,minuteS,selectedTime);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notes,menu);
        return true;
    }

}
