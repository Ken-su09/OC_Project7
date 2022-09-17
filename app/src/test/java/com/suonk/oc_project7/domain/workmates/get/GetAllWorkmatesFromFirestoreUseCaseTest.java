package com.suonk.oc_project7.domain.workmates.get;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GetAllWorkmatesFromFirestoreUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GetAllWorkmatesFromFirestoreUseCase getAllWorkmatesFromFirestoreUseCase;

    //region ============================================= MOCK =============================================

    @NonNull
    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String RESTAURANT_NAME_1 = "PIZZA N PASTA";
    private static final String RESTAURANT_NAME_2 = "PASTA";

    private static final int TEXT_COLOR_HAS_DECIDED = -16777216;
    private static final int TEXT_STYLE_HAS_DECIDED = 0;
    private static final int TEXT_COLOR_HAS_NOT_DECIDED = -7829368;
    private static final int TEXT_STYLE_HAS_NOT_DECIDED = 2;
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String UID = "UID";
    private static final String U_NAME = "U_NAME";
    private static final String EMAIL = "EMAIL";

    private static final String TEXT_TO_HIGHLIGHT = "PIZ";

    private static final String WORKMATE_HAS_CHOSEN = "WORKMATE_HAS_CHOSEN";
    private static final String WORKMATE_HAS_NOT_CHOSEN_YET = "WORKMATE_HAS_NOT_CHOSEN_YET";

    private static final String TEXT_WORKMATE_HAS_CHOSEN = WORKMATE_HAS_CHOSEN + " has chosen " + RESTAURANT_NAME;
    private static final String TEXT_WORKMATE_HAS_CHOSEN_1 = WORKMATE_HAS_CHOSEN + " has chosen " + RESTAURANT_NAME_1;
    private static final String TEXT_WORKMATE_HAS_CHOSEN_2 = WORKMATE_HAS_CHOSEN + " has chosen " + RESTAURANT_NAME_2;

    private static final String TEXT_WORKMATE_HAS_NOT_CHOSEN_YET = " has not chosen yet";

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(workmatesMutableLiveData).when(workmatesRepositoryMock).getAllWorkmatesFromFirestoreLiveData();

        workmatesMutableLiveData.setValue(getDefaultAllWorkmates());

        getAllWorkmatesFromFirestoreUseCase = new GetAllWorkmatesFromFirestoreUseCase(workmatesRepositoryMock);
    }

    @Test
    public void get_all_workmates_from_firestore() {
        // WHEN
        List<Workmate> workmatesHaveChosenToday = TestUtils.getValueForTesting(
                getAllWorkmatesFromFirestoreUseCase.getAllWorkmatesFromFirestoreLiveData());

        assertNotNull(workmatesHaveChosenToday);
        assertEquals(5, workmatesHaveChosenToday.size());
        assertEquals(getDefaultAllWorkmates(), workmatesHaveChosenToday);

        verify(workmatesRepositoryMock).getAllWorkmatesFromFirestoreLiveData();
        verifyNoMoreInteractions(workmatesRepositoryMock);
    }

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultCurrentUser() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>());
    }

    //endregion

    private List<Workmate> getDefaultAllWorkmates() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultCurrentUser());
        workmates.add(new Workmate("2", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("3", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("4", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_1, new ArrayList<>()));
        workmates.add(new Workmate("5", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_2, new ArrayList<>()));

        return workmates;
    }
}