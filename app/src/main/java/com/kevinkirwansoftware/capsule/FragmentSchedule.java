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

public class FragmentSchedule extends Fragment {
    private SQLiteDatabase mReminderDatabase;
    ArrayList<ScheduleItem> mScheduleItems;

    private View scheduleView;
    private Dialog newSchedule;

    private TextView  reminderPlus, reminderMinus, dailyReminderCounterTV;
    private TextInputEditText reminderNameET, reminderDescET;

    private Date singleDate;
    private boolean isOneTime = true;

    private LinearLayout testLL;
    private RadioButton oneTimeRB, recurringRB, dailyRB, customRB;
    private RelativeLayout oneTimeSC, recurringSC, dailyRL, customRL;

    private Button newReminderAccept, cancelNewReminder;
    private SingleDateAndTimePicker sdtp;

    private RecyclerView mScheduleRecyclerView;
    private ScheduleAdapter mScheduleAdapter;
    private RecyclerView.LayoutManager mScheduleLayoutManager;

    ArrayList<ScheduleItem> scheduleList = new ArrayList<>();
    int dailyReminderCounter = 1;

    private ImageView addAlarmButton;

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

        mScheduleRecyclerView = view.findViewById(R.id.scheduleRecyclerView);
        mScheduleRecyclerView.setHasFixedSize(true);
        mScheduleLayoutManager = new LinearLayoutManager(getContext());
        mScheduleAdapter = new ScheduleAdapter(mScheduleItems);

        mScheduleRecyclerView.setLayoutManager(mScheduleLayoutManager);
        mScheduleRecyclerView.setAdapter(mScheduleAdapter);

        mScheduleAdapter.setOnItemClickListener(
                new ScheduleAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        toggleMenu(position);
                    }

                    @Override
                    public void onDeleteClick(int position) {
                        showDeleteOption();
                    }

