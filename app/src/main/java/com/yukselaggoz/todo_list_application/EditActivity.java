package com.yukselaggoz.todo_list_application;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    public static String str_date,note;
    EditText et_title_edit,et_note_edit;
    Button btn_next_edit,btn_back_edit;
    public static long tarih;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        et_title_edit=findViewById(R.id.et_title_edit);
        et_note_edit=findViewById(R.id.et_note_edit);
        btn_next_edit=findViewById(R.id.btn_next_edit);
        btn_back_edit=findViewById(R.id.btn_back_edit);

        Bundle gelenVeri = getIntent().getExtras();
        final CharSequence gelenYazi = gelenVeri.getCharSequence("anahtar");
        final String gelenYazi_str = gelenYazi.toString();

        et_title_edit.setText(gelenYazi_str);

        Veritabani vt=new Veritabani(EditActivity.this);
        SQLiteDatabase db=vt.getReadableDatabase();
        Cursor c=db.rawQuery("SELECT * FROM my_table WHERE note_title like '" + gelenYazi_str + "'",null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
                    Date date=new Date(c.getLong(c.getColumnIndex("note_date")));
                    str_date=df.format(date);
                    note = c.getString(c.getColumnIndex("note"));

                } while (c.moveToNext());
            }
        }
        et_note_edit.setText(note);

        btn_next_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(EditActivity.this);

                dialog.setContentView(R.layout.date_dialog);

                final DatePicker datePicker=dialog.findViewById(R.id.datePicker);
                Button btn_save_dialog=dialog.findViewById(R.id.btn_save_dialog);
                Button btn_back_dialog=dialog.findViewById(R.id.btn_back_dialog);
                dialog.setTitle("Enter date");

                btn_save_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int gun=datePicker.getDayOfMonth();
                        int ay=datePicker.getMonth()+1;
                        int yil=datePicker.getYear();

                        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                        Date date=null;
                        try {
                            date=dateFormat.parse(gun+"/"+ay+"/"+yil);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tarih=date.getTime();

                        Veritabani db=new Veritabani(EditActivity.this);

                        String note_title=et_title_edit.getText().toString();
                        String note=et_note_edit.getText().toString();

                        if(note_title.equalsIgnoreCase(gelenYazi_str)) {
                            db.Guncelle(gelenYazi_str, note, tarih);
                            Toast.makeText(EditActivity.this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            NoteData noteData = new NoteData(note_title, tarih, note);
                            db.Delete(gelenYazi_str);
                            long id=db.AddNoteToDB(noteData);
                            if (id == -1)
                                Toast.makeText(EditActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(EditActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }

                        dialog.dismiss();
                    }
                });

                btn_back_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        btn_back_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(EditActivity.this,HomeActivity.class);
        startActivity(intent);

    }
}
