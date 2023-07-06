package com.suonk.oc_project7.model.data.permission_checker;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PermissionChecker {

    @NonNull
    private final Context context;

    @Inject
    public PermissionChecker(@NonNull Application context) {
        this.context = context;
    }

    public boolean hasFineLocationPermission() {
        return ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }

    public boolean hasCoarseLocationPermission() {
        return ContextCompat.checkSelfPermission(context, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED;
    }
}
