package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class Result {

    @NonNull
    private String business_status;
    @NonNull
    private Geometry geometry;
    @NonNull
    private String icon;
    @NonNull
    private String icon_background_color;
    @NonNull
    private final String icon_mask_base_uri;
    @NonNull
    private String name;
    @NonNull
    private OpeningHours opening_hours;
    @NonNull
    private Boolean permanently_closed = false;
    @NonNull
    private List<Photo> photos;
    @NonNull
    private String placeId;
    @NonNull
    private PlusCode plus_code;
    @NonNull
    private int price_level = 0;
    @NonNull
    private Double rating = 0.0;
    @NonNull
    private String reference = "";
    @NonNull
    private String scope = "";
    @NonNull
    private List<String> types;
    @NonNull
    private int user_ratings_total = 0;
    @NonNull
    private String vicinity = "";

    public Result(
            @NonNull String business_status,
            @NonNull Geometry geometry,
            @NonNull String icon,
            @NonNull String icon_background_color,
            @NonNull String icon_mask_base_uri,
            @NonNull String name,
            @NonNull OpeningHours opening_hours,
            @NonNull Boolean permanently_closed,
            @NonNull List<Photo> photos,
            @NonNull String placeId,
            @NonNull PlusCode plus_code,
            @NonNull int price_level,
            @NonNull Double rating,
            @NonNull String reference,
            @NonNull String scope,
            @NonNull List<String> types,
            @NonNull int user_ratings_total,
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
        this.placeId = placeId;
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
    public String getBusiness_status() {
        return business_status;
    }

    @NonNull
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(@NonNull Geometry geometry) {
        this.geometry = geometry;
    }

    @NonNull
    public String getIcon() {
        return icon;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public OpeningHours getOpeningHours() {
        return opening_hours;
    }

    @NonNull
    public Boolean getPermanently_closed() {
        return permanently_closed;
    }

    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    @NonNull
    public String getVicinity() {
        return vicinity;
    }

    @NonNull
    public Double getRating() {
        return rating;
    }

    @NonNull
    public List<Photo> getPhotos() {
        return photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return price_level == result.price_level && user_ratings_total == result.user_ratings_total &&
                business_status.equals(result.business_status) && geometry.equals(result.geometry) &&
                icon.equals(result.icon) && icon_background_color.equals(result.icon_background_color) &&
                icon_mask_base_uri.equals(result.icon_mask_base_uri) && name.equals(result.name) &&
                opening_hours.equals(result.opening_hours) && permanently_closed.equals(result.permanently_closed) &&
                photos.equals(result.photos) && placeId.equals(result.placeId) && plus_code.equals(result.plus_code) &&
                rating.equals(result.rating) && reference.equals(result.reference) && scope.equals(result.scope) &&
                types.equals(result.types) && vicinity.equals(result.vicinity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(business_status, geometry, icon, icon_background_color, icon_mask_base_uri,
                name, opening_hours, permanently_closed, photos, placeId, plus_code, price_level, rating,
                reference, scope, types, user_ratings_total, vicinity);
    }

    @Override
    public String toString() {
        return "Result{" +
                "business_status='" + business_status + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", icon_background_color='" + icon_background_color + '\'' +
                ", icon_mask_base_uri='" + icon_mask_base_uri + '\'' +
                ", name='" + name + '\'' +
                ", opening_hours=" + opening_hours +
                ", permanently_closed=" + permanently_closed +
                ", photos=" + photos +
                ", place_id='" + placeId + '\'' +
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
