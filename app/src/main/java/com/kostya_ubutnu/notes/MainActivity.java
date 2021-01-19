package com.kostya_ubutnu.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding4.widget.RxTextView;
import com.kostya_ubutnu.notes.adapters.Adapter;
import com.kostya_ubutnu.notes.interfaces.AdapterLIstener;
import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.viewmodels.NoteViewmodel;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterLIstener {

    public static final String EXTRA_NOTE = "Note";

    public static final int EXTRA_ADD_NOTE = 1;
    public static final int EXTRA_EDIT_NOTE = 2;

    private NoteViewmodel viewmodel;
    private Observer<List<Note>> usuallyObserver;
    private Observer<List<Note>> searchObserver;

    private RecyclerView recyclerView;
    private List<Note> listNote = new ArrayList<>();
    private Adapter adapter;

    private FloatingActionButton fab;

    private TextView noteCounter;
    private EditText fieldSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        init();
        initRecyclerView();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (getIntent().hasExtra(MainActivity.EXTRA_NOTE)) {
            Note note =(Note) getIntent().getSerializableExtra(EXTRA_NOTE);
            if (note != null){
                showNotification(note);
            } else {
                Log.d("AlarmService","Mainactivity note == null");
            }

        }

        usuallyObserver = new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                Log.d("SearchView1","ONCHANGED");
                Log.d("SearchView1","USUALLY onchanged size = " + notes.size());

                if (notes.size() == 0){
                    Log.d("SearchView1","notes == null");
                } else {
                    Log.d("SearchView1","notes != null");
                }

                noteCounter.setText("Заметок всего:" +" " + notes.size());
                listNote = notes;
                adapter.setList(notes);
            }
        };

        searchObserver = new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                noteCounter.setText("Заметок всего:" + " " + notes.size());
                Log.d("SearchView1","SEARCH onchanged size = " + notes.size());

                listNote = notes;
                adapter.setList(notes);
            }
        };

        fab.setOnClickListener(this);
        adapter.setListener(this);

        RxTextView.textChanges(fieldSearch)
                .debounce(100,TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation())
                .filter(charSequence -> charSequence.length() != 0)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.rxjava3.core.Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull CharSequence charSequence) {
                        Log.d("SerarchView1","MAINACTIVITY ONNEXT " + charSequence.toString());
                        viewmodel.getNotesOnTitle(charSequence.toString());

                        Log.d("SerarchView1","lengt = " + charSequence.length());
                        if (charSequence.length() == 0){
                            viewmodel.getAllNotes();
                        }

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d("SearchView1","error : " +e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
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
        noteCounter = findViewById(R.id.note_counter_tv);
        fieldSearch = findViewById(R.id.field_search);
        viewmodel = ViewModelProviders.of(this).get(NoteViewmodel.class);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    @Override
    protected void onStop() {
        super.onStop();
        viewmodel.getNotes().removeObserver(usuallyObserver);
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
        viewmodel.getNotes().observe(this,usuallyObserver);
        viewmodel.getSearchNotes().observe(this,searchObserver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {

            Intent noteIntent = new Intent(MainActivity.this, ActionWithNoteActivity.class);
            startActivityForResult(noteIntent, EXTRA_ADD_NOTE);
        }

    }

    @Override
    public void setItemClickListener(int position,int id,Note note) {

        Intent noteIntent = new Intent(MainActivity.this, ActionWithNoteActivity.class);
        noteIntent.putExtra(EXTRA_NOTE,note);

        switch (id){
            case 100:
                startActivityForResult(noteIntent,EXTRA_EDIT_NOTE);
                break;
            case R.id.edit_image:
                startActivityForResult(noteIntent,EXTRA_EDIT_NOTE);
                break;

            case R.id.delete_image:
                viewmodel.deleteNote(note);
//                deleteNote(position);
                break;

        }

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

    private void showNotification(Note note) {
        Alerter.create(MainActivity.this)
                 .setBackgroundColorInt(getResources().getColor(R.color.colorAccent))
                 .setIcon(R.drawable.alerter_ic_notifications)
                 .setTitle("Событие выполнено")
                 .setText("Событие с названием " + note.getTitle() + " было выполнено!")
                 .enableVibration(true)
                 .enableSwipeToDismiss()
                 .setDuration(5000)
                 .show();
    }


}
