package com.kevinkirwansoftware.capsule;

import androidx.annotation.NonNull;

public class GraphItem {
    String tag;
    String name;

    public GraphItem(String tag, String name){
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
