package com.example.concentration;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.concentration.Menu.HomeActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultsActivity extends AppCompatActivity {

    boolean isEmpty = true;

    Button backButton;
    Map<String, Integer> resArrayOfFlips = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);

        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.alpha3);

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, HomeActivity.class);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        });


        SQLiteDatabase db;
        db = openOrCreateDatabase("resultsDB", Context.MODE_PRIVATE, null);

        Cursor c = db.query("results_table", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            isEmpty = false;
            int nameColIndex = c.getColumnIndex("name");
            int flipsColIndex = c.getColumnIndex("flips");

            do {
                resArrayOfFlips.put(c.getString(nameColIndex), c.getInt(flipsColIndex));
            } while (c.moveToNext());
        } else isEmpty = true;
        c.close();

        if (!isEmpty) {
            List<Map.Entry<String, Integer>> sortList = new ArrayList(resArrayOfFlips.entrySet());
            Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

            int size;
            if (resArrayOfFlips.size() < 10) size = resArrayOfFlips.size();
            else size = 10;

            System.out.println(size);

            for (int index = 1; index <= size; index++) {
                addRow(index, sortList.get(index - 1).getKey(), resArrayOfFlips.get(sortList.get(index - 1).getKey()));
            }
        }
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
