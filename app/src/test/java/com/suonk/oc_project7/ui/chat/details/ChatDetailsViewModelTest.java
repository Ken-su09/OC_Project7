package com.suonk.oc_project7.ui.chat.details;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.graphics.Color;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.chat.add.AddNewChatToRoomUseCase;
import com.suonk.oc_project7.domain.chat.get.GetChatListByRoomIdUseCase;
import com.suonk.oc_project7.domain.chat.get.GetRoomIdByWorkmateIdsUseCase;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

    private final GetRoomIdByWorkmateIdsUseCase getRoomByIdFromFirestoreUseCaseMock = mock(GetRoomIdByWorkmateIdsUseCase.class);

    private final GetChatListByRoomIdUseCase getChatListByRoomIdUseCaseMock = mock(GetChatListByRoomIdUseCase.class);
    private final SavedStateHandle savedStateHandle = mock(SavedStateHandle.class);
    private final UserRepository userRepositoryMock = mock(UserRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String WORKMATE_ID = "WORKMATE_ID";
    private static final String WORKMATE_ID_VALUE = "WORKMATE_ID_VALUE";
    private static final String WORKMATE_NAME = "WORKMATE_NAME";

    private static final String DEFAULT_USER_ID = "DEFAULT_USER_ID";
    private static final String DEFAULT_USER_NAME = "DEFAULT_USER_NAME";
    private static final String DEFAULT_USER_EMAIL = "DEFAULT_USER_EMAIL";
    private static final String DEFAULT_USER_PICTURE_URL = "DEFAULT_USER_PICTURE_URL";

    private static final String DEFAULT_ROOM_ID_1_2 = "DEFAULT_ROOM_ID_1_2";
    private static final String DEFAULT_CHAT_ID_1_2 = "DEFAULT_CHAT_ID_1_2";

    private static final List<String> DEFAULT_ROOM_WORKMATE_IDS_1_2 = Arrays.asList(
            WORKMATE_ID_VALUE,
            DEFAULT_USER_ID);

    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";
    private static final String NO_MESSAGE = "";
    private static final Long DEFAULT_TIMESTAMP_LONG = 1000000000L;
    private static final String DEFAULT_TIMESTAMP = "12/01/1970 14:46";

    private static final int TEXT_COLOR_WHITE = Color.WHITE;
    private static final int TEXT_COLOR_BLACK = Color.BLACK;
    private static final int BACKGROUND_COLOR_ORANGE = R.drawable.custom_message_background_sender;
    private static final int BACKGROUND_COLOR_WHITE = R.drawable.custom_message_background_receiver;

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<String> currentRoomIdLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Chat>> chatsListMutableLiveData = new MutableLiveData<>();

    //endregion

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(
                addNewChatToRoomUseCaseMock,
                getRoomByIdFromFirestoreUseCaseMock,
                getChatListByRoomIdUseCaseMock,
                savedStateHandle,
                userRepositoryMock
        );
    }

    public void initChatDetailsViewModel() {
        chatDetailsViewModel = new ChatDetailsViewModel(
                addNewChatToRoomUseCaseMock,
                getRoomByIdFromFirestoreUseCaseMock,
                getChatListByRoomIdUseCaseMock,
                savedStateHandle,
                userRepositoryMock
        );
    }

    //region ============================================= GET ==============================================

    @Test
    public void get_all_chats_view_state() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(getDefaultAllChats_1_2());

        initChatDetailsViewModel();

        // WHEN
        List<ChatDetailsViewState> chatDetails = TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());

        // THEN
        assertNotNull(chatDetails);
        assertEquals(getListOfChatDetailsViewState(), chatDetails);

        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    @Test
    public void get_all_chats_view_state_with_chats_empty() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(getDefaultAllEmptyChatsChats_1_2());

        initChatDetailsViewModel();

        // WHEN
        List<ChatDetailsViewState> chatDetails = TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());

        // THEN
        assertNotNull(chatDetails);

        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    @Test
    public void get_all_chats_view_state_with_chats_null() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(null);

        initChatDetailsViewModel();

        // WHEN
        List<ChatDetailsViewState> chatDetails = TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());

        // THEN
        assertNotNull(chatDetails);

        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    //endregion

    //region ============================================== ADD =============================================

    @Test
    public void add_message() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        doNothing().when(addNewChatToRoomUseCaseMock).addNewChatToRoom(DEFAULT_ROOM_ID_1_2, DEFAULT_ROOM_WORKMATE_IDS_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(getDefaultAllChats_1_2());

        initChatDetailsViewModel();

        // WHEN
        TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());
        chatDetailsViewModel.addMessage(DEFAULT_MESSAGE);

        // THEN
        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
        verify(addNewChatToRoomUseCaseMock).addNewChatToRoom(DEFAULT_ROOM_ID_1_2, DEFAULT_ROOM_WORKMATE_IDS_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE);
    }

    @Test
    public void add_message_with_workmate_and_current_user_null() {
        // GIVEN
        doReturn(null).when(userRepositoryMock).getCustomFirebaseUser();

        initChatDetailsViewModel();

        // WHEN
        chatDetailsViewModel.addMessage(DEFAULT_MESSAGE);

        // THEN
        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    @Test
    public void add_message_with_empty_message() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(getDefaultAllChats_1_2());

        initChatDetailsViewModel();

        // WHEN
        TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());
        chatDetailsViewModel.addMessage("");

        // THEN
        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);
        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    //endregion

    //region ====================================== SINGLE LIVE EVENT =======================================

    @Test
    public void get_single_live_event_message_is_empty() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        doNothing().when(addNewChatToRoomUseCaseMock).addNewChatToRoom(DEFAULT_ROOM_ID_1_2, DEFAULT_ROOM_WORKMATE_IDS_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(getDefaultAllChats_1_2());

        initChatDetailsViewModel();

        // WHEN
        TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());
        chatDetailsViewModel.addMessage(DEFAULT_MESSAGE);

        Boolean isMessageEmpty = TestUtils.getValueForTesting(chatDetailsViewModel.getIsMessageEmpty());
        assertNotNull(isMessageEmpty);
        assertFalse(isMessageEmpty);

        // THEN
        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
        verify(addNewChatToRoomUseCaseMock).addNewChatToRoom(DEFAULT_ROOM_ID_1_2, DEFAULT_ROOM_WORKMATE_IDS_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE);
    }

    @Test
    public void get_single_live_event_message_is_empty_if_message_empty() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(getDefaultAllChats_1_2());

        initChatDetailsViewModel();

        // WHEN
        TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());
        chatDetailsViewModel.addMessage("");

        Boolean isMessageEmpty = TestUtils.getValueForTesting(chatDetailsViewModel.getIsMessageEmpty());
        assertNotNull(isMessageEmpty);
        assertTrue(isMessageEmpty);

        // THEN
        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    @Test
    public void get_single_live_event_is_there_error_with_workmate_and_user_ids_not_null() {
        // GIVEN
        doReturn(WORKMATE_ID_VALUE).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        doReturn(currentRoomIdLiveData).when(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        doReturn(chatsListMutableLiveData).when(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        currentRoomIdLiveData.setValue(DEFAULT_ROOM_ID_1_2);
        chatsListMutableLiveData.setValue(getDefaultAllChats_1_2());

        initChatDetailsViewModel();

        // WHEN
        TestUtils.getValueForTesting(chatDetailsViewModel.getChatDetails());
        Boolean isThereError = TestUtils.getValueForTesting(chatDetailsViewModel.getIsThereError());

        // THEN
        assertNotNull(isThereError);
        assertEquals(false, isThereError);

        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);

        verify(getRoomByIdFromFirestoreUseCaseMock).getRoomIdByWorkmateIds(DEFAULT_ROOM_WORKMATE_IDS_1_2);
        verify(getChatListByRoomIdUseCaseMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    @Test
    public void get_single_live_event_is_there_error_with_workmate_id_null_and_user_id_not_null() {
        // GIVEN
        doReturn(null).when(savedStateHandle).get(WORKMATE_ID);

        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();

        initChatDetailsViewModel();

        // WHEN
        Boolean isThereError = TestUtils.getValueForTesting(chatDetailsViewModel.getIsThereError());

        // THEN
        assertNotNull(isThereError);
        assertEquals(true, isThereError);

        verify(savedStateHandle, atLeastOnce()).get(WORKMATE_ID);
        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    @Test
    public void get_single_live_event_is_there_error_with_workmate_id_not_null_and_user_id_null() {
        // GIVEN
        doReturn(null).when(userRepositoryMock).getCustomFirebaseUser();

        initChatDetailsViewModel();

        // WHEN
        Boolean isThereError = TestUtils.getValueForTesting(chatDetailsViewModel.getIsThereError());

        // THEN
        assertNotNull(isThereError);
        assertEquals(true, isThereError);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    @Test
    public void get_single_live_event_is_there_error_with_workmate_and_user_ids_null() {
        // GIVEN
        doReturn(null).when(userRepositoryMock).getCustomFirebaseUser();

        initChatDetailsViewModel();

        // WHEN
        Boolean isThereError = TestUtils.getValueForTesting(chatDetailsViewModel.getIsThereError());

        // THEN
        assertNotNull(isThereError);
        assertEquals(true, isThereError);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();
    }

    //endregion

    //region ======================================== DEFAULT ROOMS =========================================

    //endregion

    //region ======================================== DEFAULT CHATS =========================================

    private List<ChatDetailsViewState> getListOfChatDetailsViewState() {
        List<ChatDetailsViewState> listOfChatDetailsViewState = new ArrayList<>();

        listOfChatDetailsViewState.add(new ChatDetailsViewState(
                DEFAULT_CHAT_ID_1_2,
                TEXT_COLOR_WHITE,
                BACKGROUND_COLOR_ORANGE,
                DEFAULT_MESSAGE,
                DEFAULT_TIMESTAMP,
                true,
                DEFAULT_USER_PICTURE_URL
        ));

        listOfChatDetailsViewState.add(new ChatDetailsViewState(
                DEFAULT_CHAT_ID_1_2,
                TEXT_COLOR_BLACK,
                BACKGROUND_COLOR_WHITE,
                DEFAULT_MESSAGE,
                DEFAULT_TIMESTAMP,
                false,
                ""));

        listOfChatDetailsViewState.add(new ChatDetailsViewState(
                DEFAULT_CHAT_ID_1_2,
                TEXT_COLOR_WHITE,
                BACKGROUND_COLOR_ORANGE,
                DEFAULT_MESSAGE,
                DEFAULT_TIMESTAMP,
                true,
                DEFAULT_USER_PICTURE_URL));

        listOfChatDetailsViewState.add(new ChatDetailsViewState(
                DEFAULT_CHAT_ID_1_2,
                TEXT_COLOR_BLACK,
                BACKGROUND_COLOR_WHITE,
                DEFAULT_MESSAGE,
                DEFAULT_TIMESTAMP,
                false,
                ""));

        return listOfChatDetailsViewState;
    }

    private List<Chat> getDefaultAllChats_1_2() {
        List<Chat> chats = new ArrayList<>();

        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));

        return chats;
    }

    private List<Chat> getDefaultAllEmptyChatsChats_1_2() {
        return new ArrayList<>();
    }

    //endregion

    private CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(
                DEFAULT_USER_ID,
                DEFAULT_USER_NAME,
                DEFAULT_USER_EMAIL,
                DEFAULT_USER_PICTURE_URL
        );
    }
}