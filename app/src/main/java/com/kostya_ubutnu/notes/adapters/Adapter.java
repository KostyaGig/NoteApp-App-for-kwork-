package com.kostya_ubutnu.notes.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kostya_ubutnu.notes.R;
import com.kostya_ubutnu.notes.interfaces.AdapterLIstener;
import com.kostya_ubutnu.notes.models.Note;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


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
        if (notes == null){
            Toast.makeText(holder.itemView.getContext(), "Заметок пока нет", Toast.LENGTH_SHORT).show();
            return;
        }
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

        TextView title,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);

        }

        void bind(final Note note){
            title.setText(note.getTitle());
            date.setText(note.getDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.setItemClickListener(note);
                }
            });

        }

    }

    public void setListener(AdapterLIstener listener){
        this.listener = listener;
    }

}
