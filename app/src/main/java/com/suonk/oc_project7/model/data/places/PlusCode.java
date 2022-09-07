package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

import java.util.Objects;

public class PlusCode {

    @NonNull
    private final String compound_code;

    @NonNull
    private final String global_code;

    public PlusCode(
            @NonNull String compound_code,
            @NonNull String global_code
    ) {
        this.compound_code = compound_code;
        this.global_code = global_code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlusCode plusCode = (PlusCode) o;
        return compound_code.equals(plusCode.compound_code) && global_code.equals(plusCode.global_code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(compound_code, global_code);
    }

    @Override
    public String toString() {
        return "PlusCode{" +
                "compound_code='" + compound_code + '\'' +
                ", global_code='" + global_code + '\'' +
                '}';
    }
}
