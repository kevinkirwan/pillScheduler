package com.kevinkirwansoftware.capsule;

import com.kevinkirwansoftware.capsule.throwaway.BookListModel;
import com.kevinkirwansoftware.capsule.throwaway.Headlines;
import com.kevinkirwansoftware.capsule.throwaway.Post;
import com.kevinkirwansoftware.capsule.throwaway.weather.WeatherResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiInterface {
    @GET("onecall")
    Observable<WeatherResponse> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("exclude") String exclude, @Query("appid") String app_id);

    @GET("onecall")
    Observable<JSONObject> getCurrentWeatherDataJson(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("appid") String app_id);

    @GET("volumes")
    Observable<BookListModel> getBookListFromApi(@Query("q") String query);


    @GET("0e340a8de1494e5f87634a3506ee3300")
    Observable<BookListModel> getTopHeadlines(@Query("query") String query);

    @GET("v2/everything?")
    Observable<List<JSONObject>> getTopHeadlinesTest2(@Query("q") String query, @Query("from") String date, @Query("sortBy") String sortBy, @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Observable<Headlines> getHeadlines(
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Observable<Headlines> getSpecificData(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );
    // News API Key: 0e340a8de1494e5f87634a3506ee3300

    @GET("everything")
    Observable<Headlines> getRawData(@Query("apiKey") String apiKey, @Query("q") String query, @Query("from") String date);

    @GET("everything")
    Observable<Headlines> getRawData(@Query("apiKey") String apiKey, @Query("q") String query, @Query("sources") String source, @Query("from") String date);

    @GET("everything")
    Observable<Headlines> getRawData(@Query("apiKey") String apiKey, @Query("q") String query, @Query("sources") String source, @Query("language") String language, @Query("from") String date);

    @GET("everything")
    Observable<Headlines> getRawData(@Query("apiKey") String apiKey, @Query("q") String query);

    @GET("top-headlines")
    Observable<Headlines> getTopByCatergory(@Query("apiKey") String apiKey, @Query("category") String query);

    @GET("top-headlines")
    Observable<Headlines> getTopByLanguage(@Query("apiKey") String apiKey, @Query("language") String query);

    @GET("top-headlines")
    Observable<Headlines> getTopByQuery(@Query("apiKey") String apiKey, @Query("q") String query);

    @GET("posts")
    Observable<List<Post>> getPosts(@Query("q") String query);

    @GET("posts")
    Observable<List<Post>> getPosts();

    @GET("posts")
    Observable<JSONObject> getPostsJson();

}
