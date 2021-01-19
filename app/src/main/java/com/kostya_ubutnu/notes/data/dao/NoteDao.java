package com.kostya_ubutnu.notes.data.dao;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kostya_ubutnu.notes.models.Note;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM NoteTable2")
    Observable<List<Note>> getAllNotes();

    @Query("SELECT * FROM  notetable2 WHERE title = :title")
    Observable<List<Note>> getNotesOnTitle(String title);


}
