package com.kostya_ubutnu.notes.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kostya_ubutnu.notes.CatchActivity;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ID_NOTE = "_id";
    public static final String TIME_NOTE = "Time";
    public static final String DATE_NOTE= "Date";
    public static final String TITLE_NOTE = "Title";
    public static final String HOURS_NOTE = "Hour";
    public static final String MINUTES_NOTE = "Minutes";
    public static final String DAY_OF_MONTH = "Day of Month";
    public static final String TEXT_NOTE = "Text";

    @Override
    public void onReceive(Context context, Intent intent) {


        int id = intent.getIntExtra(ID_NOTE,-1);
        String title = intent.getStringExtra(TITLE_NOTE);
        String hours = intent.getStringExtra(HOURS_NOTE);
        String minutes = intent.getStringExtra(MINUTES_NOTE);
        String date = intent.getStringExtra(DATE_NOTE);
        String dayOfMont = intent.getStringExtra(DAY_OF_MONTH);
        String text = intent.getStringExtra(TEXT_NOTE);

            if (id != -1 && title != null && hours != null && minutes != null && dayOfMont != null && date != null && text != null){
                Log.d("AlarmService","note != null note id = " + id + " title = " + title + " hour = " + hours + " minutes " + minutes);
               Intent catchActivity = new Intent(context,CatchActivity.class);

                String time  = "Событие было выполнено " + dayOfMont + " числа в " + hours + " часов(а) " + minutes + " минут(ы) ";

                catchActivity.putExtra(ID_NOTE, id);
                catchActivity.putExtra(TITLE_NOTE, title);
                catchActivity.putExtra(HOURS_NOTE, hours);
                catchActivity.putExtra(MINUTES_NOTE, minutes);
                catchActivity.putExtra(DATE_NOTE,date);
                catchActivity.putExtra(TIME_NOTE,time);
                catchActivity.putExtra(DAY_OF_MONTH,dayOfMont);
                catchActivity.putExtra(TEXT_NOTE,text);

                context.startActivity(catchActivity);

            } else {
                Log.d("AlarmService","note == null");
            }


    }

}
