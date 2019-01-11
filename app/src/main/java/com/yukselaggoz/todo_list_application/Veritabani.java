package com.yukselaggoz.todo_list_application;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;

public class Veritabani extends SQLiteOpenHelper {

    private static final String DB_NAME="my_database";
    private static final String TABLE_NAME="my_table";
    private static final String COLUMN_ID="_id";
    private static final String COLUMN_NOTE_TITLE="note_title";
    private static final String COLUMN_NOTE_DATE="note_date";
    private static final String COLUMN_NOTE="note";
    private static final int DB_VERSION=1;
    public ArrayList<String> results=new ArrayList<String>();
    public Veritabani(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tablo_olustur = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT, " +
                COLUMN_NOTE_DATE + " INTEGER NOT NULL, " +
                COLUMN_NOTE + " TEXT);";
        db.execSQL(tablo_olustur);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        onCreate(db);
    }

    public long AddNoteToDB(NoteData nd){

        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(COLUMN_NOTE_TITLE,nd.getNote_title());
        cv.put(COLUMN_NOTE_DATE,nd.getNote_date());
        cv.put(COLUMN_NOTE,nd.getNote());

        long l=database.insert(TABLE_NAME,null,cv);
        database.close();
        return l;

    }

    public ArrayList<String> databaseToString() {

        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_NOTE_TITLE))!=null){

                String note_title=c.getString(c.getColumnIndex(COLUMN_NOTE_TITLE));

                results.add(note_title);
            }
            c.moveToNext();
        }
        db.close();

        return results;
    }

    public void Delete(String note_title){
        SQLiteDatabase db=this.getWritableDatabase();

        String[] whereArgs={note_title};

        db.delete(TABLE_NAME,COLUMN_NOTE_TITLE + "=?" , whereArgs);
        db.close();
    }

    public void Guncelle(String note_title,String note,long date){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(COLUMN_NOTE_TITLE,note_title);
        cv.put(COLUMN_NOTE,note);
        cv.put(COLUMN_NOTE_DATE,date);

        String[] whereArgs={note_title};
        db.update(TABLE_NAME,cv,COLUMN_NOTE_TITLE+"=?",whereArgs);
        db.close();
    }

    public void UpdateByID(String note_title,int id){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(COLUMN_NOTE_TITLE,note_title);

        db.update(TABLE_NAME,cv,"_id="+id,null);
        db.close();
    }

    public ArrayList<String> DatesFromDatabase() {

        SQLiteDatabase db=getWritableDatabase();
        String query="SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c=db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_NOTE_TITLE))!=null){

                DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                Date date=new Date(c.getLong(c.getColumnIndex(COLUMN_NOTE_DATE)));
                String str_date=df.format(date);

                results.add(str_date);
            }
            c.moveToNext();
        }
        db.close();

        return results;
    }

    public int GetIDFromDatabase(String note_title){
        int id=0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM my_table WHERE note_title like '" + note_title + "'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    id = c.getInt(c.getColumnIndex("_id"));

                } while (c.moveToNext());
            }
        }
        return id;
    }

    public String GetNoteTitleFromDatabase(int id){

        String note_title = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM my_table WHERE _id like '" + id + "'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    note_title = c.getString(c.getColumnIndex("note_title"));

                } while (c.moveToNext());
            }
        }
        return note_title;
    }
}