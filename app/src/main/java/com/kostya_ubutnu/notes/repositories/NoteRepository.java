package com.kostya_ubutnu.notes.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.kostya_ubutnu.notes.data.dao.NoteDao;
import com.kostya_ubutnu.notes.data.db.NoteDatabase;
import com.kostya_ubutnu.notes.models.Note;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NoteRepository {

    public static final String TAG = "tags";

    private NoteDao noteDao;
    private MutableLiveData<List<Note>> allNotes = new MutableLiveData<>();

    public NoteRepository(Application application){

        Log.d("SearchView1","rep crate");
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDao = database.getNoteDao();

        Observable<List<Note>> data = noteDao.getAllNotes();
        data
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Note> notes) {
                        Log.d("SearchView1","onnext getdata current thread = " + Thread.currentThread().getName());

                        allNotes.setValue(notes);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG,"oN ERROR " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG,"On complete");
                    }
                });
    }

    public void insertNote(Note note){
        noteDao.insertNote(note);
    }

    public void updateNote(Note note){
        Log.d("NoteUpdated","REPOSITORY NOTE UPDATED!");
        noteDao.updateNote(note);
    }

    public void deleteNote(Note note){
        Log.d("AlarmService","Delete note!");
        noteDao.deleteNote(note);
    }

    public void getAllNotes(){
        Observable<List<Note>> allNotes = noteDao.getAllNotes();

        allNotes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Note>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Note> notes) {
                        getNotes().setValue(notes);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public Observable<List<Note>> getNotesOnTitle(String title){
        return noteDao.getNotesOnTitle(title);
    }

    public MutableLiveData<List<Note>> getNotes(){
        return allNotes;
    }


}
