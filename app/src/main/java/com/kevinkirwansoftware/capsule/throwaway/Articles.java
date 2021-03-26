package com.kevinkirwansoftware.capsule.throwaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Articles {
    private Source source;
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private String content;

    public Articles(Source source,
                    String author,
                    String title,
                    String description,
                    String url,
                    String urlToImage,
                    String publishedAt,
                    String content){
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public Source getSource() {
        return source;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getUrlToImage() { return urlToImage; }
}
