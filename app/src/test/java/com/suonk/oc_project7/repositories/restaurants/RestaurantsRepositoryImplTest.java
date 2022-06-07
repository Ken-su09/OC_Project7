package com.suonk.oc_project7.repositories.restaurants;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.restaurant.Restaurant;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepositoryImpl;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class RestaurantsRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @NonNull
    private final PlacesApiService placesApiServiceMock = mock(PlacesApiService.class);

    @NonNull
    private final Location locationMock = mock(Location.class);

    private RestaurantsRepository restaurantsRepository;

    @Before
    public void setUp() {
        doReturn(48.9575329).when(locationMock).getLatitude();
        doReturn(2.5594583).when(locationMock).getLongitude();
        restaurantsRepository = new RestaurantsRepositoryImpl(placesApiServiceMock);
    }

    @Test
    public void get_near_restaurants_with_default_location() {
        // WHEN
        String defaultLocation = locationMock.getLatitude() + "," + locationMock.getLongitude();
        restaurantsRepository.getNearRestaurants(defaultLocation);

        // THEN
        verify(placesApiServiceMock).getNearbyPlacesResponse(defaultLocation);
        verifyNoMoreInteractions(placesApiServiceMock);
    }
}