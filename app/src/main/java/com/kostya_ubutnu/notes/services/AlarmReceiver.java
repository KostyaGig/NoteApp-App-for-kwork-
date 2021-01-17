package com.kostya_ubutnu.notes.services;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.RoomDatabase;

import com.kostya_ubutnu.notes.CatchActivity;
import com.kostya_ubutnu.notes.MainActivity;
import com.kostya_ubutnu.notes.R;
import com.kostya_ubutnu.notes.adapters.Adapter;
import com.kostya_ubutnu.notes.data.db.Notedatabase;
import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.viewmodels.NoteViewmodel;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ID_NOTE = "_id";
    public static final String TIME_NOTE = "Time";
    public static final String DATE_NOTE= "Date";
    public static final String TITLE_NOTE = "Title";
    public static final String HOURS_NOTE = "Hour";
    public static final String MINUTES_NOTE = "Minutes";
    public static final String TEXT_NOTE = "Text";

    @Override
    public void onReceive(Context context, Intent intent) {


        int id = intent.getIntExtra(ID_NOTE,-1);
        String title = intent.getStringExtra(TITLE_NOTE);
        String hours = intent.getStringExtra(HOURS_NOTE);
        String minutes = intent.getStringExtra(MINUTES_NOTE);
        String date = intent.getStringExtra(DATE_NOTE);
        String text = intent.getStringExtra(TEXT_NOTE);

            if (id != -1 && title != null && hours != null && minutes != null && date != null && text != null){
                Log.d("AlarmService","note != null note id = " + id + " title = " + title + " hour = " + hours + " minutes " + minutes);
               Intent catchActivity = new Intent(context,CatchActivity.class);

                String time  = "Событие было выполнено в " + hours + " часов(а) " + minutes + " минут(ы) ";

                catchActivity.putExtra(ID_NOTE, id);
                catchActivity.putExtra(TITLE_NOTE, title);
                catchActivity.putExtra(HOURS_NOTE, hours);
                catchActivity.putExtra(MINUTES_NOTE, minutes);
                catchActivity.putExtra(DATE_NOTE,date);
                catchActivity.putExtra(TIME_NOTE,time);
                catchActivity.putExtra(TEXT_NOTE,text);

                context.startActivity(catchActivity);

            } else {
                Log.d("AlarmService","note == null");
            }


    }

}
