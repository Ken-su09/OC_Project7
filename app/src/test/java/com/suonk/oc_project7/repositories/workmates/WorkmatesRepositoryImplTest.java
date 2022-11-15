package com.suonk.oc_project7.repositories.workmates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class WorkmatesRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private WorkmatesRepository workmatesRepository;

    //region ============================================= MOCK =============================================

    private final FirebaseFirestore firebaseFirestoreMock = mock(FirebaseFirestore.class);

    private final CollectionReference collectionReferenceMock = mock(CollectionReference.class);
    private final DocumentReference documentReferenceMock = mock(DocumentReference.class);
    private final Task<Void> taskVoidMock = mock(Task.class);
    private final Task<QuerySnapshot> taskQuerySnapshotMock = mock(Task.class);
    private final Task<DocumentSnapshot> taskDocumentSnapshotMock = mock(Task.class);

    private final QuerySnapshot querySnapshotMock = mock(QuerySnapshot.class);
    private final DocumentSnapshot documentSnapshotMock = mock(DocumentSnapshot.class);
    private final ListenerRegistration listenerRegistrationMock = mock(ListenerRegistration.class);
    private final FirebaseFirestoreException firebaseFirestoreExceptionMock = mock(FirebaseFirestoreException.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String PLACE_ID_VALUE = "PLACE_ID_VALUE";
    private static final String PLACE_ID_VALUE_NO_PICTURE = "PLACE_ID_VALUE_NO_PICTURE";

    private static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;
    private static final String DISPLAY_NAME = "DISPLAY_NAME";

    private static final String CURRENT_FIREBASE_USER_ID = "CURRENT_FIREBASE_USER_ID";
    private static final String CURRENT_FIREBASE_USER_NAME = "CURRENT_FIREBASE_USER_NAME";
    private static final String CURRENT_FIREBASE_MAIL = "CURRENT_FIREBASE_MAIL";
    private static final String CURRENT_FIREBASE_PHOTO_URL = "CURRENT_FIREBASE_PHOTO_URL";

    private static final List<String> DEFAULT_LIKED_RESTAURANTS = Arrays.asList(
            PLACE_ID_VALUE,
            PLACE_ID_VALUE_NO_PICTURE);

    private static final String ALL_WORKMATES = "all_workmates";
    private static final String HAVE_CHOSEN_TODAY = "have_chosen_today";

    private final LocalDate dateToday = LocalDate.now();
    private final int year = dateToday.getYear();
    private final int month = dateToday.getMonthValue();
    private final int day = dateToday.getDayOfMonth();
    private final String today = year + "-" + month + "-" + day;
    private final String HAVE_CHOSEN_TODAY_COLLECTION_PATH = HAVE_CHOSEN_TODAY + "_" + today;

    //endregion

    //region ========================================= GET WORKMATES ========================================

    @Test
    public void get_all_workmates_from_firestore_live_data() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(getDefaultAllWorkmates()).when(querySnapshotMock).toObjects(Workmate.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<List<Workmate>> livedata = workmatesRepository.getAllWorkmatesFromFirestoreLiveData();
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultAllWorkmates(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    @Test
    public void get_workmates_have_chosen_today_live_data() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(getDefaultAllWorkmates()).when(querySnapshotMock).toObjects(Workmate.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<List<Workmate>> livedata = workmatesRepository.getWorkmatesHaveChosenTodayLiveData();
        livedata.observeForever(t -> {
            List<Workmate> captured = livedata.getValue();
            assertNotNull(captured);
        });

        //TODO

        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }


    @Test
    public void get_workmates_that_have_chosen_this_restaurant() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(taskQuerySnapshotMock).when(collectionReferenceMock).get();
        doReturn(querySnapshotMock).when(taskQuerySnapshotMock).getResult();

        doReturn(getDefaultAllWorkmates()).when(querySnapshotMock).toObjects(Workmate.class);

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        List<Workmate> workmates = workmatesRepository.getAllWorkmatesThatHaveChosenToday();

        // THEN
        assertNotNull(workmates);
        assertEquals(getDefaultAllWorkmates(), workmates);

        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).get();
        verify(taskQuerySnapshotMock).getResult();
        verify(querySnapshotMock).toObjects(Workmate.class);
    }

    //endregion

    //region =========================================== GET USER ===========================================

    @Test
    public void get_current_user_by_id_live_data() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);

        ArgumentCaptor<EventListener<DocumentSnapshot>> documentSnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(getDefaultCurrentUser()).when(documentSnapshotMock).toObject(Workmate.class);

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<Workmate> livedata = workmatesRepository.getWorkmateByIdLiveData(CURRENT_FIREBASE_USER_ID);

        // THEN
        verify(documentReferenceMock).addSnapshotListener(documentSnapshotListenerCaptor.capture());
        documentSnapshotListenerCaptor.getValue().onEvent(documentSnapshotMock, firebaseFirestoreExceptionMock);
        verify(documentSnapshotMock).toObject(Workmate.class);

        Workmate defaultWorkmate = TestUtils.getValueForTesting(livedata);
        assertNotNull(defaultWorkmate);
        assertEquals(getDefaultCurrentUser(), defaultWorkmate);

        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
    }

    @Test
    public void get_current_user_by_id_live_data_with_snapshot_document_return_null() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);

        ArgumentCaptor<EventListener<DocumentSnapshot>> documentSnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(null).when(documentSnapshotMock).toObject(Workmate.class);

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<Workmate> livedata = workmatesRepository.getWorkmateByIdLiveData(CURRENT_FIREBASE_USER_ID);

        // THEN
        verify(documentReferenceMock).addSnapshotListener(documentSnapshotListenerCaptor.capture());
        documentSnapshotListenerCaptor.getValue().onEvent(documentSnapshotMock, firebaseFirestoreExceptionMock);
        verify(documentSnapshotMock).toObject(Workmate.class);

        livedata.observeForever(t -> {
        });
        Workmate captured = livedata.getValue();
        assertNull(captured);

        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
    }

    @Test
    public void get_user_by_id_from_firestore() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskDocumentSnapshotMock).when(documentReferenceMock).get();
        doReturn(documentSnapshotMock).when(taskDocumentSnapshotMock).getResult();
        doReturn(getDefaultCurrentUser()).when(documentSnapshotMock).toObject(Workmate.class);

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        Workmate currentUser = workmatesRepository.getUserByIdFromFirestore(CURRENT_FIREBASE_USER_ID);
        assertNotNull(currentUser);
        assertEquals(getDefaultCurrentUser(), currentUser);

        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).get();
        verify(taskDocumentSnapshotMock).getResult();
        verify(documentSnapshotMock, atLeastOnce()).toObject(Workmate.class);
    }

    @Test
    public void get_user_by_id_from_firestore_with_document_snapshot_null() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskDocumentSnapshotMock).when(documentReferenceMock).get();

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        Workmate currentUser = workmatesRepository.getUserByIdFromFirestore(CURRENT_FIREBASE_USER_ID);
        assertNotNull(currentUser);
        assertEquals(getDefaultCurrentUserEmpty(), currentUser);

        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).get();
    }

    @Test
    public void get_user_by_id_from_firestore_with_document_snapshot_to_object_null() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskDocumentSnapshotMock).when(documentReferenceMock).get();
        doReturn(documentSnapshotMock).when(taskDocumentSnapshotMock).getResult();
        doReturn(null).when(documentSnapshotMock).toObject(Workmate.class);

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        Workmate currentUser = workmatesRepository.getUserByIdFromFirestore(CURRENT_FIREBASE_USER_ID);
        assertNotNull(currentUser);
        assertEquals(getDefaultCurrentUserEmpty(), currentUser);

        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).get();
        verify(taskDocumentSnapshotMock).getResult();
        verify(documentSnapshotMock, atLeastOnce()).toObject(Workmate.class);
    }

    //endregion

    //region ========================================= ADD WORKMATE =========================================

    @Test
    public void add_workmate_to_have_chosen_today() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskVoidMock).when(documentReferenceMock).set(getDefaultCurrentUser());

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        workmatesRepository.addWorkmateToHaveChosenTodayList(CURRENT_FIREBASE_USER_ID, getDefaultCurrentUser());

        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
    }

    @Test
    public void add_workmate_to_firestore() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskVoidMock).when(documentReferenceMock).set(getDefaultCurrentUser());

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        workmatesRepository.addWorkmateToFirestore(CURRENT_FIREBASE_USER_ID, getDefaultCurrentUser());

        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).set(getDefaultCurrentUser());
    }

    //endregion

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultCurrentUser() {
        return new Workmate(
                CURRENT_FIREBASE_USER_ID,
                CURRENT_FIREBASE_USER_NAME,
                CURRENT_FIREBASE_MAIL,
                CURRENT_FIREBASE_PHOTO_URL,
                PLACE_ID_VALUE,
                RESTAURANT_NAME,
                DEFAULT_LIKED_RESTAURANTS
        );
    }

    private Workmate getDefaultCurrentUserEmpty() {
        return new Workmate(
                "",
                "",
                "",
                "",
                "",
                "",
                new ArrayList<>()
        );
    }

    //endregion

    private List<Workmate> getDefaultAllWorkmates() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultCurrentUser());
        workmates.add(new Workmate("2", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("3", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("4", DISPLAY_NAME, "mail", PICTURE_URL, PLACE_ID_VALUE_NO_PICTURE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("5", DISPLAY_NAME, "mail", PICTURE_URL, "", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("6", DISPLAY_NAME, "mail", PICTURE_URL, "", RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }
}