package com.kevinkirwansoftware.capsule.throwaway;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Headlines {
    private String status;
    private int totalResults;
    private List<Articles> articles;

    public Headlines(String status, int totalResults, List<Articles> articles){
        Log.d("Kevin", "Total results: " + totalResults);
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getSourceName(int index){
        return articles.get(index).getSource().name;
    }

    public String getAuthor(int index){
        return articles.get(index).getAuthor();
    }

    public String getTitle(int index){
        return articles.get(index).getTitle();
    }

    public String getContent(int index){
        return articles.get(index).getContent();
    }

    public String getImageUrl(int index){
        return articles.get(index).getUrlToImage();
    }

    public int getTotalResults(){
        return totalResults;
    }

    public List<Articles> getArticles(){
        return articles;
    }

    /*
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalResults")
    @Expose
    private String totalResults;

    @SerializedName("articles")
    @Expose
    private List<Articles> articles;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<Articles> getArticles() {
        return articles;
    }

    public void setArticles(List<Articles> articles) {
        this.articles = articles;
    }

     */
}
