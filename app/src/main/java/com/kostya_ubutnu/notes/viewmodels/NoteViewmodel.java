package com.kostya_ubutnu.notes.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.repositories.NoteRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteViewmodel extends AndroidViewModel {

    private NoteRepository repository;
    private MutableLiveData<List<Note>> notes;
    private final MutableLiveData<List<Note>> searchNotes = new MutableLiveData<>();

    public NoteViewmodel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        notes = repository.getNotes();
    }

    public void insertNote(Note note){
        repository.insertNote(note);
    }

    public void updateNote(Note note){
        repository.updateNote(note);
    }

    public void deleteNote(Note note){
        repository.deleteNote(note);
    }

    public MutableLiveData<List<Note>> getNotes(){
        return notes;
    }

    public void getNotesOnTitle(String title){
        Observable<List<Note>> listNote = repository.getNotesOnTitle(title);

        listNote
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.d("SearchView1","onsubscribe");
                    }

                    @Override
                    public void onNext(List<Note> notesTitle) {


                        Log.d("SearchView1","size = " + notesTitle.size());
                        Log.d("SearchView1","onnext");

                            if (notesTitle.size() == 0){
                                repository.getAllNotes();
                                notes = repository.getNotes();
                            } else {

                                for (Note note:notesTitle){
                                    Log.d("SearchView1",note.getTitle());
                                }
                                Log.d("SearchView1","Thread - " + Thread.currentThread().getName());
                                searchNotes.postValue(notesTitle);
                            }


                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.d("SearchView1","onerror");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("SearchView1","onсomplete");
                    }
                });
    }

    public void getAllNotes(){
        repository.getAllNotes();
    }

    public MutableLiveData<List<Note>> getSearchNotes(){
        return searchNotes;
    }


}
