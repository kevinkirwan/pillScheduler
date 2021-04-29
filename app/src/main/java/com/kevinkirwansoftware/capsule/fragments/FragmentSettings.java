package com.kevinkirwansoftware.capsule.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.general.ApplicationFlags;
import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

public class FragmentSettings extends Fragment {

    private View settingsView;
    private RelativeLayout topThemesRL, topUnitsRL, topGraphRL;
    private LinearLayout menuThemesLL, menuUnitsLL, ampmLL, menuGraphLL;
    private ImageView themesDropIcon, unitsDropIcon, graphDropIcon;
    private EditText newsFilterET;
    private RadioButton hour12RB, hour24RB, ampmYesRB, ampmNoRB, fDegRB, cDegRB;
    private boolean timeFormatFlag = false, ampmFlag = false, degFlag = false;
    private boolean is24HourChecked, isAmpmChecked, isFdegChecked;
    private TextInputEditText latencyThresholdET, graphMinsLimitET;
    private int latencyThreshold, graphMinsLimit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);
        return settingsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        settingsInit();
    }

    private void settingsInit(){
        topThemesRL = settingsView.findViewById(R.id.topThemesRL);
        topUnitsRL = settingsView.findViewById(R.id.topUnitsRL);
        topGraphRL = settingsView.findViewById(R.id.topGraphRL);

        menuThemesLL = settingsView.findViewById(R.id.menuThemesLL);
        menuUnitsLL = settingsView.findViewById(R.id.menuUnitsLL);
        ampmLL = settingsView.findViewById(R.id.ampmLL);
        menuGraphLL = settingsView.findViewById(R.id.menuGraphLL);

        hour12RB = settingsView.findViewById(R.id.hour_12_rb);
        hour24RB = settingsView.findViewById(R.id.hour_24_rb);
        ampmYesRB = settingsView.findViewById(R.id.ampm_yes_rb);
        ampmNoRB = settingsView.findViewById(R.id.ampm_no_rb);
        fDegRB = settingsView.findViewById(R.id.temp_f_rb);
        cDegRB = settingsView.findViewById(R.id.temp_c_rb);

        themesDropIcon = settingsView.findViewById(R.id.themesDropIcon);
        unitsDropIcon = settingsView.findViewById(R.id.unitsDropIcon);
        graphDropIcon = settingsView.findViewById(R.id.graphDropIcon);
        newsFilterET = settingsView.findViewById(R.id.newsFilterET);
        latencyThresholdET = settingsView.findViewById(R.id.graphThresholdET);
        graphMinsLimitET = settingsView.findViewById(R.id.graphMaxET);

        fragmentSettingsInit();
    }

    private void fragmentSettingsInit(){
        initAppPreferences();

        topThemesRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuThemesCheck();
            }
        });

        topUnitsRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuUnitsCheck();
            }
        });

        topGraphRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuGraphCheck();
            }
        });

        hour12RB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFormatFlag = true;
                set12hourChecked();
            }
        });

        hour24RB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeFormatFlag = true;
                set24hourChecked();
            }
        });

        ampmYesRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ampmFlag = true;
                setAmpmYesChecked();
            }
        });

        ampmNoRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ampmFlag = true;
                setAmpmNoChecked();
            }
        });

        fDegRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degFlag = true;
                setFdegChecked();
            }
        });

        cDegRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degFlag = true;
                setCdegChecked();
                getContext().setTheme(R.style.ForestTheme);
            }
        });

    }

    private void initAppPreferences(){
        if(ApplicationPreferences.is24Hour()){
            set24hourChecked();
        } else {
            set12hourChecked();
        }

        if(ApplicationPreferences.isAmpm()){
            setAmpmYesChecked();
        } else {
            setAmpmNoChecked();
        }

        if(ApplicationPreferences.isDegF()){
            setFdegChecked();
        } else {
            setCdegChecked();
        }

        String latency = String.valueOf(ApplicationPreferences.getLatencyThreshold());
        String limit = String.valueOf(ApplicationPreferences.getGraphMinsLimit());

        latencyThresholdET.setText(latency);
        graphMinsLimitET.setText(limit);
    }

    private void setFdegChecked(){
        isFdegChecked = true;
        cDegRB.setBackgroundResource(R.drawable.theme_background_none);
        ViewCompat.setBackgroundTintList(
                cDegRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorTwo
                )
        );
        fDegRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                fDegRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorPrimary
                )
        );
    }

    private void setCdegChecked(){
        isFdegChecked = false;
        fDegRB.setBackgroundResource(R.drawable.theme_background_none);
        ViewCompat.setBackgroundTintList(
                fDegRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorTwo
                )
        );
        cDegRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                cDegRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorPrimary
                )
        );
    }

    private void setAmpmYesChecked(){
        isAmpmChecked = true;
        ampmNoRB.setBackgroundResource(R.drawable.theme_background_none);
        ViewCompat.setBackgroundTintList(
                ampmNoRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorTwo
                )
        );
        ampmYesRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                ampmYesRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorPrimary
                )
        );
    }

    private void setAmpmNoChecked(){
        isAmpmChecked = false;
        ampmYesRB.setBackgroundResource(R.drawable.theme_background_none);
        ViewCompat.setBackgroundTintList(
                ampmYesRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorTwo
                )
        );
        ampmNoRB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                ampmNoRB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorPrimary
                )
        );
    }

    private void set12hourChecked(){
        is24HourChecked = false;
        hour24RB.setBackgroundResource(R.drawable.theme_background_none);
        ViewCompat.setBackgroundTintList(
                hour24RB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorTwo
                )
        );
        hour12RB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                hour12RB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorPrimary
                )
        );
        ampmLL.setVisibility(View.VISIBLE);
    }

    private void set24hourChecked(){
        is24HourChecked = true;
        hour24RB.setBackgroundResource(R.drawable.theme_background);
        ViewCompat.setBackgroundTintList(
                hour24RB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorPrimary
                )
        );
        ViewCompat.setBackgroundTintList(
                hour12RB,
                ContextCompat.getColorStateList(
                        getContext(),
                        R.color.colorTwo
                )
        );
        ampmLL.setVisibility(View.GONE);
    }

    private void settingsResume(){

    }

    private void menuGraphCheck(){
        if(menuGraphLL.getVisibility() == View.VISIBLE){
            menuGraphLL.setVisibility(View.GONE);
            graphDropIcon.setRotation(90.0f);
        } else {
            menuGraphLL.setVisibility(View.VISIBLE);
            graphDropIcon.setRotation(-90.0f);
        }
    }

    private void menuThemesCheck(){
        if(menuThemesLL.getVisibility() == View.VISIBLE){
            menuThemesLL.setVisibility(View.GONE);
            themesDropIcon.setRotation(90.0f);
        } else {
            menuThemesLL.setVisibility(View.VISIBLE);
            themesDropIcon.setRotation(-90.0f);
        }
    }

    private void menuUnitsCheck(){
        if(menuUnitsLL.getVisibility() == View.VISIBLE){
            menuUnitsLL.setVisibility(View.GONE);
            unitsDropIcon.setRotation(90.0f);
        } else {
            menuUnitsLL.setVisibility(View.VISIBLE);
            unitsDropIcon.setRotation(-90.0f);
        }
    }

    private void selectTheme(){

    }

    private void updateUnits(){
        if(timeFormatFlag){
            ApplicationPreferences.setIs24Hour(is24HourChecked);
        }
        if(ampmFlag){
            ApplicationPreferences.setIsAmpm(isAmpmChecked);
        }
        if(degFlag){
            ApplicationPreferences.setIsDegF(isFdegChecked);
        }
    }

    private void updateGraphSettings(){
        ApplicationPreferences.setLatencyThreshold(Integer.parseInt(Objects.requireNonNull(latencyThresholdET.getText()).toString()));
        ApplicationPreferences.setGraphMinsLimit(Integer.parseInt(Objects.requireNonNull(graphMinsLimitET.getText()).toString()));
    }

    private void updateAllPreferences(){
        updateUnits();
        updateGraphSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateAllPreferences();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Kevin", "Applications saved");
        ApplicationPreferences.savePreferenceData(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        settingsResume();

    }

    private enum Theme{
        DARK,
        BLUE_WHITE,
        BLACK_WHITE
    }
}
