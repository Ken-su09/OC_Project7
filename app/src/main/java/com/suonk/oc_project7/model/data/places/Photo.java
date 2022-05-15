package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.List;

public class Photo {

    @NonNull
    private int height = 0;
    @NonNull
    private List<String> html_attributions;
    @NonNull
    private String photo_reference = "";
    @NonNull
    private int width = 0;


    public Photo(@NonNull int height,
                 @NonNull List<String> html_attributions,
                 @NonNull String photo_reference,
                 @NonNull int width) {
        this.height = height;
        this.html_attributions = html_attributions;
        this.photo_reference = photo_reference;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    @NonNull
    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    @NonNull
    public String getPhoto_reference() {
        return photo_reference;
    }

    public int getWidth() {
        return width;
    }
}