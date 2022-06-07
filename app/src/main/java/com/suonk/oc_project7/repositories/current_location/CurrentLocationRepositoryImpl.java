package com.suonk.oc_project7.repositories.current_location;

import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.suonk.oc_project7.model.data.places.CurrentLocation;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrentLocationRepositoryImpl implements CurrentLocationRepository {

    @NonNull
    private final MutableLiveData<CurrentLocation> locationMutableLiveData = new MutableLiveData<>();

    @NonNull
    private final FusedLocationProviderClient locationProviderClient;

    @NonNull
    private final Looper myLooper;

    @NonNull
    private final LocationRequest locationRequest = LocationRequest.create()
            .setInterval(120_000)
            .setFastestInterval(120_000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    private LocationCallback locationCallback;

    @Inject
    public CurrentLocationRepositoryImpl(@NonNull FusedLocationProviderClient locationProviderClient,
                                         @NonNull Looper myLooper) {
        this.locationProviderClient = locationProviderClient;
        this.myLooper = myLooper;
    }

    @NonNull
    @Override
    public LiveData<CurrentLocation> getLocationMutableLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    @Override
    public void startLocationUpdates() {
        if (locationCallback == null) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {

                    locationMutableLiveData.setValue(new CurrentLocation(
                            locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude()
                    ));
                }
            };
        }
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, myLooper);
    }

    @Override
    public void stopLocationUpdates() {
        locationProviderClient.removeLocationUpdates(locationCallback);
    }
}