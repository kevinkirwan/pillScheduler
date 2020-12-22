package com.kevinkirwansoftware.capsule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {
    private ArrayList<MedicineItem> mMedcineItems;

    public static class InventoryViewHolder extends RecyclerView.ViewHolder{
        public TextView mMedicineName;
        public TextView mDosage;
        public ImageView mMedicineIcon;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            mMedicineName = itemView.findViewById(R.id.medicine_name);
            mDosage = itemView.findViewById(R.id.medicine_dosage);
            mMedicineIcon = itemView.findViewById(R.id.medicine_icon);

        }
    }

    public InventoryAdapter(ArrayList<MedicineItem> medicineItems){
        mMedcineItems = medicineItems;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_inventory, parent, false);
        InventoryViewHolder ivh = new InventoryViewHolder(view);
        return ivh;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        MedicineItem item = mMedcineItems.get(position);
        holder.mMedicineName.setText(item.getMedicineName());
        holder.mDosage.setText(item.getDosageDisplay());
    }

    @Override
    public int getItemCount() {
        return mMedcineItems.size();
    }
}
