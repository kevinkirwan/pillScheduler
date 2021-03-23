package com.kevinkirwansoftware.capsule.throwaway;

import com.google.gson.annotations.SerializedName;

public class Post {

    private int userId;

    private int id;

    private String title;

    @SerializedName("body")
    private String text;

    public Post(int userId, int id, String title, String text){
        this.userId = id;
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
