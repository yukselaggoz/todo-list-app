package com.yukselaggoz.todo_list_application;

import android.app.Dialog;
import android.content.Intent;
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


public class AddNote extends AppCompatActivity {

    EditText et_title,et_note;
    Button btn_next,btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        et_note=findViewById(R.id.et_note_addnote);
        et_title=findViewById(R.id.et_title_addnote);
        btn_back=findViewById(R.id.btn_back_addnote);
        btn_next=findViewById(R.id.btn_next_addnote);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(AddNote.this);

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
                        long tarih=date.getTime();

                        String note_title=et_title.getText().toString();
                        String note=et_note.getText().toString();

                        NoteData nd=new NoteData(note_title,tarih,note);

                        Veritabani db=new Veritabani(AddNote.this);
                        long id=db.AddNoteToDB(nd);

                        if (id==-1)
                            Toast.makeText(AddNote.this, "Error!", Toast.LENGTH_SHORT).show();
                        else {

                            Toast.makeText(AddNote.this, "Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddNote.this,HomeActivity.class);
                            startActivity(intent);
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

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddNote.this,HomeActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(AddNote.this,HomeActivity.class);
        startActivity(intent);
    }
}
