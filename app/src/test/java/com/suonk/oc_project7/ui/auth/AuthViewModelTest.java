package com.suonk.oc_project7.ui.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepositoryImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthViewModelTest {

    private AuthViewModel authViewModel;

    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepositoryImpl.class);
    private final FirebaseUser firebaseUser = mock(FirebaseUser.class);

    @Before
    public void setup() {
        doNothing().when(workmatesRepositoryMock).addWorkmateToFirestore(firebaseUser);
        authViewModel = new AuthViewModel(workmatesRepositoryMock);
    }

    @Test
    public void test_add_workmate_to_firestore() {
        authViewModel.addWorkmateToFirestore(firebaseUser);

        verify(workmatesRepositoryMock).addWorkmateToFirestore(firebaseUser);
        verifyNoMoreInteractions(workmatesRepositoryMock);
    }
}