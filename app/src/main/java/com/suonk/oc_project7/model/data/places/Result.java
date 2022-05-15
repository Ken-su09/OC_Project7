package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.List;

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
    private String icon_mask_base_uri;
    @NonNull
    private String name;
    @NonNull
    private final OpeningHours openingHours;
    @NonNull
    private Boolean permanently_closed = false;
    @NonNull
    private List<Photo> photos;
    @NonNull
    private String place_id;
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
            @NonNull OpeningHours openingHours,
            @NonNull Boolean permanently_closed,
            @NonNull List<Photo> photos,
            @NonNull String place_id,
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
        this.openingHours = openingHours;
        this.permanently_closed = permanently_closed;
        this.photos = photos;
        this.place_id = place_id;
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
        return openingHours;
    }

    @NonNull
    public Boolean getPermanently_closed() {
        return permanently_closed;
    }

    @NonNull
    public String getPlace_id() {
        return place_id;
    }
}
