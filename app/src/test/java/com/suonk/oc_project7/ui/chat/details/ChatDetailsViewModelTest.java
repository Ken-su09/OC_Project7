package com.suonk.oc_project7.ui.chat.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.graphics.Color;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.chat.add.AddNewChatToRoomUseCase;
import com.suonk.oc_project7.domain.chat.add.AddNewRoomToFirestoreUseCase;
import com.suonk.oc_project7.domain.chat.get.GetChatListByRoomIdUseCase;
import com.suonk.oc_project7.domain.chat.get.GetRoomByIdFromFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmateByIdUseCase;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ChatDetailsViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ChatDetailsViewModel chatDetailsViewModel;

    //region ============================================= MOCK =============================================

    private final AddNewChatToRoomUseCase addNewChatToRoomUseCaseMock = mock(AddNewChatToRoomUseCase.class);
    private final AddNewRoomToFirestoreUseCase addNewRoomToFirestoreUseCaseMock = mock(AddNewRoomToFirestoreUseCase.class);
    private final GetRoomByIdFromFirestoreUseCase getRoomByIdFromFirestoreUseCaseMock = mock(GetRoomByIdFromFirestoreUseCase.class);
    private final GetChatListByRoomIdUseCase getChatListByRoomIdUseCaseMock = mock(GetChatListByRoomIdUseCase.class);
    private final GetWorkmateByIdUseCase getWorkmateByIdUseCaseMock = mock(GetWorkmateByIdUseCase.class);
    private final SavedStateHandle savedStateHandle = mock(SavedStateHandle.class);
    private final UserRepository userRepositoryMock = mock(UserRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String WORKMATE_ID = "WORKMATE_ID";
    private static final String WORKMATE_ID_VALUE = "WORKMATE_ID_VALUE";
    private static final String WORKMATE_NAME = "WORKMATE_NAME";
    private static final String WORKMATE_EMAIL = "WORKMATE_EMAIL";
    private static final String WORKMATE_PICTURE_URL = "WORKMATE_PICTURE_URL";
    private static final String WORKMATE_ID_WRONG_VALUE = "WORKMATE_ID_WRONG_VALUE";

    private static final String DEFAULT_USER_ID = "DEFAULT_USER_ID";
    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final String DEFAULT_USER_EMAIL = "DEFAULT_USER_EMAIL";
    private static final String DEFAULT_USER_PICTURE_URL = "DEFAULT_USER_PICTURE_URL";

    private static final String DEFAULT_ROOM_ID_1_2 = "DEFAULT_ROOM_ID_1_2";
    private static final String DEFAULT_CHAT_ID_1_2 = "DEFAULT_CHAT_ID_1_2";

    private static final ArrayList<String> DEFAULT_ROOM_WORKMATE_IDS_1_2 = new ArrayList<>(Arrays.asList(
            DEFAULT_USER_ID,
            WORKMATE_ID_VALUE));

    private static final List<String> DEFAULT_ROOM_WORKMATE_NAMES_1_2 = Arrays.asList(
            DEFAULT_USER_NAME,
            WORKMATE_NAME);

    private static final List<String> DEFAULT_ROOM_WORKMATE_PICTURE_URL_1_2 = Arrays.asList(
            DEFAULT_USER_PICTURE_URL,
            WORKMATE_PICTURE_URL);

    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";
    private static final Long DEFAULT_TIMESTAMP = 1000000000L;

    private static final int TEXT_COLOR_WHITE = Color.WHITE;
    private static final int TEXT_COLOR_BLACK = Color.BLACK;
    private static final int BACKGROUND_COLOR_ORANGE = R.drawable.custom_message_background_sender;
    private static final int BACKGROUND_COLOR_WHITE = R.drawable.custom_message_background_receiver;

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<Workmate> currentWorkmateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Workmate> currentUserLiveData = new MutableLiveData<>();
    private final MutableLiveData<Room> roomLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Chat>> chatsListMutableLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);


        doReturn(currentWorkmateLiveData).when(getWorkmateByIdUseCaseMock).getWorkmateByIdLiveData(WORKMATE_ID_VALUE);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();
        doReturn(currentUserLiveData).when(getWorkmateByIdUseCaseMock).getWorkmateByIdLiveData(DEFAULT_USER_ID);

        doReturn(roomLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomByIdFromFirestore(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        doNothing().when(addNewRoomToFirestoreUseCaseMock).addNewRoomToFirestore(DEFAULT_ROOM_ID_1_2, getDefaultRoom_1_2());
        doNothing().when(addNewChatToRoomUseCaseMock).addNewChatToRoom(DEFAULT_ROOM_ID_1_2, getDefaultChat_1_2());
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(
                addNewChatToRoomUseCaseMock,
                addNewRoomToFirestoreUseCaseMock,
                getRoomByIdFromFirestoreUseCaseMock,
                getChatListByRoomIdUseCaseMock,
                getWorkmateByIdUseCaseMock,
                savedStateHandle,
                userRepositoryMock
        );
    }

    public void initChatDetailsViewModel() {
        chatDetailsViewModel = new ChatDetailsViewModel(
                addNewChatToRoomUseCaseMock,
                addNewRoomToFirestoreUseCaseMock,
                getRoomByIdFromFirestoreUseCaseMock,
                getChatListByRoomIdUseCaseMock,
                getWorkmateByIdUseCaseMock,
                savedStateHandle,
                userRepositoryMock
        );
    }

    @Test
    public void get_all_chats_view_state() {
        // GIVEN
        roomLiveData.setValue(getDefaultRoom_1_2());
        chatsListMutableLiveData.setValue(getDefaultAllChats_1_2());
        currentUserLiveData.setValue(getDefaultCurrentUser());
        currentWorkmateLiveData.setValue(getDefaultWorkmate());

        initChatDetailsViewModel();

        // WHEN
        List<ChatDetailsViewState> chatDetails = TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());
        assertNotNull(chatDetails);
        assertEquals(getListOfChatDetailsViewState(), chatDetails);

        verify(savedStateHandle).get(WORKMATE_ID);

        verify(getWorkmateByIdUseCaseMock).getWorkmateByIdLiveData(WORKMATE_ID_VALUE);
        verify(getWorkmateByIdUseCaseMock).getWorkmateByIdLiveData(DEFAULT_USER_ID);
        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomByIdFromFirestore(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock).getCustomFirebaseUser();

        verify(addNewRoomToFirestoreUseCaseMock).addNewRoomToFirestore(DEFAULT_ROOM_ID_1_2, getDefaultRoom_1_2());
        verify(addNewChatToRoomUseCaseMock).addNewChatToRoom(DEFAULT_ROOM_ID_1_2, getDefaultChat_1_2());
    }

    //region ======================================== DEFAULT ROOMS =========================================

    private Room getDefaultRoom_1_2() {
        return new Room(DEFAULT_ROOM_ID_1_2, DEFAULT_ROOM_WORKMATE_IDS_1_2, DEFAULT_ROOM_WORKMATE_NAMES_1_2,
                DEFAULT_ROOM_WORKMATE_PICTURE_URL_1_2, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP);
    }

