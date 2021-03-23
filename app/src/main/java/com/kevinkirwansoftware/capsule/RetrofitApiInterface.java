package com.kevinkirwansoftware.capsule;

import com.kevinkirwansoftware.capsule.throwaway.BookListModel;
import com.kevinkirwansoftware.capsule.throwaway.Headlines;
import com.kevinkirwansoftware.capsule.throwaway.Post;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiInterface {
    @GET("data/2.5/weather?")
    Observable<JSONObject> getCurrentWeatherData(@Query("lat") String lat, @Query("lon") String lon, @Query("APPID") String app_id);

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

    @GET("posts")
    Observable<List<Post>> getPosts(@Query("q") String query);

    @GET("posts")
    Observable<List<Post>> getPosts();

    @GET("posts")
    Observable<JSONObject> getPostsJson();

}
