package com.suonk.oc_project7.repositories.workmates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
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
    private final Query queryMock = mock(Query.class);
    private final Query queryMock2 = mock(Query.class);
    private final DocumentReference documentReferenceMock = mock(DocumentReference.class);
    private final Task<Void> taskVoidMock = mock(Task.class);

    private final Task<DocumentSnapshot> taskDocumentSnapshotMock = mock(Task.class);
    private final DocumentSnapshot documentSnapshotMock = mock(DocumentSnapshot.class);

    private final Task<QuerySnapshot> taskQuerySnapshotMock = mock(Task.class);
    private final QuerySnapshot querySnapshotMock = mock(QuerySnapshot.class);
    private final ListenerRegistration listenerRegistrationMock = mock(ListenerRegistration.class);
    private final FirebaseFirestoreException firebaseFirestoreExceptionMock = mock(FirebaseFirestoreException.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String PLACE_ID_VALUE = "PLACE_ID_VALUE";
    private static final String PLACE_ID_VALUE_NO_PICTURE = "PLACE_ID_VALUE_NO_PICTURE";

    private static final String RESTAURANT_NAME = "RESTAURANT_NAME";
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String CURRENT_FIREBASE_USER_ID = "CURRENT_FIREBASE_USER_ID";
    private static final String CURRENT_FIREBASE_USER_NAME = "CURRENT_FIREBASE_USER_NAME";
    private static final String CURRENT_FIREBASE_MAIL = "CURRENT_FIREBASE_MAIL";
    private static final String CURRENT_FIREBASE_PHOTO_URL = "CURRENT_FIREBASE_PHOTO_URL";

    private static final String USER_ID_TO_ADD = "USER_ID_TO_ADD";
    private static final String USER_NAME_TO_ADD = "USER_NAME_TO_ADD";
    private static final String MAIL_TO_ADD = "MAIL_TO_ADD";
    private static final String PICTURE_URL_TO_ADD = "PICTURE_URL_TO_ADD";

    private static final List<String> DEFAULT_LIKED_RESTAURANTS = Arrays.asList(PLACE_ID_VALUE, PLACE_ID_VALUE_NO_PICTURE);

    private static final String ALL_WORKMATES = "all_workmates";
    private static final String HAVE_CHOSEN_TODAY = "have_chosen_today";

    private final LocalDate dateToday = LocalDate.now();
    private final int year = dateToday.getYear();
    private final int month = dateToday.getMonthValue();
    private final int day = dateToday.getDayOfMonth();
    private final String today = year + "-" + month + "-" + day;
    private final String HAVE_CHOSEN_TODAY_COLLECTION_PATH = HAVE_CHOSEN_TODAY + "_" + today;

    private static final String WORKMATE_ID_2 = "WORKMATE_ID_2";
    private static final String WORKMATE_NAME_2 = "WORKMATE_NAME_2";

    private static final String WORKMATE_ID_3 = "WORKMATE_ID_3";
    private static final String WORKMATE_NAME_3 = "WORKMATE_NAME_3";

    private static final String WORKMATE_ID_4 = "WORKMATE_ID_4";
    private static final String WORKMATE_NAME_4 = "WORKMATE_NAME_4";

    private static final String WORKMATE_ID_5 = "WORKMATE_ID_5";
    private static final String WORKMATE_NAME_5 = "WORKMATE_NAME_5";

    private static final String WORKMATE_ID_6 = "WORKMATE_ID_6";
    private static final String WORKMATE_NAME_6 = "WORKMATE_NAME_6";

    private static final String NAME_PATH = "name";
    private static final String EMAIL_PATH = "email";
    private static final String PICTURE_URL_PATH = "pictureUrl";

    //endregion

    @Before
    public void setup() {
        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);
    }

    //region ============================================================= GET WORKMATES ============================================================

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
    public void get_workmates_list_task_that_have_chosen_with_is_successful_returns_false() throws Exception {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(taskQuerySnapshotMock).when(collectionReferenceMock).get();
        doReturn(false).when(taskQuerySnapshotMock).isSuccessful();

        // WHEN
        workmatesRepository.getAllWorkmatesThatHaveChosenToday();

        // THEN
        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).get();

        ArgumentCaptor<Continuation<QuerySnapshot, List<Workmate>>> continuationArgumentCaptor = ArgumentCaptor.forClass(Continuation.class);
        verify(taskQuerySnapshotMock).continueWith(continuationArgumentCaptor.capture());

        Continuation<QuerySnapshot, List<Workmate>> continuation = continuationArgumentCaptor.getValue();
        List<Workmate> workmates = continuation.then(taskQuerySnapshotMock);

        assertEquals(getDefaultEmptyWorkmates(), workmates);

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void get_workmates_list_task_that_have_chosen_with_is_successful_returns_true_but_query_snapshot_empty_returns_true() throws Exception {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(taskQuerySnapshotMock).when(collectionReferenceMock).get();
        doReturn(true).when(taskQuerySnapshotMock).isSuccessful();

        doReturn(querySnapshotMock).when(taskQuerySnapshotMock).getResult();
        doReturn(true).when(querySnapshotMock).isEmpty();

        // WHEN
        workmatesRepository.getAllWorkmatesThatHaveChosenToday();

        // THEN
        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).get();

        ArgumentCaptor<Continuation<QuerySnapshot, List<Workmate>>> continuationArgumentCaptor = ArgumentCaptor.forClass(Continuation.class);
        verify(taskQuerySnapshotMock).continueWith(continuationArgumentCaptor.capture());

        Continuation<QuerySnapshot, List<Workmate>> continuation = continuationArgumentCaptor.getValue();
        List<Workmate> workmates = continuation.then(taskQuerySnapshotMock);

        assertEquals(getDefaultEmptyWorkmates(), workmates);

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void get_workmates_list_task_that_have_chosen() throws Exception {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(taskQuerySnapshotMock).when(collectionReferenceMock).get();
        doReturn(true).when(taskQuerySnapshotMock).isSuccessful();

        doReturn(querySnapshotMock).when(taskQuerySnapshotMock).getResult();
        doReturn(false).when(querySnapshotMock).isEmpty();
        doReturn(getDefaultAllWorkmates()).when(querySnapshotMock).toObjects(Workmate.class);

        // WHEN
        workmatesRepository.getAllWorkmatesThatHaveChosenToday();

        // THEN
        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).get();

        ArgumentCaptor<Continuation<QuerySnapshot, List<Workmate>>> continuationArgumentCaptor = ArgumentCaptor.forClass(Continuation.class);
        verify(taskQuerySnapshotMock).continueWith(continuationArgumentCaptor.capture());

        Continuation<QuerySnapshot, List<Workmate>> continuation = continuationArgumentCaptor.getValue();
        List<Workmate> workmates = continuation.then(taskQuerySnapshotMock);

        assertEquals(getDefaultAllWorkmates(), workmates);

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    //endregion

    //region =============================================================== GET USER ===============================================================

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

        verifyNoMoreInteractions(firebaseFirestoreMock);
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

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void get_current_user_by_id_task_from_firestore_with_task_is_successful_returns_false() throws Exception {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskDocumentSnapshotMock).when(documentReferenceMock).get();
        doReturn(false).when(taskDocumentSnapshotMock).isSuccessful();

        // WHEN
        workmatesRepository.getCurrentUserWhoHasChosenTodayFromFirestore(CURRENT_FIREBASE_USER_ID);

        // THEN
        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).get();

        ArgumentCaptor<Continuation<DocumentSnapshot, Workmate>> continuationArgumentCaptor = ArgumentCaptor.forClass(Continuation.class);
        verify(taskDocumentSnapshotMock).continueWith(continuationArgumentCaptor.capture());

        Continuation<DocumentSnapshot, Workmate> continuation = continuationArgumentCaptor.getValue();
        Workmate workmate = continuation.then(taskDocumentSnapshotMock);

        assertEquals(getDefaultCurrentUserEmpty(), workmate);

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void get_current_user_by_id_task_from_firestore_with_get_result_returns_null() throws Exception {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskDocumentSnapshotMock).when(documentReferenceMock).get();
        doReturn(true).when(taskDocumentSnapshotMock).isSuccessful();
        doReturn(null).when(taskDocumentSnapshotMock).getResult();

        // WHEN
        workmatesRepository.getCurrentUserWhoHasChosenTodayFromFirestore(CURRENT_FIREBASE_USER_ID);

        // THEN
        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).get();

        ArgumentCaptor<Continuation<DocumentSnapshot, Workmate>> continuationArgumentCaptor = ArgumentCaptor.forClass(Continuation.class);
        verify(taskDocumentSnapshotMock).continueWith(continuationArgumentCaptor.capture());

        Continuation<DocumentSnapshot, Workmate> continuation = continuationArgumentCaptor.getValue();
        Workmate workmate = continuation.then(taskDocumentSnapshotMock);

        assertEquals(getDefaultCurrentUserEmpty(), workmate);

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void get_current_user_by_id_task_from_firestore_with_get_result_returns_not_null_and_document_snapshot_exists_returns_false() throws Exception {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskDocumentSnapshotMock).when(documentReferenceMock).get();
        doReturn(true).when(taskDocumentSnapshotMock).isSuccessful();
        doReturn(documentSnapshotMock).when(taskDocumentSnapshotMock).getResult();
        doReturn(false).when(documentSnapshotMock).exists();

        // WHEN
        workmatesRepository.getCurrentUserWhoHasChosenTodayFromFirestore(CURRENT_FIREBASE_USER_ID);

        // THEN
        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).get();

        ArgumentCaptor<Continuation<DocumentSnapshot, Workmate>> continuationArgumentCaptor = ArgumentCaptor.forClass(Continuation.class);
        verify(taskDocumentSnapshotMock).continueWith(continuationArgumentCaptor.capture());

        Continuation<DocumentSnapshot, Workmate> continuation = continuationArgumentCaptor.getValue();
        Workmate workmate = continuation.then(taskDocumentSnapshotMock);

        assertEquals(getDefaultCurrentUserEmpty(), workmate);

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void get_current_user_by_id_task_from_firestore() throws Exception {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskDocumentSnapshotMock).when(documentReferenceMock).get();
        doReturn(true).when(taskDocumentSnapshotMock).isSuccessful();

        DocumentSnapshot documentSnapshotMock = mock(DocumentSnapshot.class);
        doReturn(documentSnapshotMock).when(taskDocumentSnapshotMock).getResult();

        doReturn(true).when(documentSnapshotMock).exists();
        doReturn(getDefaultCurrentUser()).when(documentSnapshotMock).toObject(Workmate.class);

        // WHEN
        workmatesRepository.getCurrentUserWhoHasChosenTodayFromFirestore(CURRENT_FIREBASE_USER_ID);

        // THEN
        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).get();

        ArgumentCaptor<Continuation<DocumentSnapshot, Workmate>> continuationArgumentCaptor = ArgumentCaptor.forClass(Continuation.class);
        verify(taskDocumentSnapshotMock).continueWith(continuationArgumentCaptor.capture());

        Continuation<DocumentSnapshot, Workmate> continuation = continuationArgumentCaptor.getValue();
        Workmate workmate = continuation.then(taskDocumentSnapshotMock);
        assertEquals(getDefaultCurrentUser(), workmate);

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    //endregion

    //region ============================================================= ADD WORKMATE =============================================================

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
    public void add_workmate_with_mail_to_firestore_with_task_is_false() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(queryMock).when(collectionReferenceMock).whereEqualTo(EMAIL_PATH, MAIL_TO_ADD);
        doReturn(taskQuerySnapshotMock).when(queryMock).get();
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(false).when(taskQuerySnapshotMock).isSuccessful();

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        workmatesRepository.addWorkmateToFirestore(USER_ID_TO_ADD, getDefaultCurrentUserToAdd());

        // THEN
        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).whereEqualTo(EMAIL_PATH, MAIL_TO_ADD);
        verify(queryMock).get();

        verify(taskQuerySnapshotMock).addOnCompleteListener(querySnapshotCompleteListenerCaptor.capture());
        querySnapshotCompleteListenerCaptor.getValue().onComplete(taskQuerySnapshotMock);
        verify(taskQuerySnapshotMock).isSuccessful();

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void add_workmate_with_mail_to_firestore_with_task_is_successful_with_list_is_empty() {
        // GIVEN
        doReturn(queryMock).when(collectionReferenceMock).whereEqualTo(EMAIL_PATH, MAIL_TO_ADD);
        doReturn(taskQuerySnapshotMock).when(queryMock).get();
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);

        doReturn(true).when(taskQuerySnapshotMock).isSuccessful();

        doReturn(querySnapshotMock).when(taskQuerySnapshotMock).getResult();
        doReturn(true).when(querySnapshotMock).isEmpty();

        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(USER_ID_TO_ADD);
        doReturn(taskVoidMock).when(documentReferenceMock).set(getDefaultCurrentUserToAdd());

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        workmatesRepository.addWorkmateToFirestore(USER_ID_TO_ADD, getDefaultCurrentUserToAdd());

        // THEN
        verify(collectionReferenceMock).whereEqualTo(EMAIL_PATH, MAIL_TO_ADD);
        verify(queryMock).get();

        verify(taskQuerySnapshotMock).addOnCompleteListener(querySnapshotCompleteListenerCaptor.capture());
        querySnapshotCompleteListenerCaptor.getValue().onComplete(taskQuerySnapshotMock);
        verify(taskQuerySnapshotMock).isSuccessful();

        verify(taskQuerySnapshotMock).getResult();
        verify(querySnapshotMock).isEmpty();

        verify(firebaseFirestoreMock, atLeast(2)).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).document(USER_ID_TO_ADD);
        verify(documentReferenceMock).set(getDefaultCurrentUserToAdd());

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void add_workmate_with_mail_to_firestore_with_task_is_successful_with_list_is_not_empty() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(queryMock).when(collectionReferenceMock).whereEqualTo(EMAIL_PATH, MAIL_TO_ADD);
        doReturn(taskQuerySnapshotMock).when(queryMock).get();
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);

        doReturn(true).when(taskQuerySnapshotMock).isSuccessful();

        doReturn(querySnapshotMock).when(taskQuerySnapshotMock).getResult();
        doReturn(false).when(querySnapshotMock).isEmpty();

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        workmatesRepository.addWorkmateToFirestore(USER_ID_TO_ADD, getDefaultCurrentUserToAdd());

        // THEN
        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).whereEqualTo(EMAIL_PATH, MAIL_TO_ADD);
        verify(queryMock).get();

        verify(taskQuerySnapshotMock).addOnCompleteListener(querySnapshotCompleteListenerCaptor.capture());
        querySnapshotCompleteListenerCaptor.getValue().onComplete(taskQuerySnapshotMock);
        verify(taskQuerySnapshotMock).isSuccessful();

        verify(taskQuerySnapshotMock).getResult();
        verify(querySnapshotMock).isEmpty();

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }


    @Test
    public void add_workmate_with_name_and_picture_url_to_firestore_with_task_is_false() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(queryMock).when(collectionReferenceMock).whereEqualTo(NAME_PATH, USER_NAME_TO_ADD);
        doReturn(queryMock2).when(queryMock).whereEqualTo(PICTURE_URL_PATH, PICTURE_URL_TO_ADD);
        doReturn(taskQuerySnapshotMock).when(queryMock2).get();
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(false).when(taskQuerySnapshotMock).isSuccessful();

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        workmatesRepository.addWorkmateToFirestore(USER_ID_TO_ADD, getDefaultCurrentUserWithoutMailToAdd());

        // THEN
        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).whereEqualTo(NAME_PATH, USER_NAME_TO_ADD);
        verify(queryMock).whereEqualTo(PICTURE_URL_PATH, PICTURE_URL_TO_ADD);
        verify(queryMock2).get();

        verify(taskQuerySnapshotMock).addOnCompleteListener(querySnapshotCompleteListenerCaptor.capture());
        querySnapshotCompleteListenerCaptor.getValue().onComplete(taskQuerySnapshotMock);
        verify(taskQuerySnapshotMock).isSuccessful();

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void add_workmate_with_name_and_picture_url_to_firestore_with_task_is_successful_with_list_is_empty() {
        // GIVEN
        doReturn(queryMock).when(collectionReferenceMock).whereEqualTo(NAME_PATH, USER_NAME_TO_ADD);
        doReturn(queryMock2).when(queryMock).whereEqualTo(PICTURE_URL_PATH, PICTURE_URL_TO_ADD);
        doReturn(taskQuerySnapshotMock).when(queryMock2).get();
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);

        doReturn(true).when(taskQuerySnapshotMock).isSuccessful();

        doReturn(querySnapshotMock).when(taskQuerySnapshotMock).getResult();
        doReturn(true).when(querySnapshotMock).isEmpty();

        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(USER_ID_TO_ADD);
        doReturn(taskVoidMock).when(documentReferenceMock).set(getDefaultCurrentUserWithoutMailToAdd());

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        workmatesRepository.addWorkmateToFirestore(USER_ID_TO_ADD, getDefaultCurrentUserWithoutMailToAdd());

        // THEN
        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).whereEqualTo(NAME_PATH, USER_NAME_TO_ADD);
        verify(queryMock).whereEqualTo(PICTURE_URL_PATH, PICTURE_URL_TO_ADD);
        verify(queryMock2).get();

        verify(taskQuerySnapshotMock).addOnCompleteListener(querySnapshotCompleteListenerCaptor.capture());
        querySnapshotCompleteListenerCaptor.getValue().onComplete(taskQuerySnapshotMock);
        verify(taskQuerySnapshotMock).isSuccessful();

        verify(taskQuerySnapshotMock).getResult();
        verify(querySnapshotMock).isEmpty();

        verify(firebaseFirestoreMock, atLeast(2)).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).document(USER_ID_TO_ADD);
        verify(documentReferenceMock).set(getDefaultCurrentUserWithoutMailToAdd());

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    @Test
    public void add_workmate_with_name_and_picture_url_to_firestore_with_task_is_successful_with_list_is_not_empty() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_WORKMATES);
        doReturn(queryMock).when(collectionReferenceMock).whereEqualTo(NAME_PATH, USER_NAME_TO_ADD);
        doReturn(queryMock2).when(queryMock).whereEqualTo(PICTURE_URL_PATH, PICTURE_URL_TO_ADD);
        doReturn(taskQuerySnapshotMock).when(queryMock2).get();
        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);

        doReturn(true).when(taskQuerySnapshotMock).isSuccessful();

        doReturn(querySnapshotMock).when(taskQuerySnapshotMock).getResult();
        doReturn(false).when(querySnapshotMock).isEmpty();

        workmatesRepository = new WorkmatesRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        workmatesRepository.addWorkmateToFirestore(USER_ID_TO_ADD, getDefaultCurrentUserWithoutMailToAdd());

        // THEN
        verify(firebaseFirestoreMock).collection(ALL_WORKMATES);
        verify(collectionReferenceMock).whereEqualTo(NAME_PATH, USER_NAME_TO_ADD);
        verify(queryMock).whereEqualTo(PICTURE_URL_PATH, PICTURE_URL_TO_ADD);
        verify(queryMock2).get();

        verify(taskQuerySnapshotMock).addOnCompleteListener(querySnapshotCompleteListenerCaptor.capture());
        querySnapshotCompleteListenerCaptor.getValue().onComplete(taskQuerySnapshotMock);
        verify(taskQuerySnapshotMock).isSuccessful();

        verify(taskQuerySnapshotMock).getResult();
        verify(querySnapshotMock).isEmpty();

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }


    //endregion

    //region =========================================================== REMOVE WORKMATE  ===========================================================

    @Test
    public void remove_workmate_to_have_chosen_today_normal_case() {
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        doReturn(taskVoidMock).when(documentReferenceMock).delete();

        workmatesRepository.removeWorkmateToHaveChosenTodayList(CURRENT_FIREBASE_USER_ID);

        verify(firebaseFirestoreMock).collection(HAVE_CHOSEN_TODAY_COLLECTION_PATH);
        verify(collectionReferenceMock).document(CURRENT_FIREBASE_USER_ID);
        verify(documentReferenceMock).delete();

        verifyNoMoreInteractions(firebaseFirestoreMock);
    }

    //endregion

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultCurrentUser() {
        return new Workmate(CURRENT_FIREBASE_USER_ID, CURRENT_FIREBASE_USER_NAME, CURRENT_FIREBASE_MAIL, CURRENT_FIREBASE_PHOTO_URL, PLACE_ID_VALUE, RESTAURANT_NAME, DEFAULT_LIKED_RESTAURANTS);
    }

    private Workmate getDefaultCurrentUserToAdd() {
        return new Workmate(USER_ID_TO_ADD, USER_NAME_TO_ADD, MAIL_TO_ADD, PICTURE_URL_TO_ADD, "", "", new ArrayList<>());
    }

    private Workmate getDefaultCurrentUserWithoutMailToAdd() {
        return new Workmate(USER_ID_TO_ADD, USER_NAME_TO_ADD, "", PICTURE_URL_TO_ADD, "", "", new ArrayList<>());
    }

    private Workmate getDefaultCurrentUserEmpty() {
        return new Workmate("", "", "", "", "", "", new ArrayList<>());
    }

    //endregion

    private List<Workmate> getDefaultAllWorkmates() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultCurrentUser());
        workmates.add(new Workmate(WORKMATE_ID_2, WORKMATE_NAME_2, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate(WORKMATE_ID_3, WORKMATE_NAME_3, "mail", PICTURE_URL, PLACE_ID_VALUE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate(WORKMATE_ID_4, WORKMATE_NAME_4, "mail", PICTURE_URL, PLACE_ID_VALUE_NO_PICTURE, RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate(WORKMATE_ID_5, WORKMATE_NAME_5, "mail", PICTURE_URL, "", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate(WORKMATE_ID_6, WORKMATE_NAME_6, "mail", PICTURE_URL, "", RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }

    private List<Workmate> getDefaultEmptyWorkmates() {
        return new ArrayList<>();
    }
}