
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.suonk.oc_project7.model.data.places.Geometry;
import com.suonk.oc_project7.model.data.places.OpeningHours;
import com.suonk.oc_project7.model.data.places.Photo;
import com.suonk.oc_project7.model.data.places.PlusCode;

public class Result {

    @NonNull
    @SerializedName("address_components")
    private List<AddressComponent> address_components;

    @NonNull
    @SerializedName("adr_address")
    private String adr_address;

    @NonNull
    @SerializedName("business_status")
    private String business_status;

    @NonNull
    @SerializedName("formatted_address")
    private String formatted_address;

    @NonNull
    @SerializedName("formatted_phone_number")
    private String formatted_phone_number;

    @NonNull
    @Expose
    private Geometry geometry;

    @NonNull
    @Expose
    private String icon;

    @NonNull
    @SerializedName("icon_background_color")
    private String icon_background_color;

    @NonNull
    @SerializedName("icon_mask_base_uri")
    private String icon_mask_base_uri;

    @NonNull
    @SerializedName("international_phone_number")
    private String international_phone_number;

    @NonNull
    @Expose
    private String name;

    @NonNull
    @SerializedName("opening_hours")
    private OpeningHours opening_hours;

    @NonNull
    @Expose
    private List<Photo> photos;

    @NonNull
    @SerializedName("place_id")
    private String place_id;


    @NonNull
    @SerializedName("plus_code")
    private PlusCode plus_code;

    @NonNull
    @SerializedName("price_level")
    private Long price_level;

    @NonNull
    @Expose
    private Double rating;

    @NonNull
    @Expose
    private String reference;

    @NonNull
    @Expose
    private List<Review> reviews;

    @NonNull
    @Expose
    private List<String> types;

    @NonNull
    @Expose
    private String url;

    @NonNull
    @SerializedName("user_ratings_total")
    private Long user_ratings_total;

    @NonNull
    @SerializedName("utc_offset")
    private Long utc_offset;

    @NonNull
    @Expose
    private String vicinity;

    @NonNull
    @Expose
    private String website;

    public Result(@NonNull List<AddressComponent> address_components,
                  @NonNull String adr_address,
                  @NonNull String business_status,
                  @NonNull String formatted_address,
                  @NonNull String formatted_phone_number,
                  @NonNull Geometry geometry,
                  @NonNull String icon,
                  @NonNull String icon_background_color,
                  @NonNull String icon_mask_base_uri,
                  @NonNull String international_phone_number,
                  @NonNull String name,
                  @NonNull OpeningHours opening_hours,
                  @NonNull List<Photo> photos,
                  @NonNull String place_id,
                  @NonNull PlusCode plus_code,
                  @NonNull Long price_level,
                  @NonNull Double rating,
                  @NonNull String reference,
                  @NonNull List<Review> reviews,
                  @NonNull List<String> types,
                  @NonNull String url,
                  @NonNull Long user_ratings_total,
                  @NonNull Long utc_offset,
                  @NonNull String vicinity,
                  @NonNull String website) {
        this.address_components = address_components;
        this.adr_address = adr_address;
        this.business_status = business_status;
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
        this.geometry = geometry;
        this.icon = icon;
        this.icon_background_color = icon_background_color;
        this.icon_mask_base_uri = icon_mask_base_uri;
        this.international_phone_number = international_phone_number;
        this.name = name;
        this.opening_hours = opening_hours;
        this.photos = photos;
        this.place_id = place_id;
        this.plus_code = plus_code;
        this.price_level = price_level;
        this.rating = rating;
        this.reference = reference;
        this.reviews = reviews;
        this.types = types;
        this.url = url;
        this.user_ratings_total = user_ratings_total;
        this.utc_offset = utc_offset;
        this.vicinity = vicinity;
        this.website = website;
    }

    @NonNull
    public List<AddressComponent> getAddress_components() {
        return address_components;
    }

    @NonNull
    public String getAdr_address() {
        return adr_address;
    }

