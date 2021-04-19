package com.kevinkirwansoftware.capsule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class ScheduleDialog extends Dialog {
    View scheduleView;
    Dialog newSchedule;
    private Context mContext;
    private SchedulePopOutType mSpot;
    int mPosition;
    private RecurringReminder mRecurringItem;
    private SingleReminder mSingleItem;
    private ScheduleItem mItemIn;
    private boolean updateNeeded = false;

    TextView reminderPlus, reminderMinus, dailyReminderCounterTV;
    private TextInputLayout reminderNameTIL;
    private TextInputEditText reminderNameET, reminderDescET;

    Date singleDate;
    public boolean isOneTime = true;
    private int dailyReminderCounter = 1;

    private LinearLayout testLL;
    private RadioButton oneTimeRB, recurringRB, dailyRB, customRB;
    private RelativeLayout oneTimeSC, recurringSC, dailyRL, customRL;
    private RelativeLayout dailyRL1, dailyRL2, dailyRL3, dailyRL4;

    private Button newReminderAccept, cancelNewReminder;
    private SingleDateAndTimePicker sdtp;
    private SingleDateAndTimePicker dailySdtp1, dailySdtp2, dailySdtp3, dailySdtp4;

    public ScheduleDialog(@NonNull Context context, SchedulePopOutType spotIn, ScheduleItem scheduleItemIn) {
        super(context);
        mContext = context;
        mSpot = spotIn;
        mItemIn = scheduleItemIn;

        this.setContentView(R.layout.popout_new_reminder);

        cancelNewReminder = this.findViewById(R.id.new_reminder_cancel);
        newReminderAccept = this.findViewById(R.id.new_reminder_create);

        oneTimeRB = this.findViewById(R.id.one_time_rb);
        recurringRB = this.findViewById(R.id.recurring_rb);
        dailyRB = this.findViewById(R.id.daily_rb);
        customRB = this.findViewById(R.id.custom_rb);
        sdtp = this.findViewById(R.id.single_day_picker);
        sdtp.setIsAmPm(!ApplicationPreferences.is24Hour());

        dailySdtp1 = this.findViewById(R.id.first_daily_reminder);
        dailySdtp2 = this.findViewById(R.id.second_daily_reminder);
        dailySdtp3 = this.findViewById(R.id.third_daily_reminder);
        dailySdtp4 = this.findViewById(R.id.fourth_daily_reminder);

        dailyRL1 = this.findViewById(R.id.first_daily_reminder_rl);
        dailyRL2 = this.findViewById(R.id.second_daily_reminder_rl);
        dailyRL3 = this.findViewById(R.id.third_daily_reminder_rl);
        dailyRL4 = this.findViewById(R.id.fourth_daily_reminder_rl);

        reminderNameET = this.findViewById(R.id.reminderNameET);
        reminderDescET = this.findViewById(R.id.reminderDescET);

        reminderNameTIL = this.findViewById(R.id.reminderNameTIL);

        oneTimeSC = this.findViewById(R.id.one_time_reminder_rl);
        recurringSC = this.findViewById(R.id.recurring_reminder_rl);
        dailyRL = this.findViewById(R.id.daily_rl);
        customRL = this.findViewById(R.id.custom_rl);



        reminderPlus = this.findViewById(R.id.reminder_plus);
        reminderMinus = this.findViewById(R.id.reminder_minus);
        dailyReminderCounterTV = this.findViewById(R.id.reminders_per_day_counter);

        testLL = this.findViewById(R.id.test_ll);

        recurringSC.setVisibility(View.GONE);
        customRL.setVisibility(View.GONE);
        
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if(spotIn == SchedulePopOutType.EDIT){
            updateWithScheduleInfo(scheduleItemIn);
            newReminderAccept.setText(R.string.update);
        }

        updateReminderCounter();
        //this.dismiss();

        cancelNewReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        newReminderAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOneTime){
                    createSingleReminder();
                } else {
                    createRecurringReminder();
                }
            }
        });

        recurringRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurringRBUpdate();

            }
        });

        oneTimeRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneTimeRBUpdate();

            }
        });

        dailyRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDailyRBUpdate();
            }
        });

        customRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCustomRBUpdate();
            }
        });

        reminderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderMinusUpdate();
            }
        });

        reminderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderPlusUpdate();
            }
        });


    }

    private void reminderPlusUpdate(){
        if(dailyReminderCounter < 4){
            dailyReminderCounter++;
            updateReminderCounter();
        } else {
            Toast.makeText(mContext, "Maximum 4 Reminders Per Day", Toast.LENGTH_SHORT).show();
        }
    }

    private void reminderMinusUpdate(){
        if(dailyReminderCounter > 1){
            dailyReminderCounter--;
            updateReminderCounter();
        } else {
            Toast.makeText(mContext, "Can't Have 0 Reminders", Toast.LENGTH_SHORT).show();
        }
    }

    private void setCustomRBUpdate(){
        isOneTime = true;
        customRL.setVisibility(View.VISIBLE);
        dailyRL.setVisibility(View.GONE);
        customRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                customRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorPrimary
                )
        );
        customRB.setTextColor(mContext.getResources().getColor(R.color.white));
        ViewCompat.setBackgroundTintList(
                dailyRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorThree
                )
        );
        dailyRB.setTextColor(mContext.getResources().getColor(R.color.gray));
    }

    private void setDailyRBUpdate(){
        isOneTime = false;
        customRL.setVisibility(View.GONE);
        dailyRL.setVisibility(View.VISIBLE);
        customRB.setBackgroundResource(R.drawable.theme_background_none);
        ViewCompat.setBackgroundTintList(
                customRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorThree
                )
        );
        customRB.setTextColor(mContext.getResources().getColor(R.color.gray));
        dailyRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                dailyRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorPrimary
                )
        );
        dailyRB.setTextColor(mContext.getResources().getColor(R.color.white));
    }

    private void oneTimeRBUpdate(){
        isOneTime = true;
        oneTimeSC.setVisibility(View.VISIBLE);
        recurringSC.setVisibility(View.GONE);
        oneTimeRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                oneTimeRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorPrimary
                )
        );
        oneTimeRB.setTextColor(mContext.getResources().getColor(R.color.white));
        ViewCompat.setBackgroundTintList(
                recurringRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorThree
                )
        );
        recurringRB.setTextColor(mContext.getResources().getColor(R.color.gray));
    }

    private void recurringRBUpdate(){
        isOneTime = false;
        oneTimeSC.setVisibility(View.GONE);
        recurringSC.setVisibility(View.VISIBLE);
        oneTimeRB.setBackgroundResource(R.drawable.theme_background_none);
        ViewCompat.setBackgroundTintList(
                oneTimeRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorThree
                )
        );
        oneTimeRB.setTextColor(mContext.getResources().getColor(R.color.gray));
        recurringRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                recurringRB,
                ContextCompat.getColorStateList(
                        mContext,
                        R.color.colorPrimary
                )
        );
        recurringRB.setTextColor(mContext.getResources().getColor(R.color.white));
    }

    private void updateWithScheduleInfo(ScheduleItem scheduleItem){
        reminderNameET.setText(scheduleItem.getReminderName());
        reminderDescET.setText(scheduleItem.getReminderDescription());
        if(scheduleItem instanceof RecurringReminder){
            RecurringReminder recurringReminder = (RecurringReminder) scheduleItem;
            recurringRBUpdate();
            dailyReminderCounter = ((RecurringReminder) scheduleItem).getNumDailyReminders();
            updateReminderCounter();
            dailySdtp1.setDefaultDate(recurringReminder.getCalendar1().getTime());
            dailySdtp2.setDefaultDate(recurringReminder.getCalendar2().getTime());
            dailySdtp3.setDefaultDate(recurringReminder.getCalendar3().getTime());
            dailySdtp4.setDefaultDate(recurringReminder.getCalendar4().getTime());
        } else if(scheduleItem instanceof SingleReminder){
            SingleReminder singleReminder = (SingleReminder) scheduleItem;
            oneTimeRBUpdate();
            sdtp.setDefaultDate(singleReminder.getReminderCalendar().getTime());
        }


    }

    private void updateReminderCounter(){
        dailyReminderCounterTV.setText(Integer.toString(dailyReminderCounter));

        if(dailyReminderCounter > 1){
            dailyRL2.setVisibility(View.VISIBLE);
        } else {
            dailyRL2.setVisibility(View.GONE);
        }
        if(dailyReminderCounter > 2){
            dailyRL3.setVisibility(View.VISIBLE);
        } else {
            dailyRL3.setVisibility(View.GONE);
        }
        if(dailyReminderCounter > 3){
            dailyRL4.setVisibility(View.VISIBLE);
        } else {
            dailyRL4.setVisibility(View.GONE);
        }

    }

    public enum SchedulePopOutType{
        NEW,
        EDIT
    }

    private void createRecurringReminder(){
        if(!commonCheckPass()){
            return;
        }
        if((mSpot == SchedulePopOutType.NEW) || (mItemIn instanceof SingleReminder)){
            mRecurringItem = new RecurringReminder();
            mRecurringItem.recurringReminderInit();
            mRecurringItem.setActive(true);
        } else {
            Log.d("Kevin", "Schedule ID:" + mItemIn.getScheduleID());
            mRecurringItem = (RecurringReminder) mItemIn;
        }

        mRecurringItem.setReminderName(reminderNameET.getText().toString());
        mRecurringItem.setReminderDescription(reminderDescET.getText().toString());
        mRecurringItem.setReminderType(ScheduleItem.ReminderType.RECURRING);
        int dailyReminders = Integer.parseInt(dailyReminderCounterTV.getText().toString());
        mRecurringItem.setDailyReminders(dailyReminders);

        int[][] timeArray = new int[2][4];
        for (int[] row: timeArray){
            Arrays.fill(row, -1);
        }


        Date tempDate = dailySdtp1.getDate();
        Timestamp ts = new java.sql.Timestamp(tempDate.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("HH-mm");
        String[] timeArrayString = formatter.format(ts).split("-");
        timeArray[0][0] = Integer.parseInt(timeArrayString[0]);
        timeArray[1][0] = Integer.parseInt(timeArrayString[1]);

        if(dailyReminders > 1){
            tempDate = dailySdtp2.getDate();
            ts.setTime(tempDate.getTime());
            timeArrayString = formatter.format(ts).split("-");
            timeArray[0][1] = Integer.parseInt(timeArrayString[0]);
            timeArray[1][1] = Integer.parseInt(timeArrayString[1]);
        }
        if(dailyReminders > 2){
            tempDate = dailySdtp3.getDate();
            ts.setTime(tempDate.getTime());
            timeArrayString = formatter.format(ts).split("-");
            timeArray[0][2] = Integer.parseInt(timeArrayString[0]);
            timeArray[1][2] = Integer.parseInt(timeArrayString[1]);
        }
        if(dailyReminders > 3){
            tempDate = dailySdtp4.getDate();
            ts.setTime(tempDate.getTime());
            timeArrayString = formatter.format(ts).split("-");
            timeArray[0][3] = Integer.parseInt(timeArrayString[0]);
            timeArray[1][3] = Integer.parseInt(timeArrayString[1]);
        }
        mRecurringItem.setMultiRemindersArray(timeArray, false);

        updateNeeded = true;
        this.dismiss();

    }

    private void createSingleReminder(){
        if(!commonCheckPass()){
            return;
        }
        singleDate = sdtp.getDate();
        Log.d("Kevin", "is AM PM " + sdtp);

        Timestamp ts = new java.sql.Timestamp(singleDate.getTime());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String[] timeArray = formatter.format(ts).split("-");
        if((mSpot == SchedulePopOutType.NEW) || (mItemIn instanceof RecurringReminder)){
            mSingleItem = new SingleReminder(sdtp.getDate());
            mSingleItem.singleReminderInit();
            mSingleItem.setActive(true);
        } else {
            mSingleItem = (SingleReminder) mItemIn;
            mSingleItem.setDate(sdtp.getDate());
        }

        mSingleItem.setReminderName(reminderNameET.getText().toString());
        mSingleItem.setReminderDescription(reminderDescET.getText().toString());
        mSingleItem.setReminderType(ScheduleItem.ReminderType.ONE_TIME);
        updateNeeded = true;
        this.dismiss();
    }

    private boolean commonCheckPass(){
        boolean validInputName = true;
        boolean validInputDesc = true;
        String errorMessageName = "";
        String errorMessageDesc = "";
        String[] charList = ApplicationTools.getListOfInvalidSQLiteChars();
        if((reminderNameET.getText().toString().length() == 0)){
            validInputName = false;
            errorMessageName = errorMessageName.concat("Required Field\n");
        }
        if((reminderNameET.getText().toString().length() > ApplicationTools.MAX_REMINDER_NAME_STRING_LENGTH)){
            validInputName = false;
            errorMessageName = errorMessageName.concat("Maxiumum name length: "  +  ApplicationTools.MAX_REMINDER_NAME_STRING_LENGTH + " characters\n");
        }

        if(!ApplicationTools.isSQLiteStringValid(reminderNameET.getText().toString())){
            validInputName = false;
            errorMessageName = errorMessageName.concat("Invalid characters: ");
            for (String s : charList) {
                errorMessageName = errorMessageName.concat(s + " ");
            }
            errorMessageName = errorMessageName.concat("\n");
        }

        if((reminderDescET.getText().toString().length() > ApplicationTools.MAX_REMINDER_DESC_STRING_LENGTH)){
            validInputDesc = false;
            errorMessageDesc = errorMessageDesc.concat("Maxiumum description length: "  +  ApplicationTools.MAX_REMINDER_DESC_STRING_LENGTH + " characters\n");
        }

        if(!ApplicationTools.isSQLiteStringValid(reminderDescET.getText().toString())){
            validInputDesc = false;
            errorMessageDesc = errorMessageDesc.concat("Invalid characters: ");
            for (String s : charList) {
                errorMessageDesc = errorMessageDesc.concat(s);
            }
            errorMessageDesc = errorMessageDesc.concat("\n");
        }

        if(!validInputName){
            reminderNameET.setError(errorMessageName);
        }
        if(!validInputDesc){
            reminderDescET.setError(errorMessageDesc);
        }

        return (validInputName && validInputDesc);
    }

    public boolean getUpdateNeeded(){
        return updateNeeded;
    }

    public RecurringReminder getRecurringItem(){
        return mRecurringItem;
    }

    public SingleReminder getSingleItem(){
        return mSingleItem;
    }
}
