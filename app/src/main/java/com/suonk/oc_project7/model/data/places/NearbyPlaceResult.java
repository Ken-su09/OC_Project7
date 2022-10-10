package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

public class NearbyPlaceResult {

    @NonNull
    private final String business_status;

    @NonNull
    private final Geometry geometry;

    @NonNull
    private final String icon;

    @NonNull
    private final String icon_background_color;

    @NonNull
    private final String icon_mask_base_uri;

    @NonNull
    private final String name;

    @Nullable
    private final OpeningHours opening_hours;

    @Nullable
    private final Boolean permanently_closed;

    @Nullable
    private final List<Photo> photos;

    @NonNull
    private final String place_id;

    @NonNull
    private final PlusCode plus_code;

    private final int price_level;

    @NonNull
    private final Double rating;

    @NonNull
    private final String reference;

    @NonNull
    private final String scope;

    @NonNull
    private final List<String> types;

    private final int user_ratings_total;

    @NonNull
    private final String vicinity;

    public NearbyPlaceResult(
            @NonNull String business_status,
            @NonNull Geometry geometry,
            @NonNull String icon,
            @NonNull String icon_background_color,
            @NonNull String icon_mask_base_uri,
            @NonNull String name,
            @Nullable OpeningHours opening_hours,
            @Nullable Boolean permanently_closed,
            @Nullable List<Photo> photos,
            @NonNull String placeId,
            @NonNull PlusCode plus_code,
            int price_level,
            @NonNull Double rating,
            @NonNull String reference,
            @NonNull String scope,
            @NonNull List<String> types,
            int user_ratings_total,
            @NonNull String vicinity) {
        this.business_status = business_status;
        this.geometry = geometry;
        this.icon = icon;
        this.icon_background_color = icon_background_color;
        this.icon_mask_base_uri = icon_mask_base_uri;
        this.name = name;
        this.opening_hours = opening_hours;
        this.permanently_closed = permanently_closed;
        this.photos = photos;
        this.place_id = placeId;
        this.plus_code = plus_code;
        this.price_level = price_level;
        this.rating = rating;
        this.reference = reference;
        this.scope = scope;
        this.types = types;
        this.user_ratings_total = user_ratings_total;
        this.vicinity = vicinity;
    }

    @NonNull
    public Geometry getGeometry() {
        return geometry;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public OpeningHours getOpeningHours() {
        return opening_hours;
    }

    @NonNull
    public String getPlaceId() {
        return place_id;
    }

    @NonNull
    public String getVicinity() {
        return vicinity;
    }

    @NonNull
    public Double getRating() {
        return rating;
    }

    @Nullable
    public List<Photo> getPhotos() {
        return photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NearbyPlaceResult that = (NearbyPlaceResult) o;
        return price_level == that.price_level && user_ratings_total == that.user_ratings_total && business_status.equals(that.business_status) && geometry.equals(that.geometry) && icon.equals(that.icon) && icon_background_color.equals(that.icon_background_color) && icon_mask_base_uri.equals(that.icon_mask_base_uri) && name.equals(that.name) && Objects.equals(opening_hours, that.opening_hours) && Objects.equals(permanently_closed, that.permanently_closed) && Objects.equals(photos, that.photos) && place_id.equals(that.place_id) && plus_code.equals(that.plus_code) && rating.equals(that.rating) && reference.equals(that.reference) && scope.equals(that.scope) && types.equals(that.types) && vicinity.equals(that.vicinity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(business_status, geometry, icon, icon_background_color, icon_mask_base_uri, name, opening_hours, permanently_closed, photos, place_id, plus_code, price_level, rating, reference, scope, types, user_ratings_total, vicinity);
    }

    @NonNull
    @Override
    public String toString() {
        return "NearbyPlaceResult{" +
                "business_status='" + business_status + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", icon_background_color='" + icon_background_color + '\'' +
                ", icon_mask_base_uri='" + icon_mask_base_uri + '\'' +
                ", name='" + name + '\'' +
                ", opening_hours=" + opening_hours +
                ", permanently_closed=" + permanently_closed +
                ", photos=" + photos +
                ", place_id='" + place_id + '\'' +
                ", plus_code=" + plus_code +
                ", price_level=" + price_level +
                ", rating=" + rating +
                ", reference='" + reference + '\'' +
                ", scope='" + scope + '\'' +
                ", types=" + types +
                ", user_ratings_total=" + user_ratings_total +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}
