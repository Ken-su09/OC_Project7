package com.suonk.oc_project7.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.model.data.permission_checker.PermissionChecker;
import com.suonk.oc_project7.model.data.uri.UriConverter;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class MainViewModel extends ViewModel {

    @NonNull
    private final CurrentLocationRepository locationRepository;

    private final MutableLiveData<Boolean> isPermissionEnabledLiveData = new MutableLiveData<>();

    private MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();

    private final Context context;

    private final PermissionChecker permissionChecker;
//    private final UriConverter uriConverter;

    @NonNull
    private final FirebaseUser firebaseUser;

    @Inject
    public MainViewModel(@NonNull CurrentLocationRepository locationRepository,
                         @NonNull FirebaseAuth auth,
                         @NonNull PermissionChecker permissionChecker,
//                         @NonNull UriConverter uriConverter,
                         @ApplicationContext Context context) {
        this.locationRepository = locationRepository;
        this.permissionChecker = permissionChecker;
//        this.uriConverter = uriConverter;
        this.context = context;

        firebaseUser = auth.getCurrentUser();
    }

    @SuppressLint("MissingPermission")
    public void onStart() {
        if (permissionChecker.hasFineLocationPermission() || permissionChecker.hasCoarseLocationPermission()) {
            locationRepository.startLocationUpdates();
            isPermissionEnabledLiveData.setValue(true);
        } else {
            locationRepository.stopLocationUpdates();
            isPermissionEnabledLiveData.setValue(false);
        }
    }

    public void onResume() {
        if (permissionChecker.hasFineLocationPermission() || permissionChecker.hasCoarseLocationPermission()) {
            isPermissionEnabledLiveData.setValue(true);
        } else {
            isPermissionEnabledLiveData.setValue(false);
        }
    }

    public void onStop() {
        locationRepository.stopLocationUpdates();
        isPermissionEnabledLiveData.setValue(false);
    }

    public LiveData<MainViewState> getMainViewStateLiveData() {
        if (firebaseUser != null) {
            if (firebaseUser.getPhotoUrl() != null) {
                mainViewStateLiveData.setValue(new MainViewState(
                        firebaseUser.getDisplayName(),
                        firebaseUser.getEmail(),
                        firebaseUser.getPhotoUrl().toString()
                ));
            } else {
                mainViewStateLiveData.setValue(new MainViewState(
                        firebaseUser.getDisplayName(),
                        firebaseUser.getEmail(),
                        ""
                ));
            }
        }

        return mainViewStateLiveData;
    }

    public LiveData<Boolean> getPermissionsLiveData() {
        return isPermissionEnabledLiveData;
    }
}