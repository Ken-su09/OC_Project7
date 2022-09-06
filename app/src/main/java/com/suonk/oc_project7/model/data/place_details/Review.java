
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class Review {

    @NonNull
    private final String authorName;

    @NonNull
    private final String authorUrl;

    @NonNull
    private final String language;

    @NonNull
    private final String profilePhotoUrl;

    @NonNull
    private final Long rating;

    @NonNull
    private final String relativeTimeDescription;

    @NonNull
    private final String text;

    @NonNull
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

    @NonNull
    public String getAuthorName() {
        return authorName;
    }

    @NonNull
    public String getAuthorUrl() {
        return authorUrl;
    }

    @NonNull
    public String getLanguage() {
        return language;
    }

    @NonNull
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    @NonNull
    public Long getRating() {
        return rating;
    }

    @NonNull
    public String getRelativeTimeDescription() {
        return relativeTimeDescription;
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
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

    @NonNull
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