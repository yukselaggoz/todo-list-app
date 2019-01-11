package com.yukselaggoz.todo_list_application;


public class NoteData {

    private long Note_date;
    private String Note_title;
    private String Note;
    private long Id;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public NoteData(){
    }
    public NoteData(String note_title,long note_date,String note){

        Note_title=note_title;
        Note_date=note_date;
        Note=note;

    }

    public long getNote_date() {
        return Note_date;
    }

    public void setNote_date(long note_date) {
        Note_date = note_date;
    }

    public String getNote_title() {
        return Note_title;
    }

    public void setNote_title(String note_title) {
        Note_title = note_title;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
