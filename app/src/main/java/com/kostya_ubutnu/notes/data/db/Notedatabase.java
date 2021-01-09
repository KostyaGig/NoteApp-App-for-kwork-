package com.kostya_ubutnu.notes.data.db;

import android.app.Application;
import android.provider.ContactsContract;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kostya_ubutnu.notes.data.dao.NoteDao;
import com.kostya_ubutnu.notes.models.Note;

@Database(entities = Note.class,version = 1,exportSchema = false)
public abstract class Notedatabase extends RoomDatabase {

    private static Notedatabase notedatabase;

    public abstract NoteDao getNoteDao();

    public static synchronized Notedatabase getInstance(Application application){

        if (notedatabase == null){
            notedatabase = Room.databaseBuilder(application,Notedatabase.class,"NoteDatabase.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return notedatabase;
    }

}
