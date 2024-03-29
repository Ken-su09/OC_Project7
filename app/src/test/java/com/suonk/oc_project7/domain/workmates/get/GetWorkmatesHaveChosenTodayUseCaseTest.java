package com.suonk.oc_project7.domain.workmates.get;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetWorkmatesHaveChosenTodayUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCase;

    //region ============================================= MOCK =============================================

    @NonNull
    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String RESTAURANT_NAME_1 = "PIZZA N PASTA";
    private static final String RESTAURANT_NAME_2 = "PASTA";

    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String UID = "UID";
    private static final String U_NAME = "U_NAME";
    private static final String EMAIL = "EMAIL";

    private static final String WORKMATE_HAS_CHOSEN = "WORKMATE_HAS_CHOSEN";

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(workmatesMutableLiveData).when(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();

        workmatesMutableLiveData.setValue(getDefaultWorkmatesHaveChosen());

        getWorkmatesHaveChosenTodayUseCase = new GetWorkmatesHaveChosenTodayUseCase(workmatesRepositoryMock);
    }

    @Test
    public void get_workmates_have_chosen_today_livedata() {
        // WHEN
        List<Workmate> workmatesHaveChosenToday = TestUtils.getValueForTesting(
                getWorkmatesHaveChosenTodayUseCase.getWorkmatesHaveChosenTodayLiveData());

        assertNotNull(workmatesHaveChosenToday);
        assertEquals(5, workmatesHaveChosenToday.size());
        assertEquals(getDefaultWorkmatesHaveChosen(), workmatesHaveChosenToday);

        verify(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();
    }

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultCurrentUser() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>());
    }

    //endregion

    private List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultCurrentUser());
        workmates.add(new Workmate("2", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("3", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("4", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_1, new ArrayList<>()));
        workmates.add(new Workmate("5", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_2, new ArrayList<>()));

        return workmates;
    }
}