                    @Override
                    public void onEditClick(int position) {
                        launchEditMenu(position);
                    }
                }
        );
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

    public void showDeleteOption(){

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
                }
            }
        });
    }

    private void getScheduleItemsFromDb(){
        mScheduleItems = new ArrayList<>();
        Cursor cursor = getAllItems();
        for(int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            mScheduleItems.add(new ScheduleItem());
            mScheduleItems.get(i).setReminderName(cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_NAME)));
            mScheduleItems.get(i).setReminderDescription(cursor.getString(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_DESCRIPTION)));
            mScheduleItems.get(i).setDailyReminders(cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_DAILY_REMINDERS)));

            int rt_int = cursor.getInt(cursor.getColumnIndex(ReminderColumns.ReminderEntry.COLUMN_TYPE));
            ScheduleItem.ReminderType rt_enum;

            switch (rt_int){
                case 0:
                    rt_enum = ScheduleItem.ReminderType.ONE_TIME;
                    break;
                case 1:
                    rt_enum = ScheduleItem.ReminderType.RECURRING;
                    break;
                default:
                    rt_enum = ScheduleItem.ReminderType.NONE;

            }
            mScheduleItems.get(i).setReminderType(rt_enum);
            ScheduleItem testItem = mScheduleItems.get(i);

            Log.d("Kevin", "name: " + testItem.getReminderName());
            Log.d("Kevin", "desc: " + testItem.getReminderDescription());
            Log.d("Kevin", "type: " + testItem.getTypeString());

        }
    }


    private void removeItem(int position){
        mScheduleItems.remove(position);
        mScheduleAdapter.notifyItemRemoved(position);
    }

    private void fragmentScheduleInit(){
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        mReminderDatabase = dbHelper.getWritableDatabase();

        addAlarmButton = scheduleView.findViewById(R.id.addAlarmButton);
        addAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchNewReminderPopOut();
                final ScheduleDialog scheduleDialog = new ScheduleDialog(getContext(), ScheduleDialog.SchedulePopOutType.NEW, null);
                scheduleDialog.show();
                scheduleDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if((scheduleDialog.getScheduleItem() != null) && scheduleDialog.getUpdateNeeded()){
                            mScheduleItems.add(scheduleDialog.getScheduleItem());
                            mScheduleAdapter.notifyItemInserted(mScheduleItems.size());
                        }
                    }
                });

            }
        });
    }

    /*
    private void launchNewReminderPopOut(){
        newSchedule = new Dialog(getContext());
        newSchedule.setContentView(R.layout.popout_new_reminder);

        cancelNewReminder = newSchedule.findViewById(R.id.new_reminder_cancel);
        newReminderAccept = newSchedule.findViewById(R.id.new_reminder_create);

        oneTimeRB = newSchedule.findViewById(R.id.one_time_rb);
        recurringRB = newSchedule.findViewById(R.id.recurring_rb);
        dailyRB = newSchedule.findViewById(R.id.daily_rb);
        customRB = newSchedule.findViewById(R.id.custom_rb);
        sdtp = newSchedule.findViewById(R.id.single_day_picker);

        reminderNameET = newSchedule.findViewById(R.id.reminderNameET);
        reminderDescET = newSchedule.findViewById(R.id.reminderDescET);

        oneTimeSC = newSchedule.findViewById(R.id.one_time_reminder_rl);
        recurringSC = newSchedule.findViewById(R.id.recurring_reminder_rl);
        dailyRL = newSchedule.findViewById(R.id.daily_rl);
        customRL = newSchedule.findViewById(R.id.custom_rl);

        reminderPlus = newSchedule.findViewById(R.id.reminder_plus);
        reminderMinus = newSchedule.findViewById(R.id.reminder_minus);
        dailyReminderCounterTV = newSchedule.findViewById(R.id.reminders_per_day_counter);

        testLL = newSchedule.findViewById(R.id.test_ll);

        recurringSC.setVisibility(View.GONE);
        customRL.setVisibility(View.GONE);

        newSchedule.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        newSchedule.show();

        initNewScheduleClicks();
    }

    private void updateReminderCounter(){
        dailyReminderCounterTV.setText(Integer.toString(dailyReminderCounter));
    }

    private void createNewReminder(){
        boolean validInput = true;
        String errorMessage = "";

        mScheduleItems.add(new ScheduleItem());
        int position = mScheduleItems.size() - 1;
        if(isOneTime){
            singleDate = sdtp.getDate();
            mScheduleItems.get(position).setReminderType(ScheduleItem.ReminderType.ONE_TIME);
        } else {
            mScheduleItems.get(position).setReminderType(ScheduleItem.ReminderType.RECURRING);
            Toast.makeText(getContext(), "Recurring", Toast.LENGTH_SHORT).show();
        }
        if((reminderNameET.getText() != null) || (reminderNameET.getText().toString().length() > 0)){
            String name = reminderNameET.getText().toString();
            mScheduleItems.get(position).setReminderName(name);
        } else {
            validInput = false;
            errorMessage.concat("Invalid name\n");
            return;
        }

        if((reminderDescET.getText() != null)){
            String desc = reminderDescET.getText().toString();
            mScheduleItems.get(position).setReminderName(desc);
        } else {
            validInput = false;
            errorMessage.concat("Invalid description\n");
            return;
        }

        int dailyReminders = 6;
        mScheduleAdapter.notifyItemInserted(mScheduleItems.size());

    }

     */

    private void saveDatabase(){
        /*
        ContentValues cv = new ContentValues();
        cv.put(ReminderColumns.ReminderEntry.COLUMN_NAME, name);
        cv.put(ReminderColumns.ReminderEntry.COLUMN_DESCRIPTION, desc);
        cv.put(ReminderColumns.ReminderEntry.COLUMN_TYPE, type);
        cv.put(ReminderColumns.ReminderEntry.COLUMN_DAILY_REMINDERS, dailyReminders);

        mReminderDatabase.insert(ReminderColumns.ReminderEntry.TABLE_NAME, null, cv);
        mScheduleAdapter.swapCursor(getAllItems());
         */
    }

    /*
    private void initNewScheduleClicks(){
        cancelNewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSchedule.dismiss();
            }
        });

        newReminderAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewReminder();
                newSchedule.dismiss();
            }
        });

        recurringRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOneTime = false;
                oneTimeSC.setVisibility(View.GONE);
                recurringSC.setVisibility(View.VISIBLE);
                oneTimeRB.setBackgroundResource(R.drawable.theme_background_none);
                ViewCompat.setBackgroundTintList(
                        oneTimeRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorSecondary
                        )
                );
                oneTimeRB.setTextColor(getResources().getColor(R.color.gray));
                recurringRB.setBackgroundResource(R.drawable.theme_background);
                ViewCompat.setBackgroundTintList(
                        recurringRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorPrimary
                        )
                );
                recurringRB.setTextColor(getResources().getColor(R.color.white));

            }
        });

        oneTimeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOneTime = true;
                oneTimeSC.setVisibility(View.VISIBLE);
                recurringSC.setVisibility(View.GONE);
                oneTimeRB.setBackgroundResource(R.drawable.theme_background);
                ViewCompat.setBackgroundTintList(
                        oneTimeRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorPrimary
                        )
                );
                oneTimeRB.setTextColor(getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(
                        recurringRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorSecondary
                        )
                );
                recurringRB.setTextColor(getResources().getColor(R.color.gray));

            }
        });

        dailyRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOneTime = false;
                customRL.setVisibility(View.GONE);
                dailyRL.setVisibility(View.VISIBLE);
                customRB.setBackgroundResource(R.drawable.theme_background_none);
                ViewCompat.setBackgroundTintList(
                        customRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorSecondary
                        )
                );
                customRB.setTextColor(getResources().getColor(R.color.gray));
                dailyRB.setBackgroundResource(R.drawable.theme_background);
                ViewCompat.setBackgroundTintList(
                        dailyRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorPrimary
                        )
                );
                dailyRB.setTextColor(getResources().getColor(R.color.white));

            }
        });

        customRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOneTime = true;
                customRL.setVisibility(View.VISIBLE);
                dailyRL.setVisibility(View.GONE);
                customRB.setBackgroundResource(R.drawable.theme_background);
                ViewCompat.setBackgroundTintList(
                        customRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorPrimary
                        )
                );
                customRB.setTextColor(getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(
                        dailyRB,
                        ContextCompat.getColorStateList(
                                getContext(),
                                R.color.colorSecondary
                        )
                );
                dailyRB.setTextColor(getResources().getColor(R.color.gray));

            }
        });

        reminderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dailyReminderCounter > 1){
                    dailyReminderCounter--;
                    updateReminderCounter();
                } else {
                    Toast.makeText(getContext(), "Can't Have 0 Reminders", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reminderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dailyReminderCounter < 10){
                    dailyReminderCounter++;
                    updateReminderCounter();
                } else {
                    Toast.makeText(getContext(), "Maximum 10 Reminders Per Day", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
     */

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
        super.onDestroyView();
    }
}
