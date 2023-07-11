package com.suonk.oc_project7.domain.workmates.remove;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class RemoveWorkmateToHaveChosenTodayUseCaseTest {

    private RemoveWorkmateToHaveChosenTodayUseCase removeWorkmateToHaveChosenTodayUseCase;

    @NonNull
    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepository.class);

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String RESTAURANT_NAME_ID = "RESTAURANT_NAME_ID";
    private static final String RESTAURANT_NAME = "RESTAURANT_NAME";

    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";

    private static final String DEFAULT_ID = "DEFAULT_ID";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_MAIL = "DEFAULT_MAIL";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    //endregion

    @Before
    public void setup() {
        // GIVEN
        doNothing().when(workmatesRepositoryMock).removeWorkmateToHaveChosenTodayList(DEFAULT_ID);

        removeWorkmateToHaveChosenTodayUseCase = new RemoveWorkmateToHaveChosenTodayUseCase(workmatesRepositoryMock);
    }

    @Test
    public void test_add_workmate_to_have_chosen_today() {
        // WHEN
        removeWorkmateToHaveChosenTodayUseCase.invoke(getDefaultWorkmateToRemoveToHaveChosenToday());

        // THEN
        verify(workmatesRepositoryMock).removeWorkmateToHaveChosenTodayList(DEFAULT_ID);
        verifyNoMoreInteractions(workmatesRepositoryMock);
    }

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultWorkmateToRemoveToHaveChosenToday() {
        return new Workmate(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_MAIL,
                PICTURE_URL,
                RESTAURANT_NAME_ID,
                RESTAURANT_NAME,
                new ArrayList<>());
    }

    //endregion
}