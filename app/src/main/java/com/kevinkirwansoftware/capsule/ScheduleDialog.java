package com.kevinkirwansoftware.capsule;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import java.util.Date;

public class ScheduleDialog extends Dialog {
    View scheduleView;
    Dialog newSchedule;
    Context mContext;
    SchedulePopOutType mSpot;
    int mPosition;
    ScheduleItem mScheduleItem;
    boolean updateNeeded = false;

    TextView reminderPlus, reminderMinus, dailyReminderCounterTV;
    TextInputEditText reminderNameET, reminderDescET;

    Date singleDate;
    boolean isOneTime = true;
    int dailyReminderCounter = 1;

    LinearLayout testLL;
    RadioButton oneTimeRB, recurringRB, dailyRB, customRB;
    RelativeLayout oneTimeSC, recurringSC, dailyRL, customRL;

    Button newReminderAccept, cancelNewReminder;
    SingleDateAndTimePicker sdtp;
    public ScheduleDialog(@NonNull Context context, SchedulePopOutType spotIn, ScheduleItem scheduleItemIn) {
        super(context);
        mContext = context;
        mSpot = spotIn;

        this.setContentView(R.layout.popout_new_reminder);

        cancelNewReminder = this.findViewById(R.id.new_reminder_cancel);
        newReminderAccept = this.findViewById(R.id.new_reminder_create);

        oneTimeRB = this.findViewById(R.id.one_time_rb);
        recurringRB = this.findViewById(R.id.recurring_rb);
        dailyRB = this.findViewById(R.id.daily_rb);
        customRB = this.findViewById(R.id.custom_rb);
        sdtp = this.findViewById(R.id.single_day_picker);


        reminderNameET = this.findViewById(R.id.reminderNameET);
        reminderDescET = this.findViewById(R.id.reminderDescET);

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
            updateWithScheduleInfo();
        }


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
                createNewReminder();
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
                                mContext,
                                R.color.colorSecondary
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
                                mContext,
                                R.color.colorPrimary
                        )
                );
                oneTimeRB.setTextColor(mContext.getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(
                        recurringRB,
                        ContextCompat.getColorStateList(
                                mContext,
                                R.color.colorSecondary
                        )
                );
                recurringRB.setTextColor(mContext.getResources().getColor(R.color.gray));

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
                                mContext,
                                R.color.colorSecondary
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
                                mContext,
                                R.color.colorPrimary
                        )
                );
                customRB.setTextColor(mContext.getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(
                        dailyRB,
                        ContextCompat.getColorStateList(
                                mContext,
                                R.color.colorSecondary
                        )
                );
                dailyRB.setTextColor(mContext.getResources().getColor(R.color.gray));

            }
        });

        reminderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dailyReminderCounter > 1){
                    dailyReminderCounter--;
                    updateReminderCounter();
                } else {
                    Toast.makeText(mContext, "Can't Have 0 Reminders", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mContext, "Maximum 10 Reminders Per Day", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateWithScheduleInfo(){

    }

    private void updateReminderCounter(){
        dailyReminderCounterTV.setText(Integer.toString(dailyReminderCounter));
    }

    public enum SchedulePopOutType{
        NEW,
        EDIT
    }

    private void createNewReminder(){
        boolean validInput = true;
        String errorMessage = "";
        ScheduleItem newScheduleItem;
        newScheduleItem = new ScheduleItem();
        if(isOneTime){
            singleDate = sdtp.getDate();
            newScheduleItem.setReminderType(ScheduleItem.ReminderType.ONE_TIME);
        } else {
            newScheduleItem.setReminderType(ScheduleItem.ReminderType.RECURRING);
            Toast.makeText(getContext(), "Recurring", Toast.LENGTH_SHORT).show();
        }
        if((reminderNameET.getText() != null) || (reminderNameET.getText().toString().length() > 0)){
            String name = reminderNameET.getText().toString();
            newScheduleItem.setReminderName(name);
        } else {
            validInput = false;
            errorMessage.concat("Invalid name\n");
            return;
        }

        if((reminderDescET.getText() != null)){
            String desc = reminderDescET.getText().toString();
            newScheduleItem.setReminderDescription(desc);
        } else {
            validInput = false;
            errorMessage.concat("Invalid description\n");
            return;
        }

        int dailyReminders = 6;
        setScheduleItem(newScheduleItem);
        updateNeeded = true;
        //mScheduleAdapter.notifyItemInserted(mScheduleItems.size());

        this.dismiss();

    }

    public boolean getUpdateNeeded(){
        return updateNeeded;
    }

    private void setScheduleItem(ScheduleItem scheduleItemIn){
        mScheduleItem = scheduleItemIn;
    }

    public ScheduleItem getScheduleItem(){
        return mScheduleItem;
    }
}
