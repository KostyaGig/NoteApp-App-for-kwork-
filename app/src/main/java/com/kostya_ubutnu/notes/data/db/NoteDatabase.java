package com.kostya_ubutnu.notes.data.db;

import android.app.Application;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kostya_ubutnu.notes.data.dao.NoteDao;
import com.kostya_ubutnu.notes.models.Note;

@Database(entities = Note.class,version = 1,exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase notedatabase;

    public abstract NoteDao getNoteDao();

    public static synchronized NoteDatabase getInstance(Application application){

        if (notedatabase == null){
            Log.d("SearchView1","db create");
            notedatabase = Room.databaseBuilder(application,NoteDatabase.class,"NoteDatabase.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return notedatabase;
    }

}
