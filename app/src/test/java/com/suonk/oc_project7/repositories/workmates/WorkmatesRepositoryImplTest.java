package com.suonk.oc_project7.repositories.workmates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final FirebaseFirestore firebaseFirestoreMock = mock(FirebaseFirestore.class);
    private final FirebaseUser firebaseUser = mock(FirebaseUser.class);

    private WorkmatesRepository workmatesRepository;

    private final CollectionReference workmatesCollectionReferenceMock = mock(CollectionReference.class);
    private final DocumentReference documentReferenceMock = mock(DocumentReference.class);

    private static final String USER_ID = "USER_ID";
    private static final String PLACE_ID_VALUE = "PLACE_ID_VALUE";
    private static final String PLACE_ID = "PLACE_ID";
    private static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String ADDRESS = "ADDRESS";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;
    private static final int RATING = 4;
    private static final String WEBSITE = "WEBSITE";
    private static final String DISPLAY_NAME = "DISPLAY_NAME";
    private static final boolean IS_LIKED = false;

//    private final ArgumentCaptor<EventListener<DocumentSnapshot>> documentSnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
//    private final ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);

    private final MutableLiveData<List<Workmate>> allWorkmatesFromFirestoreLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmatesHaveChosenTodayLiveData = new MutableLiveData<>();

    @Before
    public void setUp() {
//        doReturn(workmatesCollectionReferenceMock).when(firebaseFirestoreMock).collection("all_workmates");
//        doReturn(documentReferenceMock).when(firebaseFirestoreMock).document(USER_ID);
//
////        when(firebaseFirestoreMock.collection("workmates"));
//
//        allWorkmatesFromFirestoreLiveData.setValue(getAllDefaultWorkmates());
//        workmatesHaveChosenTodayLiveData.setValue(getDefaultWorkmatesHaveChosen());
//
//        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);
//
//        verify(firebaseFirestoreMock).collection("all_workmates");
//        verify(firebaseFirestoreMock).document(USER_ID);
    }

    @Test
    public void get_all_workmates_from_firestore() {
        // WHEN
//        List<Workmate> workmates = TestUtils.getValueForTesting(workmatesRepository.getAllWorkmatesFromFirestoreLiveData());
//
//        assertEquals(workmates, getAllDefaultWorkmates());

        // THEN
//        verify(workmatesRepository).getAllWorkmatesFromFirestoreLiveData();
//        verifyNoMoreInteractions(firebaseFirestoreMock);
    }
//
//    @Test
//    public void add_workmate_to_firestore() {
//        // WHEN
//        workmatesRepository.addWorkmateToFirestore(firebaseUser);
//
//        // THEN
//        verifyNoMoreInteractions(firebaseFirestoreMock);
//    }
//
//    @Test
//    public void add_workmate_to_have_chosen_today_list() {
//        // WHEN
//        workmatesRepository.addWorkmateToHaveChosenTodayList(firebaseUser, PLACE_ID_VALUE);
//
//        // THEN
//        verify(workmatesRepository).getAllWorkmatesFromFirestoreLiveData();
//        verifyNoMoreInteractions(firebaseFirestoreMock);
//    }
//
//    @Test
//    public void getWorkmateByIdFromFirestore() {
//        List<Workmate> workmates = TestUtils.getValueForTesting(workmatesRepository.getAllWorkmatesFromFirestoreLiveData());
//        assertNotNull(workmates);
//    }

    public FirebaseUser getDefaultFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public List<Workmate> getAllDefaultWorkmates() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(new Workmate("1", "workmate1", "mail", "", "", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("2", "workmate2", "mail", "", "", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("3", "workmate3", "mail", "", "", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("4", "workmate4", "mail", "", "", RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }

    public List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(new Workmate("1", "workmate1", "mail", "", "", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("2", "workmate2", "mail", "", "", RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }
}