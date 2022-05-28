package com.suonk.oc_project7.repositories.restaurants;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

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
    private final PlacesApiService placesApiService = mock(PlacesApiService.class);

    private final String defaultLocation = "48.9575329%2C2.5803872";

    private RestaurantsRepository restaurantsRepository;

    @Before
    public void setUp() {
        restaurantsRepository = new RestaurantsRepositoryImpl(placesApiService);
    }

    @Test
    public void get_near_restaurants_should_not_be_null_with_parameter_location() {
        List<Restaurant> restaurants = TestUtils.getValueForTesting(restaurantsRepository.getNearRestaurants(defaultLocation));

        assertNotNull(restaurants);
    }

    @Test
    public void get_near_restaurants_should_be_null_with_empty_parameter() {
        List<Restaurant> restaurants = TestUtils.getValueForTesting(restaurantsRepository.getNearRestaurants(""));

        assertNull(restaurants);
    }
}