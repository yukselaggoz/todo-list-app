package com.yukselaggoz.todo_list_application;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    public static String[] array, date_array;

    public static long[] long_dates;

    public static String str_date, note, note_from_date;

    ListView listView;

    public static Date deadline_date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.listview);

        Veritabani db = new Veritabani(HomeActivity.this);

        ArrayList<String> noteDataList;
        noteDataList = db.databaseToString();

        array = new String[noteDataList.size()];
        noteDataList.toArray(array);

        CheckDeadlineDate();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_view, array);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String note_title = array[position];

                Veritabani vt = new Veritabani(HomeActivity.this);
                SQLiteDatabase db = vt.getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM my_table WHERE note_title like '" + note_title + "'", null);
                if (c != null) {
                    if (c.moveToFirst()) {
                        do {

                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date(c.getLong(c.getColumnIndex("note_date")));
                            str_date = df.format(date);
                            note = c.getString(c.getColumnIndex("note"));

                        } while (c.moveToNext());
                    }
                }

                final Dialog dialog = new Dialog(HomeActivity.this);
                dialog.setContentView(R.layout.show_note);
                Button btn_ok_dialog = dialog.findViewById(R.id.btn_ok_dialog);
                TextView tv_note_dialog = dialog.findViewById(R.id.tv_note_dialog);
                TextView tv_date_dialog = dialog.findViewById(R.id.tv_date_dialog);

                tv_date_dialog.setText(str_date);
                tv_note_dialog.setText(note);
                dialog.setTitle(note_title);
                btn_ok_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_id:
                Intent intent = new Intent(HomeActivity.this, AddNote.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.delete_id:

                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                TextView textView = (TextView) menuInfo.targetView;
                String string = textView.getText().toString();

                Veritabani db = new Veritabani(HomeActivity.this);
                db.Delete(string);

                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
                break;

            case R.id.first_id:

                AdapterView.AdapterContextMenuInfo menuInfo3 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                TextView textView3 = (TextView) menuInfo3.targetView;
                String string3 = textView3.getText().toString();

                MakeFirst(string3);

                Intent intent3 = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent3);

                break;

            case R.id.edit_id:

                Intent intent2 = new Intent(HomeActivity.this, EditActivity.class);
                final AdapterView.AdapterContextMenuInfo menuInfo2 = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                final TextView textView2 = (TextView) menuInfo2.targetView;

                CharSequence cs = textView2.getText();
                intent2.putExtra("anahtar", cs);
                startActivity(intent2);

                break;

        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit").setMessage("Do you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }).setNegativeButton("No", null).show();
    }

    public Date DeadlineDate(String note_title) {

        Veritabani vt = new Veritabani(HomeActivity.this);
        SQLiteDatabase db = vt.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM my_table WHERE note_title like '" + note_title + "'", null);
        if (c != null) {
            if (c.moveToFirst()) {
                do {

                    deadline_date = new Date(c.getLong(c.getColumnIndex("note_date")));

                } while (c.moveToNext());
            }
        }
        return deadline_date;
    }

    public void CheckDeadlineDate() {

        Veritabani db = new Veritabani(HomeActivity.this);

        for (int i = 0; i < array.length; i++) {

            Date current_time = Calendar.getInstance().getTime();
            if (current_time.after(DeadlineDate(array[i]))) {

                db.Delete(array[i]);
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        }
    }

    public void MakeFirst(String note_title) {

        int[] id_array = new int[array.length];
        String[] title_array = new String[array.length];

        Veritabani vt = new Veritabani(HomeActivity.this);

        for (int i = 0; i < array.length; i++) {
            id_array[i] = vt.GetIDFromDatabase(array[i]);
        }

        for (int i = 0; i < array.length; i++)
            title_array[i] = array[i];

        for (int i = 0; i < array.length; i++) {

        }
    }
}