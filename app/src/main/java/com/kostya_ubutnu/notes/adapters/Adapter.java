package com.kostya_ubutnu.notes.adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.kostya_ubutnu.notes.R;
import com.kostya_ubutnu.notes.interfaces.AdapterLIstener;
import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.services.AlarmReceiver;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public static final int REQUEST_BROAD_CAST_RECEIVER = 101;

    private List<Note> notes = new ArrayList<>();
    private AdapterLIstener listener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("NoteTimer",notes.size() + "");

        holder.bind(notes.get(position));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setList(List<Note> listNote) {
        notes = listNote;
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,date,timer;

        ImageView editImage,deleteImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            timer = itemView.findViewById(R.id.timer);

            editImage = itemView.findViewById(R.id.edit_image);
            deleteImage = itemView.findViewById(R.id.delete_image);
        }

        void bind(final Note note) {
            title.setText(note.getTitle());
            date.setText(note.getDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.setItemClickListener(getAdapterPosition(),100,note);
                }
            });

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.setItemClickListener(getAdapterPosition(),v.getId(),note);
                }
            });

            editImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.setItemClickListener(getAdapterPosition(),v.getId(),note);
                }
            });

            if (note.getSelectedTime() == null){
                Log.d("AlarmService","selected time == null");
                timer.setTextColor(itemView.getContext().getResources().getColor(R.color.colorPrimary));
                timer.setText(note.getMinute());
            } else {
                timer.setTextColor(itemView.getContext().getResources().getColor(R.color.color_red_text));
                timer.setText("Событие будет выполнено " + note.getDayOfMonth() + " числа" +" в " + note.getHour() + " часа(ов) " + note.getMinute() + " минут(ы)");

                Intent broadCastIntent = new Intent(itemView.getContext(), AlarmReceiver.class);

                broadCastIntent.putExtra(AlarmReceiver.ID_NOTE, note.getId());
                broadCastIntent.putExtra(AlarmReceiver.DATE_NOTE, note.getDate());
                broadCastIntent.putExtra(AlarmReceiver.TITLE_NOTE, note.getTitle());
                broadCastIntent.putExtra(AlarmReceiver.HOURS_NOTE, note.getHour());
                broadCastIntent.putExtra(AlarmReceiver.MINUTES_NOTE, note.getMinute());
                broadCastIntent.putExtra(AlarmReceiver.DAY_OF_MONTH,note.getDayOfMonth());
                broadCastIntent.putExtra(AlarmReceiver.TEXT_NOTE, note.getText());

                AlarmManager manager = (AlarmManager) itemView.getContext().getSystemService(Context.ALARM_SERVICE);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(itemView.getContext(), REQUEST_BROAD_CAST_RECEIVER, broadCastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, note.getSelectedTime(), pendingIntent);
                }
            }



        }



    }

    public void setListener(AdapterLIstener listener){
        this.listener = listener;
    }



}
