package com.suonk.oc_project7.domain.workmates.add;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class AddWorkmateToFirestoreUseCaseTest {

    private AddWorkmateToFirestoreUseCase addWorkmateToFirestoreUseCase;

    @NonNull
    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepository.class);

    @NonNull
    private final UserRepository userRepositoryMock = mock(UserRepository.class);

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";

    private static final String DEFAULT_ID = "DEFAULT_ID";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_MAIL = "DEFAULT_MAIL";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;


    //endregion

    @Test
    public void test_add_workmate_to_firestore() {
        // GIVEN
        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();
        doNothing().when(workmatesRepositoryMock).addWorkmateToFirestore(DEFAULT_ID, getDefaultWorkmateToAdd());

        addWorkmateToFirestoreUseCase = new AddWorkmateToFirestoreUseCase(workmatesRepositoryMock, userRepositoryMock);

        // WHEN
        addWorkmateToFirestoreUseCase.addWorkmateToFirestore();

        // THEN
        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
        verify(workmatesRepositoryMock).addWorkmateToFirestore(DEFAULT_ID, getDefaultWorkmateToAdd());
        verifyNoMoreInteractions(workmatesRepositoryMock, userRepositoryMock);
    }

    @Test
    public void test_add_workmate_to_firestore_with_null_custom_firebase_user() {
        // GIVEN
        doReturn(null).when(userRepositoryMock).getCustomFirebaseUser();

        addWorkmateToFirestoreUseCase = new AddWorkmateToFirestoreUseCase(workmatesRepositoryMock, userRepositoryMock);

        // WHEN
        addWorkmateToFirestoreUseCase.addWorkmateToFirestore();

        // THEN
        verify(userRepositoryMock).getCustomFirebaseUser();
        verifyNoMoreInteractions(workmatesRepositoryMock, userRepositoryMock);
    }

//region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultWorkmateToAdd() {
        return new Workmate(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_MAIL,
                PICTURE_URL,
                "",
                "", new ArrayList<>());
    }

    private CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_MAIL,
                PICTURE_URL
        );
    }

    //endregion
}