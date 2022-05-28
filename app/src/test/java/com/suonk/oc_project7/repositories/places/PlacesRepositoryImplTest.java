package com.suonk.oc_project7.repositories.places;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PlacesRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @NonNull
    private final PlacesApiService placesApiService = mock(PlacesApiService.class);

    private final String defaultLocation = "48.9575329%2C2.5803872";

    private PlacesRepository placesRepository;

    @Before
    public void setUp() {
        placesRepository = new PlacesRepositoryImpl(placesApiService);
    }

    @Test
    public void get_nearby_places_should_not_be_null_or_empty() {
        List<Place> places = TestUtils.getValueForTesting(placesRepository.getNearbyPlaceResponse(defaultLocation));

        assertNotNull(places);
    }

    @Test
    public void get_nearby_places_should_be_null_with_empty_parameter() {
        List<Place> places = TestUtils.getValueForTesting(placesRepository.getNearbyPlaceResponse(""));

        assertNull(places);
    }
}