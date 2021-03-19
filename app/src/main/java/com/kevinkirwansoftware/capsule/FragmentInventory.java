package com.kevinkirwansoftware.capsule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentInventory extends Fragment {
    private RecyclerView mInventoryRecyclerView;
    private RecyclerView.Adapter mInventoryAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ImageView addMedicineItem;
    private static final String TAG = "FragmentInventory";

    private RecurringDbHelper mRecurringDbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<MedicineItem> medicineItems = new ArrayList<>();
        medicineItems.add(new MedicineItem("Tylenol", 25));
        medicineItems.add(new MedicineItem("Hydroxyzine", 120));
        medicineItems.add(new MedicineItem("Vitamin D", 50));
        medicineItems.add(new MedicineItem("Advil", 120));
        medicineItems.add(new MedicineItem("Melatonin", 120));
        addMedicineItem = view.findViewById(R.id.addMedicineItem);

        addMedicineItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        mInventoryRecyclerView = view.findViewById(R.id.inventoryRecyclerView);
        mInventoryRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mInventoryAdapter = new InventoryAdapter(medicineItems);

        mInventoryRecyclerView.setLayoutManager(mLayoutManager);
        mInventoryRecyclerView.setAdapter(mInventoryAdapter);
    }

    /*
    public void addData(String newEntry){
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if(insertData){
            Toast.makeText(getContext(), "Data Successfully Inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
        }

    }

     */

}
