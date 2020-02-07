package com.example.concentration;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class ResultsActivity extends AppCompatActivity {

    final String LOG_TAG = "myLogs"; // logging

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);

        SQLiteDatabase db;
        db = openOrCreateDatabase("resultsDB", Context.MODE_PRIVATE, null);

        Log.d(LOG_TAG, "--- READ the table: ---");
        Cursor c = db.query("results_table", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int flipsColIndex = c.getColumnIndex("flips");

            do {
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", flips = " + c.getInt(flipsColIndex));
                addRow(c.getInt(idColIndex), c.getString(nameColIndex), c.getInt(flipsColIndex));
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
    }


    public void addRow(int id, String name, int flips) {
        TableLayout resultsTableLayout = findViewById(R.id.resultsTable);
        LayoutInflater inflater = LayoutInflater.from(this);
        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);
        TextView tv = tr.findViewById(R.id.id);
        tv.setText(Integer.toString(id));
        tv = tr.findViewById(R.id.name);
        tv.setText(name);
        tv = tr.findViewById(R.id.flips);
        tv.setText(Integer.toString(flips));
        resultsTableLayout.addView(tr);
    }


}
