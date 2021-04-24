package com.kevinkirwansoftware.capsule.fragments;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.ScatterChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.kevinkirwansoftware.capsule.GraphItem;
import com.kevinkirwansoftware.capsule.InventoryAdapter;
import com.kevinkirwansoftware.capsule.MedicineItem;
import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.TimePair;
import com.kevinkirwansoftware.capsule.database.RecurringDbHelper;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;
import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class FragmentGraph extends Fragment {
    private RecyclerView mInventoryRecyclerView;
    private RecyclerView.Adapter mInventoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String TAG = "FragmentInventory";
    private ScatterChart graph;
    private Spinner spinner;
    private List<TimePair> timeList;

    private SQLiteDatabase mRecurringDatabase;
    private RecurringDbHelper mRecurringDbHelper;

    private TextView takenTimeTV, scheduledTimeTV, minutesLateTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<MedicineItem> medicineItems = new ArrayList<>();

        mInventoryRecyclerView = view.findViewById(R.id.inventoryRecyclerView);
        mInventoryRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mInventoryAdapter = new InventoryAdapter(medicineItems);

        mInventoryRecyclerView.setLayoutManager(mLayoutManager);
        mInventoryRecyclerView.setAdapter(mInventoryAdapter);

        graph = view.findViewById(R.id.graph);
        spinner = view.findViewById(R.id.graphSpinner);

        scheduledTimeTV = view.findViewById(R.id.scheduledTimeTV);
        takenTimeTV = view.findViewById(R.id.takenTimeTV);
        minutesLateTV = view.findViewById(R.id.minutesLateTV);

        initializeSpinner();

    }

    private void initializeSpinner(){
        RecurringDbHelper recurringDbHelper = new RecurringDbHelper(getContext());
        mRecurringDatabase = recurringDbHelper.getWritableDatabase();

        List<GraphItem> graphItemList = new ArrayList<>();
        Cursor cursor = getAllItems();
        cursor.moveToFirst();
        String firstTag = null;
        for(int i = cursor.getCount() - 1; i >= 0; i--){
            cursor.moveToPosition(i);
            if(cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE)) == 1){
                GraphItem graphItem = new GraphItem(cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)),
                        cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)));
                graphItemList.add(graphItem);
                firstTag = graphItem.getTag();
            }
        }

        ArrayAdapter<GraphItem> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, graphItemList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        if(firstTag != null){
            initializeChart(firstTag);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GraphItem graphItem = (GraphItem) parent.getSelectedItem();
                initializeChart(graphItem.getTag());
                clearTextViews();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private GraphItem getSelectedItem(View v){
        return  (GraphItem) spinner.getSelectedItem();
    }

    private void populateChart(){

    }

    private void initializeChart(String id){
        List<Entry> entries = new ArrayList<>();
         timeList = ApplicationPreferences.getLatencyList(Objects.requireNonNull(getContext()), id);
        for(int i = 0; i < timeList.size(); i++){
            entries.add(new Entry((float) i + 1, timeList.get(i).getTimeDifferenceMinutes()));
        }

        ScatterDataSet dataSet = new ScatterDataSet(entries, "Minutes late");
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.BLACK);
        dataSet.setScatterShape(ScatterChart.ScatterShape.CIRCLE);

        ScatterData scatterData = new ScatterData(dataSet);
        graph.setData(scatterData);
        graph.invalidate();
        graph.setPinchZoom(false);
        graph.setDoubleTapToZoomEnabled(false);
        graph.setScaleXEnabled(false);
        graph.setScaleYEnabled(false);
        graph.setDrawBorders(true);
        graph.getDescription().setText("Minutes late - last " + timeList.size() + " reminders");
        graph.getDescription().setPosition(500f, 100f);

        graph.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        graph.setDrawGridBackground(false);
        graph.getAxisRight().setDrawLabels(false);
        graph.getAxisRight().setDrawGridLines(false);
        graph.setBackgroundColor(getContext().getResources().getColor(R.color.colorTwo, getContext().getTheme()));

        YAxis yAxis = graph.getAxisLeft();
        yAxis.setLabelCount(5);
        yAxis.setDrawGridLines(false);
        yAxis.setDrawLabels(true);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisMinimum(-5f);

        XAxis xAxis = graph.getXAxis();
        xAxis.setLabelCount(timeList.size());
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        graph.getX();

        graph.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                updateTextViews((int) e.getX() - 1);
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    private void updateTextViews(int position){
        scheduledTimeTV.setText(timeList.get(position).getScheduledTimeString());
        takenTimeTV.setText(timeList.get(position).getDismissedTimeString());
        String minutesLate = Integer.toString(timeList.get(position).getTimeDifferenceMinutes());
        minutesLateTV.setText(minutesLate);
    }

    private void clearTextViews(){
        scheduledTimeTV.setText("-");
        takenTimeTV.setText("-");
        minutesLateTV.setText("-");
    }

    private Cursor getAllItems(){
        return mRecurringDatabase.query(
                RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                RecurringReminderColumns.RecurringReminderEntry.COLUMN_TIMESTAMP + " DESC"
        );

    }
}
