package com.kevinkirwansoftware.capsule.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.kevinkirwansoftware.capsule.R;
import com.kevinkirwansoftware.capsule.RetrofitApiInterface;
import com.kevinkirwansoftware.capsule.RetrofitDummyClient;
import com.kevinkirwansoftware.capsule.RetrofitNewsClient;
import com.kevinkirwansoftware.capsule.RetrofitWeatherClient;
import com.kevinkirwansoftware.capsule.WeatherData;
import com.kevinkirwansoftware.capsule.general.ApplicationPreferences;
import com.kevinkirwansoftware.capsule.general.ApplicationTools;
import com.kevinkirwansoftware.capsule.throwaway.Headlines;
import com.kevinkirwansoftware.capsule.throwaway.Post;
import com.kevinkirwansoftware.capsule.throwaway.weather.WeatherResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FragmentToday extends Fragment {
    private View todayView;
    private String longitude, latitude;
    private static String TAG = "FragmentToday.java";
    private TextClock currentTimeDisplay;
    private TextView ampm, alarmAmpm, alarm, timeZone, date, newsHeadline, newsContent, currentWeatherSummary, currentWeatherTemp;
    private ImageView weatherIcon, newsIcon;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RetrofitApiInterface retrofitNewsApi, retrofitWeatherApi, retrofitDummyApi;
    private static int STORAGE_PERMISSION_CODE = 1;
    private static int FULL_SCREEN_PERMISSION_CODE = 6;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        todayView = inflater.inflate(R.layout.fragment_today, container, false);
        return todayView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentTodayInit();
    }

    private void fragmentTodayInit() {
        currentTimeDisplay = todayView.findViewById(R.id.currentTimeDisplay);
        timeZone = todayView.findViewById(R.id.timeZone);
        date = todayView.findViewById(R.id.date);

        newsHeadline = todayView.findViewById(R.id.newsHeadline);
        newsContent = todayView.findViewById(R.id.newsContent);
        currentWeatherSummary = todayView.findViewById(R.id.currentWeatherSummary);
        currentWeatherTemp = todayView.findViewById(R.id.temperature);
        weatherIcon = todayView.findViewById(R.id.weatherIcon);
        newsIcon = todayView.findViewById(R.id.newsIcon);

        Retrofit retrofitNewsInstance = RetrofitNewsClient.getInstance();
        Retrofit retrofitWeatherInstance = RetrofitWeatherClient.getInstance();
        Retrofit retrofitDummyInstance = RetrofitDummyClient.getInstance();
        retrofitNewsApi = retrofitNewsInstance.create(RetrofitApiInterface.class);
        retrofitWeatherApi = retrofitWeatherInstance.create(RetrofitApiInterface.class);
        retrofitDummyApi = retrofitDummyInstance.create(RetrofitApiInterface.class);

        checkPermission();

        date.setText(ApplicationTools.getDateData());
        timeZone.setText(ApplicationTools.getTimeZoneData());


        fetchData();
    }

    private void updateData(){
        date.setText(ApplicationTools.getDateData());
        timeZone.setText(ApplicationTools.getTimeZoneData());

        if(ApplicationPreferences.is24Hour()){
            currentTimeDisplay.setFormat12Hour("kk:mm");
            currentTimeDisplay.setFormat24Hour("kk:mm");
        } else {
            String format = "h:mm";
            if(ApplicationPreferences.isAmpm()){
                format = format.concat(" a");
            }
            currentTimeDisplay.setFormat12Hour(format);
            currentTimeDisplay.setFormat24Hour(format);
        }

        fetchData();
    }

    private void displayWeatherPass(WeatherResponse response){
        String tempString = String.format(Locale.getDefault(),"%d", (long) response.getCurrent().getTemp()) +
                "\u00B0" + ApplicationPreferences.getTempUnitString() + ", Humidity: " + response.getCurrent().getHumidity() + "%";
        String summaryString = response.getCurrent().getWeather().get(0).getDescription();
        currentWeatherSummary.setText(ApplicationTools.weatherDescriptionFormatted(summaryString));
        currentWeatherTemp.setText(tempString);
        weatherIcon.setImageResource(ApplicationTools.getWeatherDrawable(summaryString));
        weatherIcon.setColorFilter(Objects.requireNonNull(getContext()).getResources().getColor(R.color.colorPrimary));
    }

    private void displayWeatherFail(){
        String tempString = "-";
        String summaryString = "Weather data not available";
        currentWeatherSummary.setText(ApplicationTools.weatherDescriptionFormatted(summaryString));
        currentWeatherTemp.setText(tempString);
    }


    private void dummyProcessingPass(List<Post> postList){
        newsHeadline.setText(postList.get(0).getTitle());
        String tempText = postList.get(0).getText().replaceAll("\n", " ");
        newsContent.setText(tempText);
        try {
            Glide.with(Objects.requireNonNull(getContext())).load("https://upload.wikimedia.org/wikipedia/commons/4/47/PNG_transparency_demonstration_1.png").into(newsIcon);
        } catch (Exception e){
            Log.e("Kevin", "Image not found", e.fillInStackTrace());
            newsIcon.setImageResource(R.drawable.foliage);
            newsIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);;
        }
        Log.d("Kevin", "Title: " + postList.get(0).getTitle());
        Log.d("Kevin", "Text: " + tempText);
    }

    private void dummyProcessingFail(){
        String headlineString = "Unable to load news";
        String contentString = "-";
        newsHeadline.setText(headlineString);
        newsContent.setText(contentString);
    }

    private void populateNewsStoriesPass(Headlines headlines) {
        newsHeadline.setText(headlines.getTitle(0));
        newsContent.setText(headlines.getContent(0));
        Log.d("Kevin", "Img url: " + headlines.getImageUrl(0));
        try {
            Glide.with(Objects.requireNonNull(getContext())).load(headlines.getImageUrl(0)).into(newsIcon);
        } catch (Exception e){
            Log.e("Kevin", "Image not found", e.fillInStackTrace());
            newsIcon.setImageResource(R.drawable.foliage);
            newsIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);;
        }


    }

    private void populateNewsStoriesFail(){
        String headlineString = "Unable to load news";
        String contentString = "-";
        newsHeadline.setText(headlineString);
        newsContent.setText(contentString);
    }


    private void displayData(JSONObject response) throws JSONException {
        JSONObject mainObject = response.getJSONObject("main");
        JSONArray array = response.getJSONArray("weather");
        JSONObject object = array.getJSONObject(0);
        String temp = String.valueOf(mainObject.getDouble("temp"));
        String description = object.getString("description");
        String city = response.getString("name");

    }

    private void checkPermission(){
        if ((ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        && (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG, "Location Permission has already been granted");
            LocationManager lm = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
            if(lm != null) {
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    longitude = Double.toString((int) Math.rint(location.getLongitude()));
                    latitude = Double.toString((int) Math.rint(location.getLatitude()));
                    Log.d("Kevin", "Latitude: " + latitude + " Long" + longitude);
                }
            }
        } else {
            requestStoragePermission();
        }
        if ((ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.USE_FULL_SCREEN_INTENT) == PackageManager.PERMISSION_GRANTED)) {
            Log.d(TAG, "Full screen Permission has already been granted");
        } else {
            requestFullScreenPermission();
        }


    }

    private void fetchData() {
            // Working weather call
            compositeDisposable.add(retrofitWeatherApi.getCurrentWeatherData(latitude, longitude,
                    ApplicationPreferences.getTempUnitSystemString(),
                    "hourly,daily,minutely",
                    ApplicationTools.getWeatherApiKey())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<WeatherResponse>() {
                        @Override
                        public void accept(WeatherResponse weatherResponse) throws Exception {
                            displayWeatherPass(weatherResponse);
                            Log.d("Kevin", "json test: timezone: " + weatherResponse.timezone);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            displayWeatherFail();
                        }
                    }));


        compositeDisposable.add(retrofitDummyApi.getPosts("sa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Post>>() {
                    @Override
                    public void accept(List<Post> postList) throws Exception {
                        dummyProcessingPass(postList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        dummyProcessingFail();
                    }
                }));




        compositeDisposable.add(retrofitNewsApi.getTopByLanguage(ApplicationTools.getNewsApiKey(),
                "en", "us")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Headlines>() {
                    @Override
                    public void accept(Headlines headlines) throws Exception {
                    populateNewsStoriesPass(headlines);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    populateNewsStoriesFail();
                }
            }));
    }

    private void requestFullScreenPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.USE_FULL_SCREEN_INTENT)) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for adequate notification of reminders")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()).getParent(),
                                    new String[] {Manifest.permission.USE_FULL_SCREEN_INTENT}, FULL_SCREEN_PERMISSION_CODE);
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
                    new String[] {Manifest.permission.USE_FULL_SCREEN_INTENT}, FULL_SCREEN_PERMISSION_CODE);
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION )) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()).getParent(),
                                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
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
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, STORAGE_PERMISSION_CODE);
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
    public void onResume() {
        super.onResume();
        //updateData();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
