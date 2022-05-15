package com.suonk.oc_project7.model.data.places;

import androidx.annotation.NonNull;

public class PlusCode {

    @NonNull
    private String compound_code;
    @NonNull
    private String global_code;

    public PlusCode(
            @NonNull String compound_code,
            @NonNull String global_code
    ) {
        this.compound_code = compound_code;
        this.global_code = global_code;
    }

    @NonNull
    public String getCompound_code() {
        return compound_code;
    }

    @NonNull
    public String getGlobal_code() {
        return global_code;
    }
}
