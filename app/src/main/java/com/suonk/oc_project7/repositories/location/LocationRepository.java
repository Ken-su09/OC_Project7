package com.suonk.oc_project7.repositories.location;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;

public interface LocationRepository {

    @NonNull
    LiveData<Location> getLocationMutableLiveData();

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    void startLocationUpdate();

    void stopLocationUpdate();
}