package com.kevinkirwansoftware.capsule.throwaway;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Source {
    public String id;
    public String name;

    public Source(String id, String name){
        this.id = id;
        this.name = name;
    }

}
