package com.suonk.oc_project7.repositories.current_location;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.location.Location;
import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.suonk.oc_project7.model.data.places.CurrentLocation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LocationRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FusedLocationProviderClient fusedLocationProviderClientMock = mock(FusedLocationProviderClient.class);

    private final Looper myLooper = mock(Looper.class);

    private CurrentLocationRepository currentLocationRepository;

    @Before
    public void setUp() {
        currentLocationRepository = new CurrentLocationRepositoryImpl(
                fusedLocationProviderClientMock,
                myLooper);
    }

    @Test
    public void start_location_updates_and_remove_then_remove_location_updates() {
        // GIVEN
        currentLocationRepository.startLocationUpdates();

        // WHEN
        currentLocationRepository.stopLocationUpdates();

        // THEN
        Mockito.verify(fusedLocationProviderClientMock).removeLocationUpdates(any(LocationCallback.class));
    }

    @Test
    public void start_location_updates_then_request_location_updates() {
        // GIVEN
        ArgumentCaptor<LocationCallback> argumentCaptor = ArgumentCaptor.forClass(LocationCallback.class);
        LocationResult locationResult = mock(LocationResult.class);
        Location location = mock(Location.class);
        Mockito.doReturn(40.2).when(location).getLatitude();
        Mockito.doReturn(2.8).when(location).getLongitude();
        Mockito.doReturn(location).when(locationResult).getLastLocation();

        // WHEN
        currentLocationRepository.startLocationUpdates();

        // THEN
        Mockito.verify(fusedLocationProviderClientMock).requestLocationUpdates(any(), argumentCaptor.capture(), any());

        // WHEN II
        LocationCallback locationCallback = argumentCaptor.getValue();
        locationCallback.onLocationResult(locationResult);

        // THEN II
        CurrentLocation currentLocation = currentLocationRepository.getLocationMutableLiveData().getValue();

        assertEquals(new CurrentLocation(40.2, 2.8), currentLocation);
    }
}