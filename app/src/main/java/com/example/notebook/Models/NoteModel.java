package com.example.notebook.Models;

public class NoteModel {
    private String note , tag , date , id ;
    private boolean favorites ;

    public NoteModel(String note, String tag, String date, boolean favorites , String id) {
        this.note = note;
        this.tag = tag;
        this.date = date;
        this.favorites = favorites;
        this.id = id ;
    }

    public NoteModel(String note, String tag, String date, boolean favorites ) {
        this.note = note;
        this.tag = tag;
        this.date = date;
        this.favorites = favorites;
    }
    public NoteModel() {
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
