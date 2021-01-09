package com.kostya_ubutnu.notes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "NoteTable")
public class Note implements Serializable {

    private @PrimaryKey(autoGenerate = true) int id;
    private String date;
    private String title;
    private String text;

    public Note() {
    }


    public Note(String date, String text,String title) {
        this.date = date;
        this.text = text;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
