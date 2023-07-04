package com.suonk.oc_project7.repositories.places;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.place_auto_complete.PlaceAutocomplete;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class PlacesRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private PlacesRepository placesRepository;

    //region ============================================= MOCK =============================================

    @NonNull
    private final PlacesApiService placesApiServiceMock = mock(PlacesApiService.class);

    @NonNull
    private final Location locationMock = mock(Location.class);
    private final Call<NearbyPlaceResponse> mockedCall = Mockito.mock(Call.class);
    private final Response<NearbyPlaceResponse> mockedResponse = Mockito.mock(Response.class);

    private final Call<PlaceAutocomplete> mockedCallAutocomplete = Mockito.mock(Call.class);
    private final Response<PlaceAutocomplete> mockedResponseAutocomplete = Mockito.mock(Response.class);

    //endregion

    private static final double DEFAULT_LATITUDE = 48.9575329;
    private static final double DEFAULT_LONGITUDE = 2.5594583;
    private String DEFAULT_LOCATION = DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE;

    private final String DEFAULT_INPUT = "DEFAULT_INPUT";

    @Before
    public void setup() throws IOException {
        doReturn(DEFAULT_LATITUDE).when(locationMock).getLatitude();
        doReturn(DEFAULT_LONGITUDE).when(locationMock).getLongitude();

        DEFAULT_LOCATION = locationMock.getLatitude() + "," + locationMock.getLongitude();

        placesRepository = new PlacesRepositoryImpl(placesApiServiceMock);
    }

    @Test
    public void get_nearby_places_with_default_location() {
        // GIVEN
        doReturn(mockedCall).when(placesApiServiceMock).getNearbyPlacesResponse(DEFAULT_LOCATION);

        ArgumentCaptor<Callback<NearbyPlaceResponse>> nearbyPlaceResponseCallBackCaptor =
                ArgumentCaptor.forClass(Callback.class);

        // WHEN
        placesRepository.getNearbyPlaceResponse(DEFAULT_LOCATION);

        // THEN
        verify(mockedCall).enqueue(nearbyPlaceResponseCallBackCaptor.capture());
        Callback<NearbyPlaceResponse> callback = nearbyPlaceResponseCallBackCaptor.getValue();

        // WHEN II
        callback.onResponse(mockedCall, mockedResponse);

        // THEN
        verify(placesApiServiceMock).getNearbyPlacesResponse(DEFAULT_LOCATION);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_places_autocomplete() {
        // GIVEN
        doReturn(mockedCallAutocomplete).when(placesApiServiceMock)
                .getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);

        ArgumentCaptor<Callback<PlaceAutocomplete>> placesAutoCompleteCallBackCaptor = ArgumentCaptor.forClass(Callback.class);

        // WHEN
        placesRepository.getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);

        // THEN
        verify(mockedCallAutocomplete).enqueue(placesAutoCompleteCallBackCaptor.capture());
        Callback<PlaceAutocomplete> callback = placesAutoCompleteCallBackCaptor.getValue();

        // WHEN II
        callback.onResponse(mockedCallAutocomplete, mockedResponseAutocomplete);

        // THEN
        verify(placesApiServiceMock, atLeastOnce())
                .getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_places_autocomplete_with_get_places_null() {
        // WHEN
        placesRepository.getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);

        // THEN
        verify(placesApiServiceMock).getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        verifyNoMoreInteractions(placesApiServiceMock);
    }
}