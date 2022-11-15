package com.suonk.oc_project7.repositories.current_location;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.suonk.oc_project7.model.data.places.CurrentLocation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CurrentLocationRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FusedLocationProviderClient fusedLocationProviderClientMock = mock(FusedLocationProviderClient.class);
    private final Location currentLocationMock = mock(Location.class);
    private final Looper myLooper = mock(Looper.class);

    private CurrentLocationRepository currentLocationRepository;

    private static final double DEFAULT_LATITUDE = 48.9575329;
    private static final double DEFAULT_LONGITUDE = 2.5594583;
    private static final double END_DEFAULT_LATITUDE = 26.9575329;
    private static final double END_DEFAULT_LONGITUDE = -1.5594583;

    @Before
    public void setup() {
        currentLocationRepository = new CurrentLocationRepositoryImpl(fusedLocationProviderClientMock, myLooper);
    }

    @Test
    public void start_location_updates_and_remove_then_remove_location_updates() {
        // GIVEN
        currentLocationRepository.startLocationUpdates();

        // WHEN
        currentLocationRepository.stopLocationUpdates();

        // THEN
        verify(fusedLocationProviderClientMock).removeLocationUpdates(any(LocationCallback.class));
    }

    @Test
    public void start_location_updates_then_request_location_updates() {
        // GIVEN
        ArgumentCaptor<LocationCallback> argumentCaptor = ArgumentCaptor.forClass(LocationCallback.class);
        LocationResult locationResult = mock(LocationResult.class);
        Location location = mock(Location.class);

        doReturn(DEFAULT_LATITUDE).when(location).getLatitude();
        doReturn(DEFAULT_LONGITUDE).when(location).getLongitude();
        doReturn(location).when(locationResult).getLastLocation();

        // WHEN
        currentLocationRepository.startLocationUpdates();

        // THEN
        verify(fusedLocationProviderClientMock).requestLocationUpdates(any(), argumentCaptor.capture(), any());

        // WHEN II
        LocationCallback locationCallback = argumentCaptor.getValue();
        locationCallback.onLocationResult(locationResult);

        // THEN II
        CurrentLocation currentLocation = currentLocationRepository.getLocationMutableLiveData().getValue();

        assertEquals(getDefaultCurrentLocation(), currentLocation);
    }

    @Test
    public void start_location_updates_then_request_location_updates_with_location_callback_null() {
        // GIVEN

        // WHEN
        currentLocationRepository.startLocationUpdates();

        // THEN
        CurrentLocation currentLocation = currentLocationRepository.getLocationMutableLiveData().getValue();

        assertNull(currentLocation);
    }

    @Test
    public void stop_location_updates_with_location_call_back_null() {
        // WHEN
        currentLocationRepository.stopLocationUpdates();

        // THEN
        verifyNoMoreInteractions(fusedLocationProviderClientMock, myLooper);
    }

    @Test
    public void test_location_request() {
        // GIVEN
        ArgumentCaptor<LocationRequest> argumentCaptor = ArgumentCaptor.forClass(LocationRequest.class);

        // WHEN
        currentLocationRepository.startLocationUpdates();

        // THEN
        verify(fusedLocationProviderClientMock).requestLocationUpdates(argumentCaptor.capture(), (LocationCallback) any(), any());
    }

    @Test
    public void get_distance_from_two_locations() {
        // GIVEN

        // WHEN
        currentLocationRepository.getDistanceFromTwoLocations(
                DEFAULT_LATITUDE,
                DEFAULT_LONGITUDE,
                END_DEFAULT_LATITUDE,
                END_DEFAULT_LONGITUDE
        );

        // THEN

        verifyNoMoreInteractions(fusedLocationProviderClientMock, myLooper, currentLocationMock);
    }

    @NonNull
    private CurrentLocation getDefaultCurrentLocation() {
        return new CurrentLocation(48.9575329, 2.5594583);
    }
}