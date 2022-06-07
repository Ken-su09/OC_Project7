package com.suonk.oc_project7.repositories.workmates;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestoreMock = mock(FirebaseFirestore.class);

    private WorkmatesRepository workmatesRepository;

    private final CollectionReference workmatesCollectionReferenceMock = mock(CollectionReference.class);
    private final DocumentReference documentReferenceMock = mock(DocumentReference.class);

    @Before
    public void setUp() {
        doReturn(workmatesCollectionReferenceMock).when(firebaseFirestoreMock).collection("today_choice");
        when(firebaseFirestoreMock.collection("workmates"));
        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);
    }

    @Test
    public void getAllWorkmatesFromFirestore() {
        // WHEN
        workmatesRepository.getAllWorkmatesFromFirestore();

        // THEN
        verify(workmatesRepository).getAllWorkmatesFromFirestore();
        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void getWorkmateByIdFromFirestore() {
        List<Workmate> workmates = TestUtils.getValueForTesting(workmatesRepository.getAllWorkmatesFromFirestore());
        assertNotNull(workmates);
    }
}