    @NonNull
    public String getBusiness_status() {
        return business_status;
    }

    @NonNull
    public String getFormatted_address() {
        return formatted_address;
    }

    @NonNull
    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    @NonNull
    public Geometry getGeometry() {
        return geometry;
    }

    @NonNull
    public String getIcon() {
        return icon;
    }

    @NonNull
    public String getIcon_background_color() {
        return icon_background_color;
    }

    @NonNull
    public String getIcon_mask_base_uri() {
        return icon_mask_base_uri;
    }

    @NonNull
    public String getInternational_phone_number() {
        return international_phone_number;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    @NonNull
    public List<Photo> getPhotos() {
        return photos;
    }

    @NonNull
    public String getPlace_id() {
        return place_id;
    }

    @NonNull
    public PlusCode getPlus_code() {
        return plus_code;
    }

    @NonNull
    public Long getPrice_level() {
        return price_level;
    }

    @NonNull
    public Double getRating() {
        return rating;
    }

    @NonNull
    public String getReference() {
        return reference;
    }

    @NonNull
    public List<Review> getReviews() {
        return reviews;
    }

    @NonNull
    public List<String> getTypes() {
        return types;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public Long getUser_ratings_total() {
        return user_ratings_total;
    }

    @NonNull
    public Long getUtc_offset() {
        return utc_offset;
    }

    @NonNull
    public String getVicinity() {
        return vicinity;
    }

    @NonNull
    public String getWebsite() {
        return website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return address_components.equals(result.address_components) && adr_address.equals(result.adr_address) && business_status.equals(result.business_status) && formatted_address.equals(result.formatted_address) && formatted_phone_number.equals(result.formatted_phone_number) && geometry.equals(result.geometry) && icon.equals(result.icon) && icon_background_color.equals(result.icon_background_color) && icon_mask_base_uri.equals(result.icon_mask_base_uri) && international_phone_number.equals(result.international_phone_number) && name.equals(result.name) && opening_hours.equals(result.opening_hours) && photos.equals(result.photos) && place_id.equals(result.place_id) && plus_code.equals(result.plus_code) && price_level.equals(result.price_level) && rating.equals(result.rating) && reference.equals(result.reference) && reviews.equals(result.reviews) && types.equals(result.types) && url.equals(result.url) && user_ratings_total.equals(result.user_ratings_total) && utc_offset.equals(result.utc_offset) && vicinity.equals(result.vicinity) && website.equals(result.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address_components, adr_address, business_status, formatted_address, formatted_phone_number, geometry, icon, icon_background_color, icon_mask_base_uri, international_phone_number, name, opening_hours, photos, place_id, plus_code, price_level, rating, reference, reviews, types, url, user_ratings_total, utc_offset, vicinity, website);
    }

    @Override
    public String toString() {
        return "Result{" +
                "address_components=" + address_components +
                ", adr_address='" + adr_address + '\'' +
                ", business_status='" + business_status + '\'' +
                ", formatted_address='" + formatted_address + '\'' +
                ", formatted_phone_number='" + formatted_phone_number + '\'' +
                ", geometry=" + geometry +
                ", icon='" + icon + '\'' +
                ", icon_background_color='" + icon_background_color + '\'' +
                ", icon_mask_base_uri='" + icon_mask_base_uri + '\'' +
                ", international_phone_number='" + international_phone_number + '\'' +
                ", name='" + name + '\'' +
                ", opening_hours=" + opening_hours +
                ", photos=" + photos +
                ", place_id='" + place_id + '\'' +
                ", plus_code=" + plus_code +
                ", price_level=" + price_level +
                ", rating=" + rating +
                ", reference='" + reference + '\'' +
                ", reviews=" + reviews +
                ", types=" + types +
                ", url='" + url + '\'' +
                ", user_ratings_total=" + user_ratings_total +
                ", utc_offset=" + utc_offset +
                ", vicinity='" + vicinity + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}