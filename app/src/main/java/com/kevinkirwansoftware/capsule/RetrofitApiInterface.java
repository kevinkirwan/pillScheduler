package com.kevinkirwansoftware.capsule;

import com.kevinkirwansoftware.capsule.throwaway.BookListModel;

import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiInterface {
    @GET("data/2.5/weather?")
    Observable<JSONObject> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);

    @GET("volumes")
    Observable<BookListModel> getBookListFromApi(@Query("q") String query);

}
