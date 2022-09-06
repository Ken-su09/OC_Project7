
package com.suonk.oc_project7.model.data.place_details;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Objects;

public class AddressComponent {

    @NonNull
    private final String longName;

    @NonNull
    private final String shortName;

    @NonNull
    @Expose
    private final List<String> types;

    public AddressComponent(@NonNull String longName, @NonNull String shortName, @NonNull List<String> types) {
        this.longName = longName;
        this.shortName = shortName;
        this.types = types;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressComponent that = (AddressComponent) o;
        return longName.equals(that.longName) && shortName.equals(that.shortName) && types.equals(that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longName, shortName, types);
    }

    @NonNull
    @Override
    public String toString() {
        return "AddressComponent{" +
                "longName='" + longName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", types=" + types +
                '}';
    }
}