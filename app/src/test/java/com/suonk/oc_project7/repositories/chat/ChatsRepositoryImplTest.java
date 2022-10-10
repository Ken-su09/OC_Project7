package com.suonk.oc_project7.repositories.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ChatsRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ChatsRepository chatsRepository;

    //region ============================================= MOCK =============================================


    private final FirebaseFirestore firebaseFirestoreMock = mock(FirebaseFirestore.class);

    private final CollectionReference collectionReferenceMock = mock(CollectionReference.class);
    private final CollectionReference collectionReferenceMock_2 = mock(CollectionReference.class);
    private final DocumentReference documentReferenceMock = mock(DocumentReference.class);
    private final Task<Void> taskVoidMock = mock(Task.class);
    private final Task<DocumentReference> taskDocumentReferenceMock = mock(Task.class);

    private final QuerySnapshot querySnapshotMock = mock(QuerySnapshot.class);
    private final ListenerRegistration listenerRegistrationMock = mock(ListenerRegistration.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String ALL_ROOMS_PATH = "all_rooms";
    private static final String ALL_CHATS_PATH = "all_chats";

    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";

    private static final String ROOM_ID_1_2 = "ROOM_ID_1_2";
    private static final String ROOM_ID_3_4 = "ROOM_ID_3_4";

    private static final String WORKMATE_ID_1 = "WORKMATE_ID_1";
    private static final String WORKMATE_NAME_1 = "WORKMATE_NAME_1";
    private static final String WORKMATE_PICTURE_URL_1 = "WORKMATE_PICTURE_URL_1";

    private static final String WORKMATE_ID_2 = "WORKMATE_ID_2";
    private static final String WORKMATE_NAME_2 = "WORKMATE_NAME_2";
    private static final String WORKMATE_PICTURE_URL_2 = "WORKMATE_PICTURE_URL_2";

    private static final String WORKMATE_ID_3 = "WORKMATE_ID_3";
    private static final String WORKMATE_NAME_3 = "WORKMATE_NAME_3";
    private static final String WORKMATE_PICTURE_URL_3 = "WORKMATE_PICTURE_URL_3";

    private static final String WORKMATE_ID_4 = "WORKMATE_ID_4";
    private static final String WORKMATE_NAME_4 = "WORKMATE_NAME_4";
    private static final String WORKMATE_PICTURE_URL_4 = "WORKMATE_PICTURE_URL_4";

    private static final String LAST_MESSAGE = "LAST_MESSAGE";
    private static final Long TIMESTAMP = 10L;

    private static final List<String> DEFAULT_WORKMATE_IDS_1_2 = Arrays.asList(
            WORKMATE_ID_1,
            WORKMATE_ID_2);

    private static final List<String> DEFAULT_WORKMATE_IDS_2_1 = Arrays.asList(
            WORKMATE_ID_2,
            WORKMATE_ID_1);

    private static final List<String> DEFAULT_WORKMATE_NAMES_1_2 = Arrays.asList(
            WORKMATE_NAME_1,
            WORKMATE_NAME_2);

    private static final List<String> DEFAULT_WORKMATE_PICTURE_URLS_1_2 = Arrays.asList(
            WORKMATE_PICTURE_URL_1,
            WORKMATE_PICTURE_URL_2);

    private static final List<String> DEFAULT_WORKMATE_IDS_3_4 = Arrays.asList(
            WORKMATE_ID_3,
            WORKMATE_ID_4);

    private static final List<String> DEFAULT_WORKMATE_NAMES_3_4 = Arrays.asList(
            WORKMATE_NAME_3,
            WORKMATE_NAME_4);

    private static final List<String> DEFAULT_WORKMATE_PICTURE_URLS_3_4 = Arrays.asList(
            WORKMATE_PICTURE_URL_3,
            WORKMATE_PICTURE_URL_4);

    private static final String WORKMATE_ID_FAKE_1 = "WORKMATE_ID_FAKE_1";
    private static final String WORKMATE_ID_FAKE_2 = "WORKMATE_ID_FAKE_2";

    private static final List<String> DEFAULT_WORKMATE_IDS_FAKE = Arrays.asList(
            WORKMATE_ID_FAKE_1,
            WORKMATE_ID_FAKE_2);

    private static final String CHAT_ID_1 = "CHAT_ID_1";
    private static final String CHAT_ID_2 = "CHAT_ID_2";
    private static final String CHAT_ID_3 = "CHAT_ID_3";
    private static final String CHAT_ID_4 = "CHAT_ID_4";
    private static final String CHAT_ID_5 = "CHAT_ID_5";
    private static final String CHAT_ID_6 = "CHAT_ID_6";
    private static final String CHAT_ID_7 = "CHAT_ID_7";
    private static final String CHAT_ID_8 = "CHAT_ID_8";

    //endregion

    @Before
    public void setup() {
    }

    @After
    public void teardown() {
    }

    //region ========================================== GET ROOMS ===========================================

    @Test
    public void get_all_rooms_from_firestore_live_data() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<List<Room>> livedata = chatsRepository.getAllRoomsFromFirestoreLiveData();
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultAllRooms(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    //endregion

    //region =========================================== GET ROOM ===========================================

    @Test
    public void get_room_by_id_from_firestore_live_data() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<Room> livedata = chatsRepository.getRoomByIdFromFirestoreLiveData(DEFAULT_WORKMATE_IDS_1_2);
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultRoom_1_2(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    @Test
    public void get_room_by_id_from_firestore_live_data_with_reverse_workmate_ids() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<Room> livedata = chatsRepository.getRoomByIdFromFirestoreLiveData(DEFAULT_WORKMATE_IDS_2_1);
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultRoom_1_2(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    @Test
    public void get_room_by_id_from_firestore_live_data_with_workmates_ids_3_4() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<Room> livedata = chatsRepository.getRoomByIdFromFirestoreLiveData(DEFAULT_WORKMATE_IDS_3_4);
        livedata.observeForever(room -> {
            assertNotNull(room);
            assertEquals(getDefaultRoom_3_4(), room);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    @Test
    public void get_room_by_id_from_firestore_live_data_with_fake_workmates_ids() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(getDefaultAllRooms()).when(querySnapshotMock).toObjects(Room.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<Room> livedata = chatsRepository.getRoomByIdFromFirestoreLiveData(DEFAULT_WORKMATE_IDS_FAKE);
        livedata.observeForever(t -> {
        });
        Room room = livedata.getValue();
        assertNull(room);

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    //endregion

    //region ========================================== GET CHATS ===========================================

    @Test
    public void get_chat_list_by_room_id_live_data() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(ROOM_ID_1_2);
        doReturn(collectionReferenceMock_2).when(documentReferenceMock).collection(ALL_CHATS_PATH);
        doReturn(getDefaultAllChats_1_2()).when(querySnapshotMock).toObjects(Chat.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock_2).addSnapshotListener(querySnapshotListenerCaptor.capture());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<List<Chat>> livedata = chatsRepository.getChatListByRoomId(ROOM_ID_1_2);
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultAllChats_1_2(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).document(ROOM_ID_1_2);
        verify(documentReferenceMock).collection(ALL_CHATS_PATH);
        verify(collectionReferenceMock_2).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    @Test
    public void get_chat_list_by_room_id_live_data_with_other_chats() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(ROOM_ID_3_4);
        doReturn(collectionReferenceMock_2).when(documentReferenceMock).collection(ALL_CHATS_PATH);
        doReturn(getDefaultAllChats_3_4()).when(querySnapshotMock).toObjects(Chat.class);
        ArgumentCaptor<EventListener<QuerySnapshot>> querySnapshotListenerCaptor = ArgumentCaptor.forClass(EventListener.class);
        doReturn(listenerRegistrationMock).when(collectionReferenceMock_2).addSnapshotListener(querySnapshotListenerCaptor.capture());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        // WHEN
        LiveData<List<Chat>> livedata = chatsRepository.getChatListByRoomId(ROOM_ID_3_4);
        livedata.observeForever(t -> {
            assertNotNull(t);
            assertEquals(getDefaultAllChats_3_4(), t);
        });

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).document(ROOM_ID_3_4);
        verify(documentReferenceMock).collection(ALL_CHATS_PATH);
        verify(collectionReferenceMock_2).addSnapshotListener(querySnapshotListenerCaptor.capture());
        querySnapshotListenerCaptor.getValue().onEvent(querySnapshotMock, null);
    }

    //endregion

    //region =========================================== ADD NEW ============================================

    @Test
    public void add_new_room_to_firestore() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(ROOM_ID_1_2);
        doReturn(taskVoidMock).when(documentReferenceMock).set(getDefaultRoom_1_2());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        chatsRepository.addNewRoomToFirestore(ROOM_ID_1_2, getDefaultRoom_1_2());

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).document(ROOM_ID_1_2);
        verify(documentReferenceMock).set(getDefaultRoom_1_2());
    }

    @Test
    public void add_new_chat_to_room() {
        // GIVEN
        doReturn(collectionReferenceMock).when(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        doReturn(documentReferenceMock).when(collectionReferenceMock).document(ROOM_ID_1_2);
        doReturn(collectionReferenceMock_2).when(documentReferenceMock).collection(ALL_CHATS_PATH);
        doReturn(taskDocumentReferenceMock).when(collectionReferenceMock_2).add(getDefaultChat_1_2());

        chatsRepository = new ChatsRepositoryImpl(firebaseFirestoreMock);

        chatsRepository.addNewChatToRoom(ROOM_ID_1_2, getDefaultChat_1_2());

        verify(firebaseFirestoreMock).collection(ALL_ROOMS_PATH);
        verify(collectionReferenceMock).document(ROOM_ID_1_2);
        verify(documentReferenceMock).collection(ALL_CHATS_PATH);
        verify(collectionReferenceMock_2).add(getDefaultChat_1_2());
    }

    //endregion

    //region ======================================== DEFAULT ROOMS =========================================

    private Room getDefaultRoom_1_2() {
        return new Room(ROOM_ID_1_2, DEFAULT_WORKMATE_IDS_1_2, DEFAULT_WORKMATE_NAMES_1_2, DEFAULT_WORKMATE_PICTURE_URLS_1_2, LAST_MESSAGE, TIMESTAMP);
    }

    private Room getDefaultRoom_3_4() {
        return new Room(ROOM_ID_3_4, DEFAULT_WORKMATE_IDS_3_4, DEFAULT_WORKMATE_NAMES_3_4, DEFAULT_WORKMATE_PICTURE_URLS_3_4, LAST_MESSAGE, TIMESTAMP);
    }

    private List<Room> getDefaultAllRooms() {
        List<Room> rooms = new ArrayList<>();

        rooms.add(getDefaultRoom_1_2());
        rooms.add(getDefaultRoom_3_4());

        return rooms;
    }

    //endregion

    //region ======================================== DEFAULT CHATS =========================================

    private Chat getDefaultChat_1_2() {
        return new Chat(CHAT_ID_1, WORKMATE_ID_1, DEFAULT_MESSAGE, TIMESTAMP);
    }

    private List<Chat> getDefaultAllChats_1_2() {
        List<Chat> chats = new ArrayList<>();

        chats.add(new Chat(CHAT_ID_1, WORKMATE_ID_1, DEFAULT_MESSAGE, TIMESTAMP));
        chats.add(new Chat(CHAT_ID_2, WORKMATE_ID_2, LAST_MESSAGE, TIMESTAMP));
        chats.add(new Chat(CHAT_ID_3, WORKMATE_ID_1, DEFAULT_MESSAGE, TIMESTAMP));
        chats.add(new Chat(CHAT_ID_5, WORKMATE_ID_1, LAST_MESSAGE, TIMESTAMP));

        return chats;
    }

    private List<Chat> getDefaultAllChats_3_4() {
        List<Chat> chats = new ArrayList<>();

        chats.add(new Chat(CHAT_ID_4, WORKMATE_ID_4, LAST_MESSAGE, TIMESTAMP));
        chats.add(new Chat(CHAT_ID_6, WORKMATE_ID_3, LAST_MESSAGE, TIMESTAMP));
        chats.add(new Chat(CHAT_ID_7, WORKMATE_ID_3, LAST_MESSAGE, TIMESTAMP));
        chats.add(new Chat(CHAT_ID_8, WORKMATE_ID_4, LAST_MESSAGE, TIMESTAMP));

        return chats;
    }

    //endregion
}