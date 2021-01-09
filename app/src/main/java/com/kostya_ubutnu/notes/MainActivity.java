package com.kostya_ubutnu.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kostya_ubutnu.notes.adapters.Adapter;
import com.kostya_ubutnu.notes.interfaces.AdapterLIstener;
import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.viewmodels.NoteViewmodel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterLIstener {

    public static final String EXTRA_NOTE = "Note";

    public static final int EXTRA_ADD_NOTE = 1;
    public static final int EXTRA_EDIT_NOTE = 2;

    private NoteViewmodel viewmodel;
    private Observer<List<Note>> observer;

    private RecyclerView recyclerView;
    private List<Note> listNote = new ArrayList<>();
    private Adapter adapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        init();
        initRecyclerView();

        observer = new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                listNote = notes;
                adapter.setList(notes);
            }
        };

        fab.setOnClickListener(this);
        adapter.setListener(this);
    }

    private void initRecyclerView() {

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Adapter();
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(getHelper());
        helper.attachToRecyclerView(recyclerView);

    }

    private void init() {
        fab = findViewById(R.id.fab);
        viewmodel = ViewModelProviders.of(this).get(NoteViewmodel.class);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    protected void onStop() {
        super.onStop();
        viewmodel.getNotes().removeObserver(observer);
    }


    private void deleteNote(int position){
        viewmodel.deleteNote(listNote.get(position));

        listNote.remove(position);
        adapter.notifyItemRangeChanged(0,listNote.size());
        adapter.notifyItemRemoved(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EXTRA_ADD_NOTE && resultCode == RESULT_OK && data != null){

            Log.d("dateformat","onactivityresult");
            Note note = (Note)data.getSerializableExtra(EXTRA_NOTE);
            viewmodel.insertNote(note);
        }
        else if (requestCode == EXTRA_EDIT_NOTE && resultCode == RESULT_OK && data != null){
            Log.d("dateformat","update");
            viewmodel.updateNote((Note)data.getSerializableExtra(EXTRA_NOTE));
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("dateformat","onstart");
        viewmodel.getNotes().observe(this,observer);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            Intent noteIntent = new Intent(MainActivity.this, ActionWithNoteActivity.class);
            startActivityForResult(noteIntent, EXTRA_ADD_NOTE);
        }

    }

    @Override
    public void setItemClickListener(Note note) {
        Intent noteIntent = new Intent(MainActivity.this, ActionWithNoteActivity.class);
        noteIntent.putExtra(EXTRA_NOTE,note);
        startActivityForResult(noteIntent,EXTRA_EDIT_NOTE);
    }

    public ItemTouchHelper.SimpleCallback getHelper(){
        return new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteNote(viewHolder.getAdapterPosition());
            }
        };
    }

}
