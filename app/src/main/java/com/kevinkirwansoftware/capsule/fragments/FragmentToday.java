package com.kevinkirwansoftware.capsule.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.RetrofitApiInterface;
import com.kevinkirwansoftware.capsule.RetrofitClient;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;
import com.kevinkirwansoftware.capsule.throwaway.Headlines;
import com.kevinkirwansoftware.capsule.throwaway.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FragmentToday extends Fragment {
    private View todayView;
    private static String TAG = "FragmentToday.java";
    private TextClock currentTimeDisplay;
    private TextView ampm, alarmAmpm, alarm, timeZone, date, newsHeadline, newsContent;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RetrofitApiInterface retrofitAPI;
    private static int STORAGE_PERMISSION_CODE = 1;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        todayView = inflater.inflate(R.layout.fragment_today, container, false);
        return todayView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentTimeDisplay = view.findViewById(R.id.currentTimeDisplay);
        timeZone = view.findViewById(R.id.timeZone);
        date = view.findViewById(R.id.date);

        newsHeadline = view.findViewById(R.id.newsHeadline);
        newsContent = view.findViewById(R.id.newsContent);

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitAPI = retrofit.create(RetrofitApiInterface.class);
        fragmentTodayInit();
    }

    private void fragmentTodayInit() {
        date.setText(ApplicationTools.getDateData());
        timeZone.setText(ApplicationTools.getTimeZoneData());

        ApplicationTools.getDateForApiCall();
        fetchData();


    }

    private void getPermissions(){

    }

    private void populateNewsStories(Headlines headlines) {
        newsHeadline.setText(headlines.getTitle(0));
        newsContent.setText(headlines.getContent(0));
        Log.d("Kevin", "Author: " + headlines.getTotalResults() + " \n" +
                "Text: " + headlines.getTotalResults());

        //ArrayList<String> sources = new ArrayList<>();
        for (int i = 0; i < headlines.getArticles().size(); i++){
            Log.d("Kevin", "Source: " + headlines.getArticles().get(i).getSource().name);
        }


    }


    private void displayData(JSONObject response) throws JSONException {
        JSONObject mainObject = response.getJSONObject("main");
        JSONArray array = response.getJSONArray("weather");
        JSONObject object = array.getJSONObject(0);
        String temp = String.valueOf(mainObject.getDouble("temp"));
        String description = object.getString("description");
        String city = response.getString("name");

    }

    private void fetchData() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Location Permission has already been granted");
        } else {
            requestStoragePermission();
        }
        LocationManager lm = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        if(lm != null){
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

/*
            compositeDisposable.add(retrofitAPI.getBookListFromApi("jungle")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<BookListModel>() {
                        @Override
                        public void accept(BookListModel bookListModel) throws Exception {
                            displayData(bookListModel);
                        }
                    }));

 */





            compositeDisposable.add(retrofitAPI.getRawData(ApplicationTools.getNewsApiKey(),
                        "to",
                        null,
                        "en",
                        ApplicationTools.getDateForApiCall())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Headlines>() {
                                   @Override
                                   public void accept(Headlines headlines) throws Exception {
                                       populateNewsStories(headlines);
                                   }

                               }));












/*



            compositeDisposable.add(retrofitAPI.getPosts("saepe")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Post>>() {
                        @Override
                        public void accept(List<Post> posts) throws Exception {
                            Log.d("Kevin", "Text: " + posts.get(3).getText());
                        }
                    }));

 */




/*

            compositeDisposable.add(retrofitAPI.getCurrentWeatherData("40.0", "74.0", ApplicationTools.getApplicationId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<JSONObject>() {
                        @Override
                        public void accept(JSONObject response) throws Exception {
                            displayData(response);
                        }

                    }));


 */


        } else {
            Log.d("Kevin", "Can\'t retrieve weather data");
        }

    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
