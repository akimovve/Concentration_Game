package com.example.concentration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    boolean isEmpty = true;
    Button backButton;
    private Map<String, Double> resArrayOfFlips = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);

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
        db = openOrCreateDatabase("ChallengeResults", Context.MODE_PRIVATE, null);

        try {
            Cursor c = db.query("ChallengeResults", null, null, null, null, null, null);
            if (c.moveToFirst()) {
                isEmpty = false;
                int nameColIndex = c.getColumnIndex("Name");
                int flipsColIndex = c.getColumnIndex("Percents");

                do {
                    resArrayOfFlips.put(c.getString(nameColIndex), c.getDouble(flipsColIndex));
                } while (c.moveToNext());
            } else isEmpty = true;
            c.close();

            if (!isEmpty) {
                List<Map.Entry<String, Double>> sortList = new ArrayList(resArrayOfFlips.entrySet());
                Collections.sort(sortList, new Comparator<Map.Entry<String, Double>>() {
                    @Override
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                int size = Math.min(resArrayOfFlips.size(), 10);

                System.out.println(size);

                for (int index = 1; index <= size; index++) {
                    addRow(index, sortList.get(index - 1).getKey(), resArrayOfFlips.get(sortList.get(index - 1).getKey()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TO DO: Create an array of info buttons, which will appear in each row of Results Table
     * to show the the information of every person who plays that game:
     * amount of flips, amount of points, time
     */

    @SuppressLint("SetTextI18n")
    public void addRow(int id, String name, Double percents) {
        TableLayout resultsTableLayout = findViewById(R.id.resultsTable);
        LayoutInflater inflater = LayoutInflater.from(this);
        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);
        TextView tv = tr.findViewById(R.id.id);
        tv.setText(Integer.toString(id));
        tv = tr.findViewById(R.id.name);
        tv.setText(name);
        tv = tr.findViewById(R.id.percents);
        tv.setText(percents + " %");
        resultsTableLayout.addView(tr);
    }
}
