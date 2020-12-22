package com.kevinkirwansoftware.capsule;

public class MedicineItem {
    private int mMedicineItemImage;
    private String mMedicineName;
    private int mDosageMg;
    private String mMedicineDosageDisplay;

    public MedicineItem(String name, int dosage){
        mMedicineName = name;
        mDosageMg = dosage;
        mMedicineDosageDisplay = Integer.toString(mDosageMg) + " mg";
    }

    public String getMedicineName(){
        return mMedicineName;
    }

    public int getDosageMg(){
        return mDosageMg;
    }

    public String getDosageDisplay(){
        return mMedicineDosageDisplay;
    }


}
