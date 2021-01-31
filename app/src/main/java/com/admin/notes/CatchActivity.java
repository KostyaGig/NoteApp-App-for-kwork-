package com.admin.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.admin.notes.models.Note;
import com.admin.notes.viewmodels.NoteViewmodel;

import static com.admin.notes.services.AlarmReceiver.DATE_NOTE;
import static com.admin.notes.services.AlarmReceiver.DAY_OF_MONTH;
import static com.admin.notes.services.AlarmReceiver.HOURS_NOTE;
import static com.admin.notes.services.AlarmReceiver.ID_NOTE;
import static com.admin.notes.services.AlarmReceiver.MINUTES_NOTE;
import static com.admin.notes.services.AlarmReceiver.TEXT_NOTE;
import static com.admin.notes.services.AlarmReceiver.TIME_NOTE;
import static com.admin.notes.services.AlarmReceiver.TITLE_NOTE;

public class CatchActivity extends AppCompatActivity {

    private NoteViewmodel viewmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cathc);

        viewmodel = ViewModelProviders.of(this).get(NoteViewmodel.class);

        int id = getIntent().getIntExtra(ID_NOTE,-1);
        String date = getIntent().getStringExtra(DATE_NOTE);
        String time = getIntent().getStringExtra(TIME_NOTE);
        String title = getIntent().getStringExtra(TITLE_NOTE);
        String hours = getIntent().getStringExtra(HOURS_NOTE);
        String minutes = getIntent().getStringExtra(MINUTES_NOTE);
        String dayOfMonth = getIntent().getStringExtra(DAY_OF_MONTH);
        String text = getIntent().getStringExtra(TEXT_NOTE);

        if (id != -1 && title != null && hours != null && minutes != null && dayOfMonth != null && date != null && text != null){
            Log.d("AlarmService","CATCH ACTIVITY note != null note id = " + id + " title = " + title + " hour = " + hours + " minutes " + minutes);


            Note note = new Note(date,title,text,hours,time,null,null);
            note.setId(id);

            viewmodel.updateNote(note);

            startActivity(new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    .putExtra(MainActivity.EXTRA_NOTE,note)
            );
        } else {
            Log.d("AlarmService","CATCH ACTIVITY note == null");
        }

    }


}
