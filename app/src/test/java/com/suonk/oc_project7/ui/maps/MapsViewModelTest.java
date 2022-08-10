package com.suonk.oc_project7.ui.maps;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
import com.suonk.oc_project7.repositories.restaurants.RestaurantsRepository;
import com.suonk.oc_project7.ui.main.MainViewModel;
import com.suonk.oc_project7.ui.restaurants.list.RestaurantItemViewState;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MapsViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private MapsViewModel viewModel;

    private final CurrentLocationRepository locationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final PlacesRepository placesRepository = Mockito.mock(PlacesRepository.class);
    private final CurrentLocation currentLocation = mock(CurrentLocation.class);

    private final String DEFAULT_ID = "DEFAULT_ID";
    private static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    private static final boolean RESTAURANT_IS_OPEN_TRUE = true;
    private static final boolean RESTAURANT_IS_OPEN_FALSE = false;

    private static final double LATITUDE = -1.256546;
    private static final double LONGITUDE = 8.256546;
    private static final String LOCATION = LATITUDE + "," + LONGITUDE;

    @NonNull
    private final MutableLiveData<CurrentLocation> currentLocationLiveData = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<List<MapMarker>> mapMarkersLiveData = new MutableLiveData<>();

    @NonNull
    private final MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        doReturn(LATITUDE).when(currentLocation).getLat();
        doReturn(LONGITUDE).when(currentLocation).getLng();
        doReturn(currentLocationLiveData).when(locationRepository).getLocationMutableLiveData();
        doReturn(placesLiveData).when(placesRepository).getNearbyPlaceResponse(LOCATION);

        currentLocationLiveData.setValue(currentLocation);
        placesLiveData.setValue(getDefaultPlaces());
        mapMarkersLiveData.setValue(getDefaultMapMarkers());

        viewModel = new MapsViewModel(locationRepository, placesRepository);

        verify(locationRepository).getLocationMutableLiveData();
    }

    @Test
    public void test_get_map_markers() {
        // When
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapMakersLiveData());

        assertNotNull(mapMarkers);
        assertEquals(2, mapMarkers.size());
        assertEquals(getDefaultMapMarkers(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository);
    }

    @Test
    public void test_get_map_markers_if_places_null() {
        // Given
        placesLiveData.setValue(null);

        // When
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapMakersLiveData());

        assertNotNull(mapMarkers);
        assertEquals(0, mapMarkers.size());

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository);
    }

    private List<Place> getDefaultPlaces() {
        List<Place> places = new ArrayList<>();

        places.add(new Place(DEFAULT_ID, RESTAURANT_NAME, LATITUDE, LONGITUDE, RESTAURANT_IS_OPEN_TRUE));
        places.add(new Place(DEFAULT_ID, RESTAURANT_NAME, LATITUDE, LONGITUDE, RESTAURANT_IS_OPEN_FALSE));

        return places;
    }

    private List<MapMarker> getDefaultMapMarkers() {
        List<MapMarker> mapMarkers = new ArrayList<>();

        Float DEFAULT_COLOR = 120.0F;
        mapMarkers.add(new MapMarker(DEFAULT_ID, LATITUDE, LONGITUDE, RESTAURANT_NAME, DEFAULT_COLOR));
        Float DEFAULT_COLOR_2 = 0.0F;
        mapMarkers.add(new MapMarker(DEFAULT_ID, LATITUDE, LONGITUDE, RESTAURANT_NAME, DEFAULT_COLOR_2));

        return mapMarkers;
    }
}