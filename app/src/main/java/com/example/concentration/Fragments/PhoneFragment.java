package com.example.concentration.Fragments;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.concentration.DataSave.DataBaseHelper;
import com.example.concentration.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneFragment extends Fragment {

    private Map<String, Double> arrayPercents = new HashMap<>();
    private Map<String, Integer> arrayFlips = new HashMap<>();
    private Map<String, Integer> arrayPoints = new HashMap<>();
    private List<Map.Entry<String, Double>> sortList;
    private TableLayout resultsTableLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone, container, false);
        resultsTableLayout = view.findViewById(R.id.resTable);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SQLiteOpenHelper dbHelper = new DataBaseHelper(getActivity(), "GameRes", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("GameRes", null, null, null, null, null, null);

        boolean isEmpty;
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

            sortList = new ArrayList(arrayPercents.entrySet());
            Collections.sort(sortList, new Comparator<Map.Entry<String, Double>>() {
                @Override
                public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            final int size = Math.min(arrayPercents.size(), 10);

            for (int index = 1; index <= size; index++) {
                addRow(index, sortList.get(index - 1).getKey(), arrayPercents.get(sortList.get(index - 1).getKey()));
            }
        }

    }

    private void addRow(final int id, String name, Double percents) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        TableRow tr = (TableRow) inflater.inflate(R.layout.table_row, null);
        TextView tv = tr.findViewById(R.id.id);
        tv.setText(Integer.toString(id));
        tv = tr.findViewById(R.id.name);
        tv.setText(name);
        tv = tr.findViewById(R.id.percents);
        tv.setText(percents + " %");
        resultsTableLayout.addView(tr);
        tr.setClickable(true);
        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(id-1);
                String sorted = sortList.get(id - 1).getKey();
                displayInformation(sorted, arrayFlips.get(sorted), arrayPoints.get(sorted));
            }
        });
    }

    private void displayInformation(String name, int flips, int points) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
