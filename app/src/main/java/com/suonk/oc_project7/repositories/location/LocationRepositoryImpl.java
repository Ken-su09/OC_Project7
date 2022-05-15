package com.suonk.oc_project7.repositories.location;

import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LocationRepositoryImpl implements LocationRepository {

    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();

    @NonNull
    private final FusedLocationProviderClient locationProviderClient;

    @NonNull
    private final LocationRequest locationRequest = LocationRequest.create()
            .setInterval(120_000)
            .setFastestInterval(120_000)
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

    private LocationCallback locationCallback;

    @Inject
    public LocationRepositoryImpl(@NonNull FusedLocationProviderClient locationProviderClient) {
        this.locationProviderClient = locationProviderClient;
    }

    @NonNull
    @Override
    public LiveData<Location> getLocationMutableLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(
            anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"}
    )
    @Override
    public void startLocationUpdate() {
        if (locationCallback == null) {
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    locationMutableLiveData.setValue(locationResult.getLastLocation());
                }
            };
        }
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void stopLocationUpdate() {
        locationProviderClient.removeLocationUpdates(locationCallback);
    }
}