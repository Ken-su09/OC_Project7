package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Photo {

    private final int height;

    @NonNull
    private final List<String> htmlAttributions;

    @NonNull
    private final String photo_reference;

    private final int width;


    public Photo(int height,
                 @NonNull List<String> htmlAttributions,
                 @NonNull String photo_reference,
                 int width) {
        this.height = height;
        this.htmlAttributions = htmlAttributions;
        this.photo_reference = photo_reference;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    @NonNull
    public String getPhotoReference() {
        return photo_reference;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Photo photo = (Photo) o;
        return height == photo.height && width == photo.width && htmlAttributions.equals(photo.htmlAttributions) && photo_reference.equals(photo.photo_reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, htmlAttributions, photo_reference, width);
    }

    @NonNull
    @Override
    public String toString() {
        return "Photo{" +
                "height=" + height +
                ", htmlAttributions=" + htmlAttributions +
                ", photo_reference='" + photo_reference + '\'' +
                ", width=" + width +
                '}';
    }
}