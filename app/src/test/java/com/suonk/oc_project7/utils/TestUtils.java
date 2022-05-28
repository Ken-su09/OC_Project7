package com.suonk.oc_project7.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public class TestUtils {
    @NonNull
    public static <T> T getValueForTesting(@NonNull LiveData<T> liveData) {
        liveData.observeForever(t -> {
        });
        T captured = liveData.getValue();

        if (captured == null) {
            throw new AssertionError("LiveData value was null after observe");
        }

        return captured;
    }
}
