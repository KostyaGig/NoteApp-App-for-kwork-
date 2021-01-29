package com.kostya_ubutnu.notes;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.models.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.IllegalFormatCodePointException;

public class ActionWithNoteActivity extends AppCompatActivity implements View.OnClickListener{


    private Toolbar toolbar;
    private Note clickedNote;

    private EditText fieldTitle,fieldText;

    private Button selectDateBtn,selectTimeBtn;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;
   
    int hour = 0;
    int minute = 0;
    int day = 0;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_with_note);

        initToolbar();

        fieldTitle = findViewById(R.id.field_title);
        fieldText  = findViewById(R.id.field_text);

        selectDateBtn = findViewById(R.id.select_date_btn);
        selectTimeBtn = findViewById(R.id.select_time_btn);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        clickedNote = (Note) getIntent().getSerializableExtra(MainActivity.EXTRA_NOTE);

            if (clickedNote != null) {
                toolbar.setTitle(getResources().getString(R.string.edit_note_text));
                fieldTitle.setText(clickedNote.getTitle());
                fieldText.setText(clickedNote.getText());

            } else {
                toolbar.setTitle(getResources().getString(R.string.add_note_text));
            }


            selectDateBtn.setOnClickListener(this);
            selectTimeBtn.setOnClickListener(this);

            dateListener = (view, year, month, dayOfMonth) -> {
                day = dayOfMonth;
                Log.d("CurrentNote","listener date day -> " + day);
            };

            timeListener = (view, hourOfDay, minuteOfHour) -> {
                hour = hourOfDay;


                minute = minuteOfHour;
                Log.d("CurrentNote","listener time hour  > " + hour + " minute ->> " + minute + " day -> " + day + " ---currentTimeInMillis--- " + calendar.getTimeInMillis());
            };

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
            Log.d("CurrentNote", "clicked note = null");
        } else {
            Log.d("CurrentNote","clicked note != null");
        }
        String title = fieldTitle.getText().toString().trim();
        String text = fieldText.getText().toString().trim();


        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)){
            Toast.makeText(this, "Введите название заметки и ее текст!", Toast.LENGTH_SHORT).show();
            return;
        } else if (hour == 0 || day == 0 || minute == 0){
            Toast.makeText(this, "Настройте дату и время!", Toast.LENGTH_SHORT).show();
            return;
        }

            Intent mainIntentNote = new Intent();

            calendar.set(Calendar.HOUR_OF_DAY,hour);
            calendar.set(Calendar.MINUTE,minute);
            calendar.set(Calendar.DAY_OF_MONTH,day);

            String hourS = String.valueOf(hour);
            String minuteS = String.valueOf(minute);
            String dayS = String.valueOf(day);

            long selectedTime = calendar.getTimeInMillis();

            if (hour > 12){
                hourS = String.valueOf(hour - 12);
            }

            if (minute < 10){
                minuteS = "0" + minute;
            }

            Log.d("CurrentNote","SAVE NOTE current hour -> " + hourS + " current minute -> " + minuteS + " current day -> " + dayS + "----------CurrentTime------->>>> " + calendar.getTimeInMillis() + "CurrentTime = " + selectedTime);

            note = new Note(getCurrentDate(),title,text,hourS,minuteS,dayS,selectedTime);

            if (note == null){
                Log.d("CurrentNote","NOTE = NULL");
            } else {
                Log.d("CurrentNote","Note != NULL");
                Log.d("CurrentNote","id " + note.getId() + " current time -> " + note.getSelectedTime());
            }


        if (clickedNote != null){
            Log.d("CurrentNote","id clicked note = " + clickedNote.getId());
            //Устанавливаем новой записи id старой записи для оьновления записи по Id
            note.setId(clickedNote.getId());
            Log.d("CurrentNote","sending note id  = " + note.getId());
        }

        setResult(RESULT_OK,mainIntentNote);
        mainIntentNote.putExtra(MainActivity.EXTRA_NOTE,note);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.select_date_btn:
                selectDate();
                break;
            case R.id.select_time_btn:
                selectTime();
                break;
        }

    }

    private void selectDate(){
        new DatePickerDialog(ActionWithNoteActivity.this,dateListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void selectTime(){
        new TimePickerDialog(ActionWithNoteActivity.this,timeListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
    }

}
