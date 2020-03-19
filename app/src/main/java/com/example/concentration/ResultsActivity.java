package com.example.concentration;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    boolean isEmpty = true;
    Button backButton, mainMenu;
    private Map<String, Double> arrayPercents = new HashMap<>();
    private Map<String, Integer> arrayFlips = new HashMap<>();
    private Map<String, Integer> arrayPoints = new HashMap<>();
    private ArrayList<Button> buts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results_layout);

        backButton = findViewById(R.id.backButton);
        init();

        boolean flag = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            flag = bundle.getBoolean("Results");

        if (flag) {
            mainMenu.setVisibility(View.VISIBLE);
            backButton.setVisibility(View.INVISIBLE);
        }
        else {
            mainMenu.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.VISIBLE);
        }

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ResultsActivity.this, HomeActivity.class);
                overridePendingTransition(R.anim.activity_down_up_enter, R.anim.slow_appear);
                startActivity(intent);
            }
        };
        backButton.setOnClickListener(onClickListener);
        mainMenu.setOnClickListener(onClickListener);



        final SQLiteDatabase db;
        db = openOrCreateDatabase("GameRes", Context.MODE_PRIVATE, null);

        try {
            final Cursor c = db.query("GameRes", null, null, null, null, null, null);
            if (c.moveToFirst()) {
                isEmpty = false;
                int nameColIndex = c.getColumnIndex("Name");
                int percents = c.getColumnIndex("Percents");
                int flips = c.getColumnIndex("Flips");
                int points = c.getColumnIndex("Points");

                do {
                    arrayPercents.put(c.getString(nameColIndex), c.getDouble(percents));
                    arrayFlips.put(c.getString(nameColIndex), c.getInt(flips));
                    arrayPoints.put(c.getString(nameColIndex), c.getInt(points));
                } while (c.moveToNext());
            } else isEmpty = true;
            c.close();

            if (!isEmpty) {

                final List<Map.Entry<String, Double>> sortList = new ArrayList(arrayPercents.entrySet());
                Collections.sort(sortList, new Comparator<Map.Entry<String, Double>>() {
                    @Override
                    public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                final int size = Math.min(arrayPercents.size(), 10);

                for (int index = 1; index <= size; index++) {
                    addRow(index, sortList.get(index - 1).getKey(), arrayPercents.get(sortList.get(index - 1).getKey()));
                    buts.get(index - 1).setVisibility(View.VISIBLE);
                }


                OnClickListener listener = new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index;
                        for (index = 0; index < size; index++) {
                            if (v.getId() == buts.get(index).getId()) break;
                        }
                        String sorted = sortList.get(index).getKey();
                        displayInformation(sorted, arrayFlips.get(sorted), arrayPoints.get(sorted));
                    }
                };
                for (int i = 0; i < size; i++)
                    buts.get(i).setOnClickListener(listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TO DO: Create an array of info buttons, which will appear in each row of Results Table
     * to show the information of every person who plays that game:
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

    private void init() {
        buts.add((Button)findViewById(R.id.button00));
        buts.add((Button)findViewById(R.id.button01));
        buts.add((Button)findViewById(R.id.button02));
        buts.add((Button)findViewById(R.id.button03));
        buts.add((Button)findViewById(R.id.button04));
        buts.add((Button)findViewById(R.id.button05));
        buts.add((Button)findViewById(R.id.button06));
        buts.add((Button)findViewById(R.id.button07));
        buts.add((Button)findViewById(R.id.button08));
        buts.add((Button)findViewById(R.id.button09));
        mainMenu = findViewById(R.id.mainMenu);
    }

    private void displayInformation(String name, int flips, int points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(name);
        builder.setMessage("Flips: " + flips + "\nPoints: " + points);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
