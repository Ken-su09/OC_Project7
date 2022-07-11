
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Review {

    @NonNull
    @SerializedName("author_name")
    private final String authorName;

    @NonNull
    @SerializedName("author_url")
    private final String authorUrl;

    @NonNull
    @Expose
    private final String language;

    @NonNull
    @SerializedName("profile_photo_url")
    private final String profilePhotoUrl;

    @NonNull
    @Expose
    private final Long rating;

    @NonNull
    @SerializedName("relative_time_description")
    private final String relativeTimeDescription;

    @NonNull
    @Expose
    private final String text;

    @NonNull
    @Expose
    private final Long time;

    public Review(@NonNull String authorName,
                  @NonNull String authorUrl,
                  @NonNull String language,
                  @NonNull String profilePhotoUrl,
                  @NonNull Long rating,
                  @NonNull String relativeTimeDescription,
                  @NonNull String text,
                  @NonNull Long time) {
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.language = language;
        this.profilePhotoUrl = profilePhotoUrl;
        this.rating = rating;
        this.relativeTimeDescription = relativeTimeDescription;
        this.text = text;
        this.time = time;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public String getLanguage() {
        return language;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public Long getRating() {
        return rating;
    }

    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    public String getText() {
        return text;
    }

    public Long getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return authorName.equals(review.authorName) && authorUrl.equals(review.authorUrl) && language.equals(review.language) && profilePhotoUrl.equals(review.profilePhotoUrl) && rating.equals(review.rating) && relativeTimeDescription.equals(review.relativeTimeDescription) && text.equals(review.text) && time.equals(review.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName, authorUrl, language, profilePhotoUrl, rating, relativeTimeDescription, text, time);
    }

    @Override
    public String toString() {
        return "Review{" +
                "authorName='" + authorName + '\'' +
                ", authorUrl='" + authorUrl + '\'' +
                ", language='" + language + '\'' +
                ", profilePhotoUrl='" + profilePhotoUrl + '\'' +
                ", rating=" + rating +
                ", relativeTimeDescription='" + relativeTimeDescription + '\'' +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}