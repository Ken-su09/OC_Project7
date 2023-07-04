package com.suonk.oc_project7.ui.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.places.Place;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_location.CurrentLocationRepository;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.repositories.places.PlacesRepository;
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

    private MapViewModel viewModel;

    //region ============================================= MOCK =============================================

    private final CurrentLocationRepository locationRepository = Mockito.mock(CurrentLocationRepository.class);
    private final GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCaseMock = mock(GetWorkmatesHaveChosenTodayUseCase.class);
    private final PlacesRepository placesRepository = Mockito.mock(PlacesRepository.class);
    private final CurrentLocation currentLocation = mock(CurrentLocation.class);
    private final CurrentUserSearchRepository currentUserSearchRepository = Mockito.mock(CurrentUserSearchRepository.class);
    private final Application application = Mockito.mock(Application.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private final String DEFAULT_ID = "DEFAULT_ID";
    private final String DEFAULT_ID_2 = "DEFAULT_ID_2";
    private final String DEFAULT_ID_3 = "DEFAULT_ID_3";

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String RESTAURANT_NAME_1 = "PIZZA N PASTA";
    private static final String RESTAURANT_NAME_2 = "PASTA";

    private static final String MY_POSITION = "My Position";

    private static final boolean RESTAURANT_IS_OPEN_TRUE = true;
    private static final boolean RESTAURANT_IS_OPEN_FALSE = false;

    private static final double LATITUDE = -1.256546;
    private static final double LONGITUDE = 8.256546;
    private static final String LOCATION = LATITUDE + "," + LONGITUDE;
    private static final String LOCATION_IF_CURRENT_LOC_NULL = 0.0 + "," + 0.0;

    private static final String EMAIL = "EMAIL";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final int DEFAULT_ICON_RED = R.drawable.ic_custom_google_marker_red;
    private static final int DEFAULT_ICON_BLUE = R.drawable.ic_custom_google_marker_blue;
    private static final int DEFAULT_USER_ICON = R.drawable.custom_google_marker_user;

    private static final String TEXT_TO_HIGHLIGHT = "PIZ";

    //endregion

    //region =============================================================== LIVEDATA ===============================================================

    private final MutableLiveData<CurrentLocation> currentLocationLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<MapMarker>> mapMarkersLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmatesLiveData = new MutableLiveData<>();
    private final MutableLiveData<CharSequence> currentUserSearchLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(LATITUDE).when(currentLocation).getLat();
        doReturn(LONGITUDE).when(currentLocation).getLng();
        doReturn(currentLocationLiveData).when(locationRepository).getLocationMutableLiveData();

        doReturn(workmatesLiveData).when(getWorkmatesHaveChosenTodayUseCaseMock).getWorkmatesHaveChosenTodayLiveData();

        doReturn(placesLiveData).when(placesRepository).getNearbyPlaceResponse(LOCATION);
        doReturn(currentUserSearchLiveData).when(currentUserSearchRepository).getCurrentUserSearchLiveData();
        doReturn(MY_POSITION).when(application).getString(R.string.my_position);

        currentLocationLiveData.setValue(currentLocation);
        placesLiveData.setValue(getDefaultPlaces());
        mapMarkersLiveData.setValue(getDefaultMapMarkers());
        workmatesLiveData.setValue(getDefaultWorkmatesHaveChosen());
        currentUserSearchLiveData.setValue(TEXT_TO_HIGHLIGHT);

        viewModel = new MapViewModel(locationRepository, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository, application);

        verify(locationRepository).getLocationMutableLiveData();
        verify(currentUserSearchRepository).getCurrentUserSearchLiveData();
        verify(getWorkmatesHaveChosenTodayUseCaseMock).getWorkmatesHaveChosenTodayLiveData();
    }

    @Test
    public void get_map_markers_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(4, mapMarkers.size());
        assertEquals(getDefaultMapMarkers(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);
        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_with_search() {
        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(3, mapMarkers.size());
        assertEquals(getDefaultMapMarkersWithSearch(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_with_search_null() {
        // WHEN
        currentUserSearchLiveData.setValue(null);
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(4, mapMarkers.size());
        assertEquals(getDefaultMapMarkers(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_places_null_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        placesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(1, mapMarkers.size());
        assertEquals(getDefaultMapMarkersIfPlacesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_places_null_with_search() {
        // GIVEN
        placesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(1, mapMarkers.size());
        assertEquals(getDefaultMapMarkersIfPlacesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_places_null_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);
        placesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(1, mapMarkers.size());
        assertEquals(getDefaultMapMarkersIfPlacesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_workmates_null_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        workmatesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(4, mapMarkers.size());
        assertEquals(getDefaultMapMarkersWithWorkmatesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_workmates_null_with_search() {
        // GIVEN
        workmatesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(3, mapMarkers.size());
        assertEquals(getDefaultMapMarkersWithWorkmatesNullWithSearch(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_workmates_null_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);
        workmatesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(4, mapMarkers.size());
        assertEquals(getDefaultMapMarkersWithWorkmatesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_places_and_workmates_null_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        placesLiveData.setValue(null);
        workmatesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(1, mapMarkers.size());
        assertEquals(getDefaultMapMarkersIfPlacesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_places_and_workmates_null_with_search() {
        // GIVEN
        placesLiveData.setValue(null);
        workmatesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(1, mapMarkers.size());
        assertEquals(getDefaultMapMarkersIfPlacesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_if_places_and_workmates_null_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);
        placesLiveData.setValue(null);
        workmatesLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(1, mapMarkers.size());
        assertEquals(getDefaultMapMarkersIfPlacesNull(), mapMarkers);

        verify(currentLocation, atLeastOnce()).getLat();
        verify(currentLocation, atLeastOnce()).getLng();
        verify(placesRepository).getNearbyPlaceResponse(LOCATION);
        verify(application, atLeastOnce()).getString(R.string.my_position);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    @Test
    public void get_map_markers_with_current_location_null() {
        // GIVEN
        currentLocationLiveData.setValue(null);

        // WHEN
        List<MapMarker> mapMarkers = TestUtils.getValueForTesting(viewModel.getMapViewStateLiveData());

        assertNotNull(mapMarkers);
        assertEquals(0, mapMarkers.size());

        verify(placesRepository).getNearbyPlaceResponse(LOCATION_IF_CURRENT_LOC_NULL);

        Mockito.verifyNoMoreInteractions(locationRepository, currentLocation, placesRepository, getWorkmatesHaveChosenTodayUseCaseMock, currentUserSearchRepository);
    }

    private List<Place> getDefaultPlaces() {
        List<Place> places = new ArrayList<>();

        places.add(new Place(DEFAULT_ID, RESTAURANT_NAME, "", LATITUDE, LONGITUDE, RESTAURANT_IS_OPEN_TRUE));
        places.add(new Place(DEFAULT_ID_2, RESTAURANT_NAME_1, "", LATITUDE, LONGITUDE, RESTAURANT_IS_OPEN_FALSE));
        places.add(new Place(DEFAULT_ID_3, RESTAURANT_NAME_2, "", LATITUDE, LONGITUDE, RESTAURANT_IS_OPEN_FALSE));

        return places;
    }

    private List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(new Workmate("1", "workmate1", EMAIL, PICTURE_URL, DEFAULT_ID, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("2", "workmate2", EMAIL, PICTURE_URL, DEFAULT_ID_2, RESTAURANT_NAME_1, new ArrayList<>()));
        workmates.add(new Workmate("3", "workmate3", EMAIL, PICTURE_URL, DEFAULT_ID, RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }

    private List<MapMarker> getDefaultMapMarkers() {
        List<MapMarker> mapMarkers = new ArrayList<>();

        mapMarkers.add(new MapMarker(DEFAULT_ID, LATITUDE, LONGITUDE, RESTAURANT_NAME, "", DEFAULT_ICON_BLUE));
        mapMarkers.add(new MapMarker(DEFAULT_ID_2, LATITUDE, LONGITUDE, RESTAURANT_NAME_1, "", DEFAULT_ICON_BLUE));
        mapMarkers.add(new MapMarker(DEFAULT_ID_3, LATITUDE, LONGITUDE, RESTAURANT_NAME_2, "", DEFAULT_ICON_RED));
        mapMarkers.add(new MapMarker("", -1.256546, 8.256546, MY_POSITION, "", DEFAULT_USER_ICON));

        return mapMarkers;
    }

    private List<MapMarker> getDefaultMapMarkersWithWorkmatesNull() {
        List<MapMarker> mapMarkers = new ArrayList<>();

        mapMarkers.add(new MapMarker(DEFAULT_ID, LATITUDE, LONGITUDE, RESTAURANT_NAME, "", DEFAULT_ICON_RED));
        mapMarkers.add(new MapMarker(DEFAULT_ID_2, LATITUDE, LONGITUDE, RESTAURANT_NAME_1, "", DEFAULT_ICON_RED));
        mapMarkers.add(new MapMarker(DEFAULT_ID_3, LATITUDE, LONGITUDE, RESTAURANT_NAME_2, "", DEFAULT_ICON_RED));
        mapMarkers.add(new MapMarker("", -1.256546, 8.256546, MY_POSITION, "", DEFAULT_USER_ICON));

        return mapMarkers;
    }

    private List<MapMarker> getDefaultMapMarkersWithWorkmatesNullWithSearch() {
        List<MapMarker> mapMarkers = new ArrayList<>();

        mapMarkers.add(new MapMarker(DEFAULT_ID, LATITUDE, LONGITUDE, RESTAURANT_NAME, "", DEFAULT_ICON_RED));
        mapMarkers.add(new MapMarker(DEFAULT_ID_2, LATITUDE, LONGITUDE, RESTAURANT_NAME_1, "", DEFAULT_ICON_RED));
        mapMarkers.add(new MapMarker("", -1.256546, 8.256546, MY_POSITION, "", DEFAULT_USER_ICON));

        return mapMarkers;
    }

    private List<MapMarker> getDefaultMapMarkersWithSearch() {
        List<MapMarker> mapMarkers = new ArrayList<>();

        mapMarkers.add(new MapMarker(DEFAULT_ID, LATITUDE, LONGITUDE, RESTAURANT_NAME, "", DEFAULT_ICON_BLUE));
        mapMarkers.add(new MapMarker(DEFAULT_ID_2, LATITUDE, LONGITUDE, RESTAURANT_NAME_1, "", DEFAULT_ICON_BLUE));
        mapMarkers.add(new MapMarker("", -1.256546, 8.256546, MY_POSITION, "", DEFAULT_USER_ICON));

        return mapMarkers;
    }

    private List<MapMarker> getDefaultMapMarkersIfPlacesNull() {
        List<MapMarker> mapMarkers = new ArrayList<>();

        mapMarkers.add(new MapMarker("", -1.256546, 8.256546, MY_POSITION, "", DEFAULT_USER_ICON));

        return mapMarkers;
    }
}