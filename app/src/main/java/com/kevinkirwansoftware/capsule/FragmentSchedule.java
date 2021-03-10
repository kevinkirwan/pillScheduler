package com.kevinkirwansoftware.capsule;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentSchedule extends Fragment {
    private SQLiteDatabase mReminderDatabase;
    ArrayList<ScheduleItem> mScheduleItems;

    private View scheduleView;

    private boolean timerNeeded = true, addReminderInfoNeeded = false;

    private LinearLayout addReminderInfoLL;
    private RecyclerView mScheduleRecyclerView;
    private ScheduleAdapter mScheduleAdapter;
    private RecyclerView.LayoutManager mScheduleLayoutManager;

    ArrayList<ScheduleItem> scheduleList = new ArrayList<>();
    private int dailyReminderCounter = 1;
    private int addReminderInfoCounter = 0;
    private int addReminderVisibilityCounter = 40;

    private ImageView addReminderButton, addReminderArrowIV;
    private TextView addReminderArrowTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        scheduleView = inflater.inflate(R.layout.fragment_schedule, container, false);
        fragmentScheduleInit();
        return scheduleView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getScheduleItemsFromDb();
        timingHandler();

        mScheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView);
        mScheduleRecyclerView.setHasFixedSize(true);
        mScheduleLayoutManager = new LinearLayoutManager(getContext());
        mScheduleAdapter = new ScheduleAdapter(mScheduleItems);

        mScheduleRecyclerView.setLayoutManager(mScheduleLayoutManager);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);

        addReminderInfoLL = view.findViewById(R.id.addReminderInfoLL);
        addReminderArrowTV = view.findViewById(R.id.addReminderArrowTV);
        addReminderArrowIV = view.findViewById(R.id.addReminderArrowIV);

        mScheduleAdapter.setOnItemClickListener(
                new ScheduleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        toggleMenu(position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        showDeleteOption(position);
                    }

                    @Override
                    public void onEditClick(int position) {
                        launchEditMenu(position);
                    }
                }
        );

        mScheduleRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    addReminderButton.setVisibility(View.INVISIBLE);
                    addReminderVisibilityCounter = 0;
                } else {
                    addReminderButton.setVisibility(View.VISIBLE);
                }
                timerNeeded = true;
            }
        });
    }

    public void toggleMenu(int position){
        for(int i = 0; i < mScheduleItems.size(); i++){
            if(mScheduleItems.get(i).isMenuVisible()){
                mScheduleItems.get(i).setMenuVisible(false);
                mScheduleAdapter.notifyItemChanged(i);
            }
        }

        mScheduleItems.get(position).setMenuVisible(true);
        mScheduleAdapter.notifyItemChanged(position);

    }

    public void showDeleteOption(int position){
        removeItem(position);
    }

    public void launchEditMenu(int position){
        final ScheduleDialog scheduleDialog = new ScheduleDialog(getContext(), ScheduleDialog.SchedulePopOutType.EDIT, mScheduleItems.get(position));
        scheduleDialog.show();
        scheduleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if((scheduleDialog.getScheduleItem() != null) && scheduleDialog.getUpdateNeeded()){
                    mScheduleItems.add(scheduleDialog.getScheduleItem());
                    mScheduleAdapter.notifyItemInserted(mScheduleItems.size());
                    ApplicationFlags.SetReminderDatasetItemAddedFlag((scheduleDialog.getScheduleItem().getScheduleID()));
                    Log.d("Kevin", "New Item UUID" + scheduleDialog.getScheduleItem().getScheduleID());
                }
            }
        });
    }

    private void getScheduleItemsFromDb(){
        mScheduleItems = new ArrayList<>();
        Cursor cursor = getAllItems();
        if(cursor.getCount() == 0){
            addReminderInfoNeeded = true;
            return;
        }
        int j = 0;
        Log.d("Kevin", "Count: " + cursor.getCount());
        for(int i = cursor.getCount() - 1; i >= 0; i--){
            Log.d("Kevin", "DB row: " + i + " AR row: " + j);
            cursor.moveToPosition(i);

            int rt_int = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_TYPE));
            ScheduleItem.ReminderType rt_enum;

            switch (rt_int){
                case 0:
                    rt_enum = ScheduleItem.ReminderType.ONE_TIME;
                    mScheduleItems.add(new ScheduleItem());
                    break;
                case 1:
                    rt_enum = ScheduleItem.ReminderType.RECURRING;
                    mScheduleItems.add(new RecurringReminder());
                    int[][] tempArray = new int[2][4];

                    Log.d("Kevin", "Index: " + cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_HOUR_TWO));
                    tempArray[0][0] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_HOUR_ONE));
                    tempArray[1][0] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_MINUTE_ONE));
                    tempArray[0][1] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_HOUR_TWO));
                    tempArray[1][1] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_MINUTE_TWO));
                    tempArray[0][2] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_HOUR_THREE));
                    tempArray[1][2] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_MINUTE_THREE));
                    tempArray[0][3] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_HOUR_FOUR));
                    tempArray[1][3] = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_REMINDER_MINUTE_FOUR));
                     /*
                    tempArray[0][0] = 0;
                    tempArray[1][0] = 0;
                    tempArray[0][1] = 0;
                    tempArray[1][1] = 0;
                    tempArray[0][2] = 0;
                    tempArray[1][2] = 0;
                    tempArray[0][3] = 0;
                    tempArray[1][3] = 0;

                      */
                    ((RecurringReminder) mScheduleItems.get(j)).setMultiRemindersArray(tempArray);
                    ((RecurringReminder) mScheduleItems.get(j)).setDailyReminders(cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_DAILY_REMINDERS)));

                    break;
                default:
                    rt_enum = ScheduleItem.ReminderType.NONE;
                    Toast.makeText(getContext(),"Kevin THIS SHOULD NOT BE POSSIBLE", Toast.LENGTH_SHORT).show();

            }
            mScheduleItems.get(j).setReminderType(rt_enum);




            mScheduleItems.get(j).setReminderName(cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_NAME)));
            mScheduleItems.get(j).setReminderDescription(cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_DESCRIPTION)));
            mScheduleItems.get(j).setScheduleID(cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID)));


            ScheduleItem testItem = mScheduleItems.get(j);
            j++;

        }
    }


    private void removeItem(int position){
        ApplicationFlags.SetReminderDatasetItemRemovedFlag(mScheduleItems.get(position).getScheduleID());
        Log.d("Kevin", "Deleted Items: ");
        for (int i = 0; i < ApplicationFlags.GetRemindersRemovedList().size(); i++){
            Log.d("Kevin", "Removed name: " + ApplicationFlags.GetRemindersRemovedList().get(i));
        }
        mScheduleItems.remove(position);
        mScheduleAdapter.notifyItemRemoved(position);
    }

    private void fragmentScheduleInit(){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        mReminderDatabase = dbHelper.getWritableDatabase();

        addReminderButton = scheduleView.findViewById(R.id.addReminderButton);
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchNewReminderPopOut();
                addReminderInfoNeeded = false;
                final ScheduleDialog scheduleDialog = new ScheduleDialog(getContext(), ScheduleDialog.SchedulePopOutType.NEW, null);
                scheduleDialog.show();
                scheduleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if((scheduleDialog.getScheduleItem() != null) && scheduleDialog.getUpdateNeeded()){
                            mScheduleItems.add(scheduleDialog.getScheduleItem());
                            mScheduleAdapter.notifyItemInserted(mScheduleItems.size());
                            ApplicationFlags.SetReminderDatasetItemAddedFlag(scheduleDialog.getScheduleItem().getScheduleID());
                            Log.d("Kevin", "New Item UUID" + scheduleDialog.getScheduleItem().getScheduleID());
                        }
                    }
                });

            }
        });
    }

    private void loadScheduleItemsToDatabase(){
        if(!ApplicationFlags.GetReminderDatasetNeedsUpdate()){
            ApplicationFlags.ResetReminderDatasetFlags();
            return;
        }
        int flagCounter = 0;
        boolean dbResetNeeded = false;
        if(ApplicationFlags.GetReminderDatasetItemAdded()){
            Log.d("Kevin", "ADDED 1");
            flagCounter++;
        }
        if(ApplicationFlags.GetReminderDatasetItemRemoved()){
            Log.d("Kevin", "REMOVED 1");
            flagCounter++;
        }
        if(ApplicationFlags.GetReminderDatasetItemChanged()){
            Log.d("Kevin", "CHANGED 1");
            flagCounter++;
        }

        if(flagCounter > 1){
            dbResetNeeded = true;
        }

        if(!dbResetNeeded){
            if(ApplicationFlags.GetReminderDatasetItemAdded()){
                Log.d("Kevin", "ADDED");
                for(int i = 0; i < ApplicationFlags.GetRemindersAddedList().size(); i++){
                    Log.d("Kevin", "ADDED " + i);
                    for(int j = 0; j < mScheduleItems.size(); j++){
                        if(ApplicationFlags.GetRemindersAddedList().get(i).equals(mScheduleItems.get(j).getScheduleID())){
                            ContentValues cv = new ContentValues();
                            ScheduleItem holderItem = mScheduleItems.get(j);
                            if(mScheduleItems.get(j) instanceof RecurringReminder){
                                assert holderItem instanceof RecurringReminder;
                                int[][] tempArray = holderItem.getMultiRemindersArray();
                                cv.put(ReminderColumns.ReminderEntry.COLUMN_REMINDER_HOUR_ONE, holderItem.getReminderName());
                            }

                            cv.put(ReminderColumns.ReminderEntry.COLUMN_NAME, holderItem.getReminderName());
                            cv.put(ReminderColumns.ReminderEntry.COLUMN_DESCRIPTION, holderItem.getReminderDescription());
                            cv.put(ReminderColumns.ReminderEntry.COLUMN_TYPE, holderItem.getTypeInt());
                            cv.put(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());

                            mReminderDatabase.insert(ReminderColumns.ReminderEntry.TABLE_NAME, null, cv);
                            Log.d("Kevin", "Added: " + holderItem.getReminderName());
                        }
                    }
                }
            }
            if(ApplicationFlags.GetReminderDatasetItemRemoved()){
                Cursor cursor = getAllItems();
                cursor.moveToFirst();
                for(int i = 0; i < ApplicationFlags.GetRemindersRemovedList().size(); i++){
                    Log.d("Kevin", "Item removed: " + ApplicationFlags.GetRemindersRemovedList().get(i));
                    for(int j = 0; j < cursor.getCount(); j++){
                        cursor.moveToPosition(j);
                        Log.d("Kevin: " , "AR val: " + ApplicationFlags.GetRemindersRemovedList() + " DB val: " + cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID)));
                        if(cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID)).equals(ApplicationFlags.GetRemindersRemovedList().get(i))){
                            long id = cursor.getLong(cursor.getColumnIndex(ReminderColumns.ReminderEntry._ID));
                            mReminderDatabase.delete(ReminderColumns.ReminderEntry.TABLE_NAME,
                                    ReminderColumns.ReminderEntry._ID + "=" + id,
                                    null);
                        }
                    }
                }
            }
            if(ApplicationFlags.GetReminderDatasetItemChanged()){


            }
        } else {
            Cursor cursor = getAllItems();
            for(int i = cursor.getCount() - 1; i >= 0; i--){
                cursor.moveToPosition(i);
                long id = cursor.getLong(cursor.getColumnIndex(ReminderColumns.ReminderEntry._ID));
                mReminderDatabase.delete(ReminderColumns.ReminderEntry.TABLE_NAME,
                        ReminderColumns.ReminderEntry._ID + "=" + id,
                        null);
            }
            for(int j = mScheduleItems.size() - 1; j >= 0; j--){
                    ContentValues cv = new ContentValues();
                    ScheduleItem holderItem = mScheduleItems.get(j);
                    cv.put(ReminderColumns.ReminderEntry.COLUMN_NAME, holderItem.getReminderName());
                    cv.put(ReminderColumns.ReminderEntry.COLUMN_DESCRIPTION, holderItem.getReminderDescription());
                    cv.put(ReminderColumns.ReminderEntry.COLUMN_TYPE, holderItem.getTypeInt());
                    cv.put(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());

                    mReminderDatabase.insert(ReminderColumns.ReminderEntry.TABLE_NAME, null, cv);
            }


        }




        if(ApplicationFlags.GetRemindersChangedList() != null){
            for(int i = 0; i < ApplicationFlags.GetRemindersChangedList().size(); i++){
                String currentID = ApplicationFlags.GetRemindersChangedList().get(i);
            }
        }


        Cursor cursor = getAllItems();
        cursor.moveToFirst();
        for(int i = 0; i < scheduleList.size(); i++){
            if(i < cursor.getCount()){
                cursor.moveToPosition(i);
                String dbID = cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID));
                if(!dbID.equals(scheduleList.get(i).getScheduleID()) || (dbID == null)){
                    ContentValues cv = new ContentValues();
                    ScheduleItem holderItem = scheduleList.get(i);
                    cv.put(ReminderColumns.ReminderEntry.COLUMN_NAME, holderItem.getReminderName());
                    cv.put(ReminderColumns.ReminderEntry.COLUMN_DESCRIPTION, holderItem.getReminderDescription());
                    cv.put(ReminderColumns.ReminderEntry.COLUMN_TYPE, holderItem.getTypeInt());
                    // DELETE AFTER FIRST RUN
                    if(holderItem.getScheduleID() == null){
                        cv.put(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID, Integer.toString(i));
                    } else {
                        cv.put(ReminderColumns.ReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());
                    }
                    mReminderDatabase.replace(ReminderColumns.ReminderEntry.TABLE_NAME, null, cv);
                    mReminderDatabase.insert(ReminderColumns.ReminderEntry.TABLE_NAME, null, cv);
                }
            }
        }
        //mReminderDatabase.insert(ReminderColumns.ReminderEntry.TABLE_NAME, null, cv);
        ApplicationFlags.ResetReminderDatasetFlags();
    }


    private void timingHandler(){
        final Handler timeDateHandler = new Handler();
        Timer timeDateScheduler = new Timer();
        final TimerTask timeDateUpdate = new TimerTask() {
            public void run() {
                timeDateHandler.post(new Runnable() {
                    public void run() {
                        if (timerNeeded) {
                            if (addReminderVisibilityCounter < 40)  {
                                if(addReminderButton.getVisibility() == View.VISIBLE){
                                    addReminderButton.setVisibility(View.INVISIBLE);
                                }
                                addReminderVisibilityCounter++;
                            } else {
                                addReminderButton.setVisibility(View.VISIBLE);
                            }

                            // Logic to display flashing Add Reminder Button
                            if (addReminderInfoNeeded){
                                addReminderInfoLL.setVisibility(View.VISIBLE);
                                int testColor = getContext().getResources().getColor(R.color.colorSecondary);
                            if (addReminderInfoCounter < 2250) {
                                if ((addReminderInfoCounter % 500) < 250) {
                                    addReminderArrowIV.setImageAlpha(250 - (addReminderInfoCounter % 500));
                                    addReminderArrowTV.setTextColor(Color.argb(250 - (addReminderInfoCounter % 500), Color.red(testColor), Color.green(testColor), Color.blue(testColor)));
                                } else {
                                    addReminderArrowIV.setImageAlpha(addReminderInfoCounter % 250);
                                    addReminderArrowTV.setTextColor(Color.argb(addReminderInfoCounter % 250, Color.red(testColor), Color.green(testColor), Color.blue(testColor)));
                                }
                                addReminderInfoCounter = addReminderInfoCounter + 10;
                            } else if (addReminderInfoCounter == 2250) {
                                addReminderInfoLL.setVisibility(View.GONE);
                                addReminderInfoNeeded = false;
                            }
                        } else {
                                addReminderInfoLL.setVisibility(View.GONE);
                            }

                    }
                    }
                });
            }
        };
        timeDateScheduler.scheduleAtFixedRate(timeDateUpdate, 100, 50);

    }

    private Cursor getAllItems(){
        return mReminderDatabase.query(
                ReminderColumns.ReminderEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                ReminderColumns.ReminderEntry.COLUMN_TIMESTAMP + " DESC"
        );

    }

    @Override
    public void onDestroyView() {
        Log.d("Kevin", "View Destroyed");
        timerNeeded = false;
        loadScheduleItemsToDatabase();
        super.onDestroyView();
    }


}
