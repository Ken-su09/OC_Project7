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

@RunWith(MockitoJUnitRunner.class)
public class GetWorkmateByIdUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GetWorkmateByIdUseCase getWorkmateByIdUseCase;

    //region ============================================= MOCK =============================================

    @NonNull
    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;
    private static final String UID = "UID";
    private static final String U_NAME = "U_NAME";
    private static final String EMAIL = "EMAIL";

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<Workmate> currentWorkmateLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(currentWorkmateLiveData).when(workmatesRepositoryMock).getWorkmateByIdLiveData(UID);

        currentWorkmateLiveData.setValue(getDefaultCurrentUser());

        getWorkmateByIdUseCase = new GetWorkmateByIdUseCase(workmatesRepositoryMock);
    }

    @Test
    public void get_workmates_have_chosen_today_livedata() {
        // WHEN
        Workmate currentWorkmate = TestUtils.getValueForTesting(
                getWorkmateByIdUseCase.getWorkmateByIdLiveData(UID));

        assertNotNull(currentWorkmate);
        assertEquals(getDefaultCurrentUser(), currentWorkmate);

        verify(workmatesRepositoryMock).getWorkmateByIdLiveData(UID);
    }

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultCurrentUser() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>());
    }

    //endregion
}