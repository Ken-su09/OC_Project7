package com.suonk.oc_project7.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.places.CurrentLocation;
import com.suonk.oc_project7.model.data.places.Geometry;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResponse;
import com.suonk.oc_project7.model.data.places.NearbyPlaceResult;
import com.suonk.oc_project7.model.data.places.OpeningHours;
import com.suonk.oc_project7.model.data.places.Photo;
import com.suonk.oc_project7.model.data.places.PlusCode;
import com.suonk.oc_project7.model.data.places.Viewport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;

public class PlacesServiceTest {

    public MockWebServer mockWebServer;
    public PlacesApiService api;

    private static final double DEFAULT_LATITUDE = 48.9575329;
    private static final double DEFAULT_LONGITUDE = 2.5594583;

    @Before
    public void setup() {
        mockWebServer = new MockWebServer();
        api = PlacesApiHolder.getInstance(mockWebServer.url("/"));
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void get_nearby_places_with_default_location_with_mock_web() throws IOException {
        // GIVEN
        String body = getResourceContent(this, "NearbyPlacesNominalCase.json");
        MockResponse responseMock = new MockResponse().setResponseCode(200).setBody(body);
        mockWebServer.enqueue(responseMock);
        String DEFAULT_LOCATION = DEFAULT_LATITUDE + "," + DEFAULT_LONGITUDE;

        // WHEN
        Response<NearbyPlaceResponse> response = api.getNearbyPlacesResponse(DEFAULT_LOCATION).execute();

        // THEN
        assertNotNull(response.body());

        List<NearbyPlaceResult> results = response.body().getResults();

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(getDefaultNearbyResults(), results);
    }

    @NonNull
    public static String getResourceContent(Object testClass, @NonNull String uri) throws IOException {
        @SuppressWarnings("ConstantConditions")
        InputStream inputStream = testClass.getClass().getClassLoader().getResourceAsStream(uri);

        if (inputStream != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                inputStream.close();
            }
            return writer.toString();
        } else {
            throw new IllegalStateException("No resource found for uri = " + uri);
        }
    }

    private List<NearbyPlaceResult> getDefaultNearbyResults() {
        return new ArrayList<NearbyPlaceResult>() {{
            add(new NearbyPlaceResult(
                            "OPERATIONAL",
                            new Geometry(
                                    new CurrentLocation(
                                            48.9621778, 2.5594583
                                    ), new Viewport(
                                    null,
                                    null
                            )),
                            "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
                            "#FF9E67",
                            "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
                            "Blue Thaï",
                            new OpeningHours(false),
                            null,
                            new ArrayList<Photo>() {
                                {
                                    add(new Photo(
                                            2268,
                                            null,
                                            "AcYSjRi8j1Kt2yhvxdx7_jT3M_sm_D7fFvaNGL3KYTX8m7B9ItpRyqrtMZuwGKIIx0wntQTU3xUTLW5Fw52Qe6OGKNb6ttVlwbvWV9adjUr-81l4ZQtdAOvCqUiukHKCz8n039cQRaHAwCSg6qLvzPGuSS5BJsW1AMFv31Wg4b9LA4FmYRYs",
                                            4032
                                    ));
                                }
                            },
                            "ChIJMTNUKZ0W5kcR-54FypemmuY",
                            new PlusCode("XH65+VQ Tremblay-en-France, France", "8FW4XH65+VQ"),
                            2,
                            4.4,
                            "ChIJMTNUKZ0W5kcR-54FypemmuY",
                            "GOOGLE",
                            new ArrayList<>(Arrays.asList("restaurant",
                                    "food",
                                    "point_of_interest",
                                    "establishment")),
                            654,
                            "2 Route des Petits Ponts, Tremblay-en-France"
                    )
            );

            add(new NearbyPlaceResult(
                            "OPERATIONAL",
                            new Geometry(
                                    new CurrentLocation(
                                            48.9587933, 2.5726341
                                    ), new Viewport(
                                    null,
                                    null
                            )),
                            "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/generic_business-71.png",
                            "#7B9EB0",
                            "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
                            "AL RISTORANTE TRATTORIA",
                            new OpeningHours(false),
                            null,
                            new ArrayList<Photo>() {
                                {
                                    add(new Photo(
                                            494,
                                            null,
                                            "AcYSjRicu6x70o2O4V7xUqjJHjzBkwRFPrhsLoZCZcdTaHiIG3g8vwHXv7wASPpDJaWEHKoa_PLsIJLoIXs7OTJZI_vXr9-V3_Htsr_Q88W5qTMiv4_57HHza8UfMYcL_15h4ZqZ3pQMK6Z51eSFza4ZaxpghnSn6pePioknQVHEeMx_PHH3",
                                            878
                                    ));
                                }
                            },
                            "ChIJvd5caJEW5kcR1AM14z1RiJ0",
                            new PlusCode("XH5F+G3 Tremblay-en-France, France", "8FW4XH5F+G3"),
                            0,
                            4.2,
                            "ChIJvd5caJEW5kcR1AM14z1RiJ0",
                            "GOOGLE",
                            new ArrayList<>(Arrays.asList("meal_delivery",
                                    "meal_takeaway",
                                    "restaurant",
                                    "food",
                                    "point_of_interest",
                                    "establishment")),
                            188,
                            "90 Avenue Henri Barbusse, Tremblay-en-France"
                    )
            );

        }};
    }
}