package com.kevinkirwansoftware.capsule.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kevinkirwansoftware.capsule.notifications.NotificationHelper;
import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.database.RecurringDbHelper;
import com.kevinkirwansoftware.capsule.RecurringReminder;
import com.kevinkirwansoftware.capsule.database.RecurringReminderColumns;
import com.kevinkirwansoftware.capsule.notifications.ReminderBroadcast;
import com.kevinkirwansoftware.capsule.ScheduleAdapter;
import com.kevinkirwansoftware.capsule.ScheduleDialog;
import com.kevinkirwansoftware.capsule.ScheduleItem;
import com.kevinkirwansoftware.capsule.SingleReminder;
import com.kevinkirwansoftware.capsule.general.ApplicationFlags;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class FragmentSchedule extends Fragment {
    private static String TAG = "FragmentSchedule.java";
    private SQLiteDatabase mRecurringDatabase;
    private ArrayList<ScheduleItem> mScheduleItems;

    private View scheduleView;

    private boolean timerNeeded = true, addReminderInfoNeeded = false;

    private LinearLayout addReminderInfoLL;
    private RecyclerView mScheduleRecyclerView;
    private ScheduleAdapter mScheduleAdapter;
    private RecyclerView.LayoutManager mScheduleLayoutManager;

    private int dailyReminderCounter = 1;
    private int addReminderInfoCounter = 0;
    private int addReminderVisibilityCounter = 40;

    private ImageView addReminderButton, addReminderArrowIV;
    private TextView addReminderArrowTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        scheduleView = inflater.inflate(R.layout.fragment_schedule, container, false);
        return scheduleView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentScheduleInit();
        getScheduleItemsFromDb();
        timingHandler();

        mScheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView);
        mScheduleRecyclerView.setHasFixedSize(true);
        mScheduleLayoutManager = new LinearLayoutManager(getContext());
        mScheduleItems = new ArrayList<>();
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
                /*
                if((scheduleDialog.getScheduleItem() != null) && scheduleDialog.getUpdateNeeded()){
                    mScheduleItems.add(scheduleDialog.getScheduleItem());
                    mScheduleAdapter.notifyItemInserted(mScheduleItems.size());
                    ApplicationFlags.SetReminderDatasetItemAddedFlag((scheduleDialog.getScheduleItem().getScheduleID()));
                    Log.d("Kevin", "New Item UUID" + scheduleDialog.getScheduleItem().getScheduleID());
                }

                 */
            }
        });


    }

    private void getScheduleItemsFromDb(){
        if(mScheduleItems == null){
            mScheduleItems = new ArrayList<>();
        }

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

            int rt_int = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_TYPE));
            ScheduleItem.ReminderType rt_enum;

            switch (rt_int){
                case 0:
                    rt_enum = ScheduleItem.ReminderType.ONE_TIME;
                    mScheduleItems.add(new SingleReminder(
                            cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_YEAR)),
                            cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MONTH)),
                            cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAY)),
                            cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_HOUR)),
                            cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_MINUTE))
                    ));
                    break;
                case 1:
                    rt_enum = ScheduleItem.ReminderType.RECURRING;
                    mScheduleItems.add(new RecurringReminder());
                    int[][] tempArray = new int[2][4];

                    tempArray[0][0] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_ONE));
                    tempArray[1][0] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_ONE));
                    tempArray[0][1] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_TWO));
                    tempArray[1][1] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_TWO));
                    tempArray[0][2] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_THREE));
                    tempArray[1][2] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_THREE));
                    tempArray[0][3] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_HOUR_FOUR));
                    tempArray[1][3] = cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_REMINDER_MINUTE_FOUR));
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
                    ((RecurringReminder) mScheduleItems.get(j)).setDailyReminders(cursor.getInt(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DAILY_REMINDERS)));

                    break;
                default:
                    mScheduleItems.add(new ScheduleItem());
                    rt_enum = ScheduleItem.ReminderType.NONE;
                    Toast.makeText(getContext(),"Kevin THIS SHOULD NOT BE POSSIBLE", Toast.LENGTH_SHORT).show();

            }
            mScheduleItems.get(j).setReminderType(rt_enum);




            mScheduleItems.get(j).setReminderName(cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_NAME)));
            mScheduleItems.get(j).setReminderDescription(cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_DESCRIPTION)));
            mScheduleItems.get(j).setScheduleID(cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)));

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
        RecurringDbHelper recurringDbHelper = new RecurringDbHelper(getContext());
        mRecurringDatabase = recurringDbHelper.getWritableDatabase();



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

                        if(scheduleDialog.getUpdateNeeded()){
                            String id;
                            if(scheduleDialog.isOneTime){
                                mScheduleItems.add(scheduleDialog.getSingleItem());
                                id = scheduleDialog.getSingleItem().getScheduleID();
                            } else {
                                mScheduleItems.add(scheduleDialog.getRecurringItem());
                                id = scheduleDialog.getRecurringItem().getScheduleID();
                            }

                            mScheduleAdapter.notifyItemInserted(mScheduleItems.size() - 1);
                            ApplicationFlags.SetReminderDatasetItemAddedFlag(id);
                            for (int i = 0; i < mScheduleItems.size(); i++){
                                Log.d("Kevin", "class: " + i + mScheduleItems.get(i).getClass().toString());
                            }


                        }


                    }
                });

            }
        });


    }

    private void saveScheduleItemsToDatabase(){
        Log.d("Kevin", "Items saved to DB");
        if(!ApplicationFlags.GetReminderDatasetNeedsUpdate()){
            Log.d(TAG, "saveScheduleItemsToDatabase(), Database does not need updating...");
            ApplicationFlags.ResetReminderDatasetFlags();
            return;
        }
        int flagCounter = 0;
        boolean dbResetNeeded = false;
        if(ApplicationFlags.GetReminderDatasetItemAdded()){
            Log.d(TAG, "saveScheduleItemsToDatabase(), Items need to be added to database...");
            flagCounter++;
        }
        if(ApplicationFlags.GetReminderDatasetItemRemoved()){
            Log.d(TAG, "saveScheduleItemsToDatabase(), Items need to be removed from database...");
            flagCounter++;
        }
        if(ApplicationFlags.GetReminderDatasetItemChanged()){
            Log.d(TAG, "saveScheduleItemsToDatabase(), Items need to be changed in database...");
            flagCounter++;
        }

        if(flagCounter > 1){
            dbResetNeeded = true;
            Log.d(TAG, "saveScheduleItemsToDatabase(), Hard reset of database needed...");
        }

        if(!dbResetNeeded){
            if(ApplicationFlags.GetReminderDatasetItemAdded()){
                for(int i = 0; i < ApplicationFlags.GetRemindersAddedList().size(); i++){
                    for(int j = 0; j < mScheduleItems.size(); j++){
                        if(ApplicationFlags.GetRemindersAddedList().get(i).equals(mScheduleItems.get(j).getScheduleID())){
                            if(mScheduleItems.get(j) instanceof RecurringReminder){
                                mRecurringDatabase.insert(RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                                        null,
                                        ApplicationTools.setRecurringReminderCV((RecurringReminder) mScheduleItems.get(j)));
                            } else if(mScheduleItems.get(j) instanceof SingleReminder){
                                mRecurringDatabase.insert(RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                                        null,
                                        ApplicationTools.setSingleReminderCV((SingleReminder) mScheduleItems.get(j)));
                            }
                        }
                    }
                }
            }
            //
            if(ApplicationFlags.GetReminderDatasetItemRemoved()){
                Cursor cursor = getAllItems();
                cursor.moveToFirst();
                for(int i = 0; i < ApplicationFlags.GetRemindersRemovedList().size(); i++){
                    Log.d("Kevin", "Item removed: " + ApplicationFlags.GetRemindersRemovedList().get(i));
                    for(int j = 0; j < cursor.getCount(); j++){
                        cursor.moveToPosition(j);
                        Log.d("Kevin: " , "AR val: " + ApplicationFlags.GetRemindersRemovedList() + " DB val: " + cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)));
                        if(cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID)).equals(ApplicationFlags.GetRemindersRemovedList().get(i))){
                            long id = cursor.getLong(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry._ID));
                            mRecurringDatabase.delete(RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                                    RecurringReminderColumns.RecurringReminderEntry._ID + "=" + id,
                                    null);
                        }
                    }
                }
            }
            if(ApplicationFlags.GetReminderDatasetItemChanged()){
                Cursor cursor = getAllItems();
                cursor.moveToFirst();
                for(int i = 0; i < mScheduleItems.size(); i++){
                    if(i < cursor.getCount()){
                        cursor.moveToPosition(i);
                        String dbID = cursor.getString(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID));
                        if(!dbID.equals(mScheduleItems.get(i).getScheduleID())){
                            ContentValues cv = new ContentValues();
                            ScheduleItem holderItem = mScheduleItems.get(i);
                            if(mScheduleItems.get(i) instanceof RecurringReminder){
                                cv = ApplicationTools.setRecurringReminderCV((RecurringReminder) mScheduleItems.get(i));
                            } else if(mScheduleItems.get(i) instanceof SingleReminder){
                                cv = ApplicationTools.setSingleReminderCV((SingleReminder) mScheduleItems.get(i));
                            }
                            if(holderItem.getScheduleID() == null){
                                cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID, UUID.randomUUID().toString());
                            } else {
                                cv.put(RecurringReminderColumns.RecurringReminderEntry.COLUMN_SCHEDULE_ID, holderItem.getScheduleID());
                            }
                            mRecurringDatabase.insert(RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME, null, cv);
                        }
                    }
                }

            }
        } else {
            Cursor cursor = getAllItems();
            for(int i = cursor.getCount() - 1; i >= 0; i--){
                cursor.moveToPosition(i);
                long id = cursor.getLong(cursor.getColumnIndex(RecurringReminderColumns.RecurringReminderEntry._ID));
                mRecurringDatabase.delete(RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                        RecurringReminderColumns.RecurringReminderEntry._ID + "=" + id,
                        null);
            }
            for(int j = mScheduleItems.size() - 1; j >= 0; j--){
                if(mScheduleItems.get(j) instanceof RecurringReminder){
                    mRecurringDatabase.insert(RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                            null,
                            ApplicationTools.setRecurringReminderCV((RecurringReminder) mScheduleItems.get(j)));
                }
                if(mScheduleItems.get(j) instanceof SingleReminder){
                    mRecurringDatabase.insert(RecurringReminderColumns.RecurringReminderEntry.TABLE_NAME,
                            null,
                            ApplicationTools.setSingleReminderCV((SingleReminder) mScheduleItems.get(j)));
                }
            }
        }
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

    private void reminderSetUp(){
        //displayNotification(getContext());
        for (int i = 0; i < mScheduleItems.size(); i++){
            Log.d("Kevin", "Class" + mScheduleItems.get(i).getClass().toString());
            if(mScheduleItems.get(i) instanceof SingleReminder){
                Log.d("Kevin", "Single");
                SingleReminder singleReminder = (SingleReminder) mScheduleItems.get(i);
                Calendar c = singleReminder.getReminderCalendar();
                if(!c.before(Calendar.getInstance())){
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(getContext(), ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 1, intent, 0);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                    Calendar now = Calendar.getInstance();
                    long diff = (c.getTimeInMillis() - now.getTimeInMillis())/1000;
                    Log.d("Kevin", "Scheduled Year : " + c.get(Calendar.YEAR));
                    Log.d("Kevin", "Month : " + c.get(Calendar.MONTH));
                    Log.d("Kevin", "Day : " + c.get(Calendar.DAY_OF_MONTH));
                    Log.d("Kevin", "Hour : " + c.get(Calendar.HOUR_OF_DAY));
                    Log.d("Kevin", "Minute : " + c.get(Calendar.MINUTE));
                    Log.d("Kevin", "Current Year : " + now.get(Calendar.YEAR));
                    Log.d("Kevin", "Month : " + now.get(Calendar.MONTH));
                    Log.d("Kevin", "Day : " + now.get(Calendar.DAY_OF_MONTH));
                    Log.d("Kevin", "Hour : " + now.get(Calendar.HOUR_OF_DAY));
                    Log.d("Kevin", "Minute : " + now.get(Calendar.MINUTE));
                    Log.d("Kevin", "In seconds : " + diff);
                } else {
                    Log.d("Kevin", "Alarm b4");
                }




            } else if(mScheduleItems.get(i) instanceof RecurringReminder) {
                Log.d("Kevin", "Recurring");
            }
        }
    }

    private void displayNotification(Context context){
        Log.d(TAG, "displayNotification() Notification displayed...");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1, nb.build());
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

    @Override
    public void onDestroyView() {
        Log.d("Kevin", "View Destroyed");
        timerNeeded = false;
        saveScheduleItemsToDatabase();
        super.onDestroyView();
    }


    @Override
    public void onPause(){
        Log.d("Kevin", "View Paused");
        timerNeeded = false;
        reminderSetUp();
        saveScheduleItemsToDatabase();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d("Kevin", "onResume Called");
        getScheduleItemsFromDb();
        super.onResume();
    }

    @Override
    public void onStart() {
        Log.d("Kevin", "onStart Called");
        super.onStart();
    }


}
