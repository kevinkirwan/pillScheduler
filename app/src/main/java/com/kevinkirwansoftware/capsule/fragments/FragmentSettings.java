package com.kevinkirwansoftware.capsule.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kevinkirwansoftware.capsule.R;

public class FragmentSettings extends Fragment {

    private View settingsView;
    private LinearLayout themeHeaderLL;
    private LinearLayout themeDropdownLL;

    private LinearLayout themeDark;
    private LinearLayout themeBlackWhite;
    private LinearLayout themeBlueWhite;
    private ImageView themeDropdownArrow;
    private Theme selectedTheme;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);
        fragmentSettingsInit();


        return settingsView;
    }

    private void fragmentSettingsInit(){
        themeHeaderLL = settingsView.findViewById(R.id.themeHeaderLL);
        themeDropdownLL = settingsView.findViewById(R.id.themeDropdownLL);
        themeDropdownArrow = settingsView.findViewById(R.id.themeDropdownArrow);

        themeDark = settingsView.findViewById(R.id.darkTheme);
        themeBlackWhite = settingsView.findViewById(R.id.blackWhiteTheme);
        themeBlueWhite = settingsView.findViewById(R.id.blueWhiteTheme);

        initThemes();



        themeHeaderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayThemes();
            }
        });


    }

    private void displayThemes(){
        if(themeDropdownLL.getVisibility() == View.GONE){
            themeDropdownLL.setVisibility(View.VISIBLE);
            themeDropdownArrow.setRotation(90.0f);
        } else{
            themeDropdownLL.setVisibility(View.GONE);
            themeDropdownArrow.setRotation(0.0f);
        }
    }

    private void initThemes(){
        themeDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add alarm", Toast.LENGTH_LONG).show();
            }
        });

        themeBlackWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        themeBlueWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    private enum Theme{
        DARK,
        BLUE_WHITE,
        BLACK_WHITE
    }
}
