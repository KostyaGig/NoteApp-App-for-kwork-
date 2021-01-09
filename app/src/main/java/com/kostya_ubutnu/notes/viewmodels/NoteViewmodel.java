package com.kostya_ubutnu.notes.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kostya_ubutnu.notes.models.Note;
import com.kostya_ubutnu.notes.repositories.NoteRepository;

import java.util.List;

public class NoteViewmodel extends AndroidViewModel {

    private NoteRepository repository;
    private MutableLiveData<List<Note>> notes;

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

}
