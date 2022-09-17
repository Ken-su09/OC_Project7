package com.suonk.oc_project7.ui.auth;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import com.suonk.oc_project7.domain.workmates.add.AddWorkmateToFirestoreUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthViewModelTest {

    private AuthViewModel authViewModel;

    private final AddWorkmateToFirestoreUseCase addWorkmateToFirestoreUseCaseMock = mock(AddWorkmateToFirestoreUseCase.class);

    @Before
    public void setup() {
        doNothing().when(addWorkmateToFirestoreUseCaseMock).addWorkmateToFirestore();
        authViewModel = new AuthViewModel(addWorkmateToFirestoreUseCaseMock);
    }

    @Test
    public void test_add_workmate_to_firestore() {
        authViewModel.addWorkmateToFirestore();

        verify(addWorkmateToFirestoreUseCaseMock).addWorkmateToFirestore();
        verifyNoMoreInteractions(addWorkmateToFirestoreUseCaseMock);
    }
}