package com.kevinkirwansoftware.capsule;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {
    private ArrayList<ScheduleItem> mScheduleItems;
    private OnItemClickListener mListener;
    private Context context;

    public ScheduleAdapter(ArrayList<ScheduleItem> scheduleItems, Context context){
        mScheduleItems = scheduleItems;
        this.context = context;
    }

    public interface OnItemClickListener{
        void onLongClick(int position);
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onActivationCheck(int position, boolean isChecked);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        private TextView reminderNameTV, reminderDescTV, reminderTimeTV, reminderDataLineTwo, typeTV;
        private ImageView mScheduleIcon, mEditIcon, mDeleteIcon;
        private LinearLayout scheduleItemMenuLL, miscInfoLL;
        private CheckBox mActivationCB;
        private boolean menuVisible = false;

        public ScheduleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            reminderNameTV = itemView.findViewById(R.id.reminderName);
            reminderDescTV = itemView.findViewById(R.id.reminderDesc);
            reminderTimeTV = itemView.findViewById(R.id.reminderTime);
            reminderDataLineTwo = itemView.findViewById(R.id.reminderDataLineTwo);
            mScheduleIcon = itemView.findViewById(R.id.iconIV);
            typeTV = itemView.findViewById(R.id.typeTV);

            mEditIcon = itemView.findViewById(R.id.editIcon);
            mDeleteIcon = itemView.findViewById(R.id.deleteIcon);

            scheduleItemMenuLL = itemView.findViewById(R.id.scheduleItemMenuLL);
            miscInfoLL = itemView.findViewById(R.id.miscInfoLL);

            mActivationCB = itemView.findViewById(R.id.activationCheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }

                    }


                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onLongClick(position);
                        }

                    }
                    return false;
                }
            });



            mEditIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            mDeleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                        Log.d("Kevin", "Item Deleted: " + position);
                    }
                }
            });

            mActivationCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onActivationCheck(position, mActivationCB.isChecked());
                        }
                    }
                }
            });

        }
    }






    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        if(mScheduleItems.get(position) instanceof SingleReminder){
            SingleReminder currentItem = (SingleReminder) mScheduleItems.get(position);
            holder.mScheduleIcon.setImageResource(R.drawable.ic_one_time);
            holder.reminderNameTV.setText(currentItem.getReminderName());
            holder.reminderDescTV.setText(currentItem.getReminderDescription());
            holder.reminderTimeTV.setText(currentItem.getTimeSingleAsString());
            holder.reminderDataLineTwo.setText(currentItem.getDateSingleAsString());
            holder.typeTV.setText(currentItem.getTypeString());
            holder.mActivationCB.setText(currentItem.getActiveString());
            holder.reminderDataLineTwo.setVisibility(View.VISIBLE);
            if(currentItem.isMenuVisible()){
                holder.scheduleItemMenuLL.setVisibility(View.VISIBLE);
                holder.miscInfoLL.setVisibility(View.GONE);
            } else {
                holder.scheduleItemMenuLL.setVisibility(View.GONE);
                holder.miscInfoLL.setVisibility(View.VISIBLE);
            }
            if(currentItem.isActive()){
                holder.mActivationCB.setChecked(true);
                holder.reminderNameTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.reminderDescTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.reminderTimeTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.reminderDataLineTwo.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.typeTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.mScheduleIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.mActivationCB.setChecked(false);
                holder.reminderNameTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.reminderDescTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.reminderTimeTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.reminderDataLineTwo.setTextColor(context.getResources().getColor(R.color.gray));
                holder.typeTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.mScheduleIcon.setColorFilter(context.getResources().getColor(R.color.gray));
            }
        } else {
            RecurringReminder currentItem = (RecurringReminder) mScheduleItems.get(position);
            holder.mScheduleIcon.setImageResource(R.drawable.ic_recurring);
            holder.reminderNameTV.setText(currentItem.getReminderName());
            holder.reminderDescTV.setText(currentItem.getReminderDescription());
            holder.typeTV.setText(currentItem.getTypeString());
            holder.mActivationCB.setText(currentItem.getActiveString());
            holder.reminderDataLineTwo.setVisibility(View.GONE);
            if(currentItem.isMenuVisible()){
                holder.scheduleItemMenuLL.setVisibility(View.VISIBLE);
                holder.miscInfoLL.setVisibility(View.GONE);
            } else {
                holder.scheduleItemMenuLL.setVisibility(View.GONE);
                holder.miscInfoLL.setVisibility(View.VISIBLE);
            }
            if(currentItem.getNumDailyReminders() == 1){
                holder.reminderTimeTV.setText(currentItem.getFistTimeAsString());
                holder.reminderTimeTV.setTextSize(24);
            } else {
                holder.reminderTimeTV.setText(currentItem.getNumDailyRemindersString());
                holder.reminderTimeTV.setTextSize(16);
            }
            if(currentItem.isActive()){
                holder.mActivationCB.setChecked(true);
                holder.reminderNameTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.reminderDescTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.reminderTimeTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.reminderDataLineTwo.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.typeTV.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.mScheduleIcon.setColorFilter(context.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.mActivationCB.setChecked(false);
                holder.reminderNameTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.reminderDescTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.reminderTimeTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.reminderDataLineTwo.setTextColor(context.getResources().getColor(R.color.gray));
                holder.typeTV.setTextColor(context.getResources().getColor(R.color.gray));
                holder.mScheduleIcon.setColorFilter(context.getResources().getColor(R.color.gray));
            }
        }

        /*
        holder.reminderNameTV.setText(currentItem.getReminderName());
        holder.reminderDescTV.setText(currentItem.getReminderDescription());

         */





    }

    @Override
    public int getItemCount() {
        return mScheduleItems.size();
    }

}
