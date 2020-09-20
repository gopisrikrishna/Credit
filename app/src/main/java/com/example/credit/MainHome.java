package com.example.credit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainHome extends AppCompatActivity {
    SQLiteDatabase db;
    String[] name, email, balance, transactionString;
    ListView ls;

    @Override
    protected void onStart() {
        super.onStart();
        if (db.isOpen()) {
            Cursor result = db.rawQuery("SELECT * FROM account", null);
            int i = 0;
            name = new String[result.getCount()];
            email = new String[result.getCount()];
            balance = new String[result.getCount()];

            while (result.moveToNext()) {
                name[i] = result.getString(1);
                email[i] = result.getString(0);
                balance[i] = String.valueOf(result.getInt(2));
                i++;
            }
            result.close();
        }
        MyAdapter adapter = new MyAdapter(this, name, email, balance);
        ls.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ls = findViewById(R.id.listview);
        db = openOrCreateDatabase("Student", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS account(Email TEXT PRIMARY KEY , name TEXT , balance int);");
        db.execSQL("CREATE TABLE IF NOT EXISTS tt(sender VARCHAR ,receiver VARCHAR,amount VARCHAR);");

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainHome.this, Transaction.class);
                i.putExtra("id", email[position]);
                i.putExtra("name", name[position]);
                i.putExtra("bal", balance[position]);
                i.putExtra("emails", email);
                i.putExtra("names", name);
                startActivity(i);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            helo();
            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

    }

    public void helo() {
        db.execSQL("INSERT INTO account  VALUES('Gopi.y153@diet.ac.in','Gopi',500.00)");
        db.execSQL("INSERT INTO account  VALUES('Karthik@diet.ac.in','Karthik',500.00)");
        db.execSQL("INSERT INTO account  VALUES('Lohith@diet.ac.in','Lohith',500.00)");
        db.execSQL("INSERT INTO account  VALUES('Likhith@diet.ac.in','Likhith',500.00)");
        db.execSQL("INSERT INTO account  VALUES('Rohith@diet.ac.in','Rohith',500.00)");
        db.execSQL("INSERT INTO account  VALUES('Jayanth@diet.ac.in','Revenger',500.00)");
        db.execSQL("INSERT INTO account  VALUES('User1@email.com','User1',500.00)");
        db.execSQL("INSERT INTO account  VALUES('User2@email.com','User2',500.00)");
        db.execSQL("INSERT INTO account  VALUES('User3@email.com','User3',500.00)");


    }

    public void rem(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainHome.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.transaction_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Transactions");
        ListView lv = convertView.findViewById(R.id.lv1);


        if (db.isOpen()) {
            Cursor result = db.rawQuery("SELECT * FROM tt", null);
            transactionString = new String[result.getCount()];
            int x = 0;
            while (result.moveToNext()) {
                transactionString[x] = "\nTransferred " + result.getString(2) + " credits from " + result.getString(0) + " to " + result.getString(1) + "\n";
                x++;
            }
            result.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, transactionString);
        lv.setAdapter(adapter);
        alertDialog.show();

    }
}