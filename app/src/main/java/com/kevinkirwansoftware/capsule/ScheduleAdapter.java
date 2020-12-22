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

    public ScheduleAdapter(ArrayList<ScheduleItem> scheduleItems){
        mScheduleItems = scheduleItems;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder{
        public TextView reminderNameTV, reminderDescTV;
        public ImageView mScheduleIcon, mEditIcon, mDeleteIcon;
        LinearLayout scheduleItemMenuLL;
        boolean menuVisible = false;

        public ScheduleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            reminderNameTV = itemView.findViewById(R.id.reminderName);
            reminderDescTV = itemView.findViewById(R.id.reminderDesc);
            mScheduleIcon = itemView.findViewById(R.id.imageView);

            mEditIcon = itemView.findViewById(R.id.editIcon);
            mDeleteIcon = itemView.findViewById(R.id.deleteIcon);

            scheduleItemMenuLL = itemView.findViewById(R.id.scheduleItemMenuLL);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                    if(listener != null){
                        Log.d("Kevin", "Item Pressed");
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }

                    }

                     */
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener != null){
                        Log.d("Kevin", "Item Pressed");
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
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
                            listener.onDeleteClick(position);
                        }
                        Log.d("Kevin", "Item Deleted: " + position);
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
        ScheduleItem currentItem = mScheduleItems.get(position);

        ScheduleItem.ReminderType rt = currentItem.getReminderType();
        if(rt == ScheduleItem.ReminderType.ONE_TIME){
            holder.mScheduleIcon.setImageResource(R.drawable.clipboard);
        } else if(rt == ScheduleItem.ReminderType.RECURRING){
            holder.mScheduleIcon.setImageResource(R.drawable.calender);
        }

        holder.reminderNameTV.setText(currentItem.getReminderName());
        holder.reminderDescTV.setText(currentItem.getReminderDescription());

        if(currentItem.isMenuVisible()){
            holder.scheduleItemMenuLL.setVisibility(View.VISIBLE);
        } else {
            holder.scheduleItemMenuLL.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mScheduleItems.size();
    }

}
