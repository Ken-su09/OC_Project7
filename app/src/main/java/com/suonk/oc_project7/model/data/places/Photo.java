package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.List;

public class Photo {

    @NonNull
    private int height = 0;
    @NonNull
    private List<String> htmlAttributions;
    @NonNull
    private String photo_reference = "";
    @NonNull
    private int width = 0;


    public Photo(@NonNull int height,
                 @NonNull List<String> htmlAttributions,
                 @NonNull String photo_reference,
                 @NonNull int width) {
        this.height = height;
        this.htmlAttributions = htmlAttributions;
        this.photo_reference = photo_reference;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    @NonNull
    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    @NonNull
    public String getPhotoReference() {
        return photo_reference;
    }

    public int getWidth() {
        return width;
    }
}