//    private Room getDefaultRoom_3_4() {
//        return new Room(ROOM_ID_3_4, DEFAULT_WORKMATE_IDS_3_4, DEFAULT_WORKMATE_NAMES_3_4, DEFAULT_WORKMATE_PICTURE_URLS_3_4, LAST_MESSAGE, TIMESTAMP);
//    }

    private List<Room> getDefaultAllRooms() {
        List<Room> rooms = new ArrayList<>();

        rooms.add(getDefaultRoom_1_2());
//        rooms.add(getDefaultRoom_3_4());

        return rooms;
    }

    //endregion

    //region ======================================== DEFAULT CHATS =========================================

    private List<ChatDetailsViewState> getListOfChatDetailsViewState() {
        List<ChatDetailsViewState> listOfChatDetailsViewState = new ArrayList<>();

        listOfChatDetailsViewState.add(new ChatDetailsViewState(
                DEFAULT_CHAT_ID_1_2,
                WORKMATE_NAME,
                WORKMATE_PICTURE_URL,
                TEXT_COLOR_WHITE,
                BACKGROUND_COLOR_ORANGE,
                DEFAULT_MESSAGE,
                DEFAULT_TIMESTAMP.toString()));

        listOfChatDetailsViewState.add(new ChatDetailsViewState(
                DEFAULT_CHAT_ID_1_2,
                WORKMATE_NAME,
                WORKMATE_PICTURE_URL,
                TEXT_COLOR_WHITE,
                BACKGROUND_COLOR_ORANGE,
                DEFAULT_MESSAGE,
                DEFAULT_TIMESTAMP.toString()));

        return listOfChatDetailsViewState;
    }

    private Chat getDefaultChat_1_2() {
        return new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP);
    }

    private List<Chat> getDefaultAllChats_1_2() {
        List<Chat> chats = new ArrayList<>();

        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP));

        return chats;
    }

    //    private List<Chat> getDefaultAllChats_3_4() {
//        List<Chat> chats = new ArrayList<>();
//
//        chats.add(new Chat(CHAT_ID_4, WORKMATE_ID_4, LAST_MESSAGE, TIMESTAMP));
//        chats.add(new Chat(CHAT_ID_6, WORKMATE_ID_3, LAST_MESSAGE, TIMESTAMP));
//        chats.add(new Chat(CHAT_ID_7, WORKMATE_ID_3, LAST_MESSAGE, TIMESTAMP));
//        chats.add(new Chat(CHAT_ID_8, WORKMATE_ID_4, LAST_MESSAGE, TIMESTAMP));
//
//        return chats;
//    }

    //endregion

    private CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(
                DEFAULT_USER_ID,
                DEFAULT_USER_NAME,
                DEFAULT_USER_EMAIL,
                DEFAULT_USER_PICTURE_URL
        );
    }

    private Workmate getDefaultCurrentUser() {
        return new Workmate(
                DEFAULT_USER_ID,
                DEFAULT_USER_NAME,
                DEFAULT_USER_EMAIL,
                DEFAULT_USER_PICTURE_URL,
                "",
                "",
                new ArrayList<>()
        );
    }

    private Workmate getDefaultWorkmate() {
        return new Workmate(
                WORKMATE_ID_VALUE,
                WORKMATE_NAME,
                WORKMATE_PICTURE_URL,
                WORKMATE_PICTURE_URL,
                "",
                "",
                new ArrayList<>()
        );
    }
}