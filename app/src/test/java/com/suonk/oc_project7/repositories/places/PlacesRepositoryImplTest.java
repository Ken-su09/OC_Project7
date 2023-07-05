package com.suonk.oc_project7.repositories.places;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.suonk.oc_project7.api.PlacesApiService;
import com.suonk.oc_project7.model.data.place_auto_complete.AutocompleteResponse;
import com.suonk.oc_project7.model.data.place_auto_complete.PlaceAutocomplete;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;

import org.junit.Assert;
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
    private final Call<NearbyPlaceResponse> mockedCall = Mockito.mock(Call.class);
    private final Response<NearbyPlaceResponse> mockedResponse = Mockito.mock(Response.class);

    private final Call<AutocompleteResponse> autocompleteResponseCallMock = Mockito.mock(Call.class);
    private final Call<PlaceAutocomplete> mockedCallAutocomplete = Mockito.mock(Call.class);
    private final Callback<AutocompleteResponse> autocompleteResponseCallbackMock = Mockito.mock(Callback.class);
    private final Callback<PlaceAutocomplete> mockedCallbackAutocomplete = Mockito.mock(Callback.class);
    private final Response<PlaceAutocomplete> mockedResponseAutocomplete = Mockito.mock(Response.class);

    //endregion

    private static final double DEFAULT_LATITUDE = 48.9575329;
    private static final double DEFAULT_LONGITUDE = 2.5594583;
    private final String DEFAULT_LOCATION = DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE;

    private final String DEFAULT_INPUT = "DEFAULT_INPUT";

    @Before
    public void setup() throws IOException {
        placesRepository = new PlacesRepositoryImpl(placesApiServiceMock);
    }

    @Test
    public void get_nearby_places_with_default_location() {
        // GIVEN
        doReturn(mockedCall).when(placesApiServiceMock).getNearbyPlacesResponse(DEFAULT_LOCATION);

        ArgumentCaptor<Callback<NearbyPlaceResponse>> nearbyPlaceResponseCallBackCaptor = ArgumentCaptor.forClass(Callback.class);

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
        doReturn(autocompleteResponseCallMock).when(placesApiServiceMock).getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        autocompleteResponseCallMock.enqueue(autocompleteResponseCallbackMock);

        // WHEN
        placesRepository.getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT).observeForever(Assert::assertNotNull);

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_places_autocomplete_returns_null() {
        // GIVEN
        doReturn(null).when(placesApiServiceMock).getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);

        // WHEN
        placesRepository.getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT).observeForever(Assert::assertNotNull);

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getPlacesAutocomplete(DEFAULT_LOCATION, Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_places_autocomplete_with_location_null() {
        // GIVEN
        doReturn(autocompleteResponseCallMock).when(placesApiServiceMock).getPlacesAutocomplete(Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        autocompleteResponseCallMock.enqueue(autocompleteResponseCallbackMock);

        // WHEN
        placesRepository.getPlacesAutocomplete(null, Locale.getDefault().getLanguage(), DEFAULT_INPUT).observeForever(list -> {
            assertNotNull(list);
            assertEquals(0, list.size());
        });

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getPlacesAutocomplete(Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        verifyNoMoreInteractions(placesApiServiceMock);
    }

    @Test
    public void get_places_autocomplete_with_location_null_returns_null() {
        // GIVEN
        doReturn(null).when(placesApiServiceMock).getPlacesAutocomplete(Locale.getDefault().getLanguage(), DEFAULT_INPUT);

        // WHEN
        placesRepository.getPlacesAutocomplete(null, Locale.getDefault().getLanguage(), DEFAULT_INPUT).observeForever(Assert::assertNotNull);

        // THEN
        verify(placesApiServiceMock, atLeastOnce()).getPlacesAutocomplete(Locale.getDefault().getLanguage(), DEFAULT_INPUT);
        verifyNoMoreInteractions(placesApiServiceMock);
    }
}