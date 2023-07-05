package com.suonk.oc_project7.repositories.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class ChatsRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ChatsRepository chatsRepository;

    //region ============================================= MOCK =============================================

    private final FirebaseFirestore firebaseFirestoreMock = mock(FirebaseFirestore.class);

    private final CollectionReference collectionReferenceRoomsMock = mock(CollectionReference.class);
    private final CollectionReference collectionReferenceChatsMock = mock(CollectionReference.class);

    private final DocumentReference documentReferenceRoomsMock = mock(DocumentReference.class);

    private final DocumentReference documentReferenceRoomsMock_2 = mock(DocumentReference.class);
    private final CollectionReference collectionReferenceChatsMock_2 = mock(CollectionReference.class);

    private final Exception exceptionMock = mock(Exception.class);

    private final Task<Void> taskVoidMock = mock(Task.class);
    private final Task<DocumentReference> taskDocumentReferenceMock = mock(Task.class);
    private final Task<QuerySnapshot> getTaskQuerySnapshotReferenceMock = mock(Task.class);
    private final Task<QuerySnapshot> addOnCompleteTaskQuerySnapshotReferenceMock = mock(Task.class);

    private final QuerySnapshot querySnapshotMock = mock(QuerySnapshot.class);
    private final ListenerRegistration listenerRegistrationMock = mock(ListenerRegistration.class);

    private final Clock fixedClock = Clock.fixed(Instant.EPOCH.plusMillis(DEFAULT_TIMESTAMP_LONG), ZoneId.systemDefault());

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String ALL_ROOMS_PATH = "all_rooms";
    private static final String ALL_CHATS_PATH = "all_chats";

    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";

    private static final String ROOM_ID_CREATE = "ROOM_ID_CREATE";
    private static final String ROOM_ID_1_2 = "ROOM_ID_1_2";
    private static final String ROOM_ID_3_4 = "ROOM_ID_3_4";

    private static final String WORKMATE_ID_1 = "WORKMATE_ID_1";
    private static final String WORKMATE_ID_2 = "WORKMATE_ID_2";
    private static final String WORKMATE_ID_3 = "WORKMATE_ID_3";
    private static final String WORKMATE_ID_4 = "WORKMATE_ID_4";

    private static final String LAST_MESSAGE = "LAST_MESSAGE";

    private static final List<String> DEFAULT_WORKMATE_IDS_1_2 = Arrays.asList(WORKMATE_ID_1, WORKMATE_ID_2);
    private static final List<String> DEFAULT_WORKMATE_IDS_2_1 = Arrays.asList(WORKMATE_ID_2, WORKMATE_ID_1);
    private static final List<String> NON_SENSE_WORKMATE_IDS = Arrays.asList("NON_SENSE_WORKMATE_ID_1", "NON_SENSE_WORKMATE_ID_2");

    private static final List<String> DEFAULT_WORKMATE_IDS_3_4 = Arrays.asList(WORKMATE_ID_3, WORKMATE_ID_4);

    private static final String WORKMATE_ID_FAKE_1 = "WORKMATE_ID_FAKE_1";
    private static final String WORKMATE_ID_FAKE_2 = "WORKMATE_ID_FAKE_2";

    private static final List<String> DEFAULT_WORKMATE_IDS_FAKE = Arrays.asList(WORKMATE_ID_FAKE_1, WORKMATE_ID_FAKE_2);

    private static final String CHAT_ID_1 = "CHAT_ID_1";
    private static final String CHAT_ID_2 = "CHAT_ID_2";
    private static final String CHAT_ID_3 = "CHAT_ID_3";
    private static final String CHAT_ID_4 = "CHAT_ID_4";
    private static final String CHAT_ID_5 = "CHAT_ID_5";
    private static final String CHAT_ID_6 = "CHAT_ID_6";
    private static final String CHAT_ID_7 = "CHAT_ID_7";
    private static final String CHAT_ID_8 = "CHAT_ID_8";

    private static final Long DEFAULT_TIMESTAMP_LONG = 1000000000L;

    //endregion

    private void initConstructor() {
        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock, fixedClock);
    }

    @Before
    public void setup() {
        initConstructor();
    }

    //region =============================================================== GET ROOM ===============================================================

    @Test
    public void get_room_id_by_workmates_ids_live_data_with_task_is_successful_false_and_get_exception_null() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getTaskQuerySnapshotReferenceMock).when(collectionReferenceRoomsMock).get();

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(addOnCompleteTaskQuerySnapshotReferenceMock).when(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());

        doReturn(false).when(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        doReturn(null).when(addOnCompleteTaskQuerySnapshotReferenceMock).getException();

        // WHEN
        chatsRepository.getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2).observeForever(Assert::assertNull);

        verify(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onComplete(addOnCompleteTaskQuerySnapshotReferenceMock);
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).getException();

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).get();
    }

    @Test
    public void get_room_id_by_workmates_ids_live_data_with_task_is_successful_false_and_get_exception_mock() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getTaskQuerySnapshotReferenceMock).when(collectionReferenceRoomsMock).get();

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(addOnCompleteTaskQuerySnapshotReferenceMock).when(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());

        doReturn(false).when(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        doReturn(exceptionMock).when(addOnCompleteTaskQuerySnapshotReferenceMock).getException();

        // WHEN
        chatsRepository.getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2).observeForever(Assert::assertNull);

        verify(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onComplete(addOnCompleteTaskQuerySnapshotReferenceMock);
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).getException();

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).get();
    }

    @Test
    public void get_room_id_by_workmates_ids_live_data_with_task_is_successful_true_and_rooms_empty() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getTaskQuerySnapshotReferenceMock).when(collectionReferenceRoomsMock).get();
        doReturn(documentReferenceRoomsMock).when(collectionReferenceRoomsMock).document();
        doReturn(ROOM_ID_CREATE).when(documentReferenceRoomsMock).getId();

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(addOnCompleteTaskQuerySnapshotReferenceMock).when(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());

        doReturn(true).when(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        doReturn(querySnapshotMock).when(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        doReturn(getDefaultEmptyRooms()).when(querySnapshotMock).toObjects(Room.class);

        // WHEN
        chatsRepository.getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2).observeForever(roomId -> {
            assertNotNull(roomId);
            assertEquals(roomId, ROOM_ID_CREATE);
        });

        verify(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onComplete(addOnCompleteTaskQuerySnapshotReferenceMock);
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();

        verify(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        verify(querySnapshotMock).toObjects(Room.class);

        verify(firebaseFirestoreMock, atLeast(2)).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).get();
        verify(collectionReferenceRoomsMock).document();
        verify(documentReferenceRoomsMock).getId();
    }

    @Test
    public void get_room_id_by_workmates_ids_live_data_with_task_is_successful_true() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getTaskQuerySnapshotReferenceMock).when(collectionReferenceRoomsMock).get();

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(addOnCompleteTaskQuerySnapshotReferenceMock).when(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());

        doReturn(true).when(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        doReturn(querySnapshotMock).when(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);

        // WHEN
        chatsRepository.getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2).observeForever(roomId -> {
            assertNotNull(roomId);
            assertEquals(roomId, ROOM_ID_1_2);
        });

        verify(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onComplete(addOnCompleteTaskQuerySnapshotReferenceMock);
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();

        verify(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        verify(querySnapshotMock).toObjects(Room.class);

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).get();
    }

    @Test
    public void get_room_id_by_workmates_ids_live_data_with_non_sense_workmate_ids() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getTaskQuerySnapshotReferenceMock).when(collectionReferenceRoomsMock).get();
        doReturn(documentReferenceRoomsMock).when(collectionReferenceRoomsMock).document();
        doReturn(ROOM_ID_CREATE).when(documentReferenceRoomsMock).getId();

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(addOnCompleteTaskQuerySnapshotReferenceMock).when(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());

        doReturn(true).when(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        doReturn(querySnapshotMock).when(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);

        // WHEN
        chatsRepository.getRoomIdByWorkmateIds(NON_SENSE_WORKMATE_IDS).observeForever(roomId -> {
            assertNotNull(roomId);
            assertEquals(roomId, ROOM_ID_CREATE);
        });

        verify(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onComplete(addOnCompleteTaskQuerySnapshotReferenceMock);
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();

        verify(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        verify(querySnapshotMock).toObjects(Room.class);

        verify(firebaseFirestoreMock, atLeast(2)).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).get();
        verify(collectionReferenceRoomsMock).document();
        verify(documentReferenceRoomsMock).getId();
    }

    @Test
    public void get_room_id_by_workmates_ids_live_data_with_reverse_workmate_ids() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getTaskQuerySnapshotReferenceMock).when(collectionReferenceRoomsMock).get();

        ArgumentCaptor<OnCompleteListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(addOnCompleteTaskQuerySnapshotReferenceMock).when(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());

        doReturn(true).when(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();
        doReturn(querySnapshotMock).when(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);

        // WHEN
        chatsRepository.getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_2_1).observeForever(roomId -> {
            assertNotNull(roomId);
            assertEquals(roomId, ROOM_ID_1_2);
        });

        verify(getTaskQuerySnapshotReferenceMock).addOnCompleteListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onComplete(addOnCompleteTaskQuerySnapshotReferenceMock);
        verify(addOnCompleteTaskQuerySnapshotReferenceMock).isSuccessful();

        verify(addOnCompleteTaskQuerySnapshotReferenceMock).getResult();
        verify(querySnapshotMock).toObjects(Room.class);

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).get();
    }

    //endregion

    //region ============================================================== GET CHATS ===============================================================

    @Test
    public void get_chat_list_by_room_id_live_data() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(documentReferenceRoomsMock).when(collectionReferenceRoomsMock).document(ROOM_ID_1_2);
        doReturn(collectionReferenceChatsMock).when(documentReferenceRoomsMock).collection(ALL_CHATS_PATH);
        doReturn(getDefaultAllChats_1_2()).when(querySnapshotMock).toObjects(Chat.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceChatsMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        initConstructor();

        // WHEN
        LiveData<List<Chat>> livedata = chatsRepository.getChatListByRoomId(ROOM_ID_1_2);
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultAllChats_1_2(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).document(ROOM_ID_1_2);
        verify(documentReferenceRoomsMock).collection(ALL_CHATS_PATH);
        verify(collectionReferenceChatsMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    @Test
    public void get_chat_list_by_room_id_live_data_with_other_chats() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(documentReferenceRoomsMock).when(collectionReferenceRoomsMock).document(ROOM_ID_3_4);
        doReturn(collectionReferenceChatsMock).when(documentReferenceRoomsMock).collection(ALL_CHATS_PATH);
        doReturn(getDefaultAllChats_3_4()).when(querySnapshotMock).toObjects(Chat.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceChatsMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        initConstructor();

        // WHEN
        LiveData<List<Chat>> livedata = chatsRepository.getChatListByRoomId(ROOM_ID_3_4);
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultAllChats_3_4(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).document(ROOM_ID_3_4);
        verify(documentReferenceRoomsMock).collection(ALL_CHATS_PATH);
        verify(collectionReferenceChatsMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    //endregion

    //region =============================================================== ADD NEW ================================================================

    @Test
    public void add_new_room_to_firestore() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(documentReferenceRoomsMock).when(collectionReferenceRoomsMock).document(ROOM_ID_1_2);
        doReturn(taskVoidMock).when(documentReferenceRoomsMock).set(getDefaultRoom_1_2());

        initConstructor();

        chatsRepository.addNewRoomToFirestore(getDefaultRoom_1_2());

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceRoomsMock).document(ROOM_ID_1_2);
        verify(documentReferenceRoomsMock).set(getDefaultRoom_1_2());
    }

    @Test
    public void add_new_chat_to_room() {
        // GIVEN
        doReturn(collectionReferenceRoomsMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);

        doReturn(documentReferenceRoomsMock_2).when(collectionReferenceRoomsMock).document();
        doReturn(collectionReferenceChatsMock_2).when(documentReferenceRoomsMock_2).collection(ALL_CHATS_PATH);
        doReturn(CHAT_ID_1).when(collectionReferenceChatsMock_2).getId();

        doReturn(documentReferenceRoomsMock).when(collectionReferenceRoomsMock).document(ROOM_ID_1_2);
        doReturn(collectionReferenceChatsMock).when(documentReferenceRoomsMock).collection(ALL_CHATS_PATH);

        doReturn(taskDocumentReferenceMock).when(collectionReferenceChatsMock).add(getDefaultChat_1_2());

        initConstructor();

        chatsRepository.addNewChatToRoom(ROOM_ID_1_2, WORKMATE_ID_1, DEFAULT_MESSAGE);

        verify(firebaseFirestoreMock, atLeastOnce()).collection(ALL_ROOMS_PATH);

        verify(collectionReferenceRoomsMock).document();
        verify(documentReferenceRoomsMock_2).collection(ALL_CHATS_PATH);
        verify(collectionReferenceChatsMock_2).getId();

        verify(collectionReferenceRoomsMock).document(ROOM_ID_1_2);
        verify(documentReferenceRoomsMock, atLeastOnce()).collection(ALL_CHATS_PATH);
        verify(collectionReferenceChatsMock).add(getDefaultChat_1_2());
    }

    //endregion

    //region ============================================================ DEFAULT ROOMS =============================================================

    private Room getDefaultRoom_1_2() {
        return new Room(ROOM_ID_1_2, DEFAULT_WORKMATE_IDS_1_2);
    }

    private Room getDefaultRoom_3_4() {
        return new Room(ROOM_ID_3_4, DEFAULT_WORKMATE_IDS_3_4);
    }

    private List<Room> getDefaultAllRooms() {
        List<Room> rooms = new ArrayList<>();

        rooms.add(getDefaultRoom_1_2());
        rooms.add(getDefaultRoom_3_4());

        return rooms;
    }

    private List<Room> getDefaultEmptyRooms() {
        return new ArrayList<>();
    }

    //endregion

    //region ======================================== DEFAULT CHATS =========================================

    private Chat getDefaultChat_1_2() {
        return new Chat(CHAT_ID_1, WORKMATE_ID_1, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG);
    }

    private List<Chat> getDefaultAllChats_1_2() {
        List<Chat> chats = new ArrayList<>();

        chats.add(new Chat(CHAT_ID_1, WORKMATE_ID_1, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(CHAT_ID_2, WORKMATE_ID_2, LAST_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(CHAT_ID_3, WORKMATE_ID_1, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(CHAT_ID_5, WORKMATE_ID_1, LAST_MESSAGE, DEFAULT_TIMESTAMP_LONG));

        return chats;
    }

    private List<Chat> getDefaultAllChats_3_4() {
        List<Chat> chats = new ArrayList<>();

        chats.add(new Chat(CHAT_ID_4, WORKMATE_ID_4, LAST_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(CHAT_ID_6, WORKMATE_ID_3, LAST_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(CHAT_ID_7, WORKMATE_ID_3, LAST_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(CHAT_ID_8, WORKMATE_ID_4, LAST_MESSAGE, DEFAULT_TIMESTAMP_LONG));

        return chats;
    }

    //endregion
}