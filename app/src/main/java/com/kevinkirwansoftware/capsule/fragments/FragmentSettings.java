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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.kevinkirwansoftware.capsule.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    private RelativeLayout topThemesLL;
    private LinearLayout menuThemesLL;
    private ImageView themesDropIcon;
    private EditText newsFilterET;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        settingsView = inflater.inflate(R.layout.fragment_settings, container, false);
        return settingsView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topThemesLL = view.findViewById(R.id.topLinearLayoutThemes);
        menuThemesLL = view.findViewById(R.id.menuThemesLL);
        themesDropIcon = view.findViewById(R.id.themesDropIcon);
        newsFilterET = view.findViewById(R.id.newsFilterET);
        try {
            fragmentSettingsInit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    final TestScheduler scheduler1 = new TestScheduler();


    private void fragmentSettingsInit() throws InterruptedException {
        topThemesLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuThemesCheck();
            }
        });

        /*
        Observable<Character> characterObservable = Observable
                .fromIterable(getResultOfET())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        characterObservable.subscribe(new Observer<Character>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d("Kevin", "onSubscribe called");
            }

            @Override
            public void onNext(Character character) {
                Log.d("Kevin", "onNext, char: " + character);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Kevin", "onError called");
            }

            @Override
            public void onComplete() {
                Log.d("Kevin", "onComplete called");
            }
        });

         */






    }

    private ArrayList<Character> getResultOfET(){
        ArrayList<Character> list = new ArrayList<>();


        list.add('a');
        list.add('b');
        list.add('c');
        list.add('d');
        list.add('e');
        list.add('f');


        /*

        for (int i = 0; i < newsFilterET.getText().length(); i++){
            list.add(newsFilterET.getText().charAt(i));
        }

         */




        return list;
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

    private void selectTheme(){

    }


    private enum Theme{
        DARK,
        BLUE_WHITE,
        BLACK_WHITE
    }
}
