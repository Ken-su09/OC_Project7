package com.suonk.oc_project7.repositories.current_location;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.places.CurrentLocation;

public interface CurrentLocationRepository {

    @NonNull
    LiveData<CurrentLocation> getLocationMutableLiveData();

    float getDistanceFromTwoLocations(double startLat, double startLng,
                                      double endLat, double endLng);

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    void startLocationUpdates();

    void stopLocationUpdates();
}