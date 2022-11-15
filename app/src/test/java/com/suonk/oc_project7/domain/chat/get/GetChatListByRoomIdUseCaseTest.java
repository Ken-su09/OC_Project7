package com.suonk.oc_project7.domain.chat.get;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetChatListByRoomIdUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GetChatListByRoomIdUseCase getChatListByRoomIdUseCase;

    //region ============================================= MOCK =============================================

    @NonNull
    private final ChatsRepository chatsRepositoryMock = mock(ChatsRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String WORKMATE_ID_VALUE = "WORKMATE_ID_VALUE";
    private static final String DEFAULT_USER_ID = "DEFAULT_USER_ID";

    private static final String DEFAULT_ROOM_ID_1_2 = "DEFAULT_ROOM_ID_1_2";
    private static final String DEFAULT_CHAT_ID_1_2 = "DEFAULT_CHAT_ID_1_2";

    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";
    private static final Long DEFAULT_TIMESTAMP_LONG = 1000000000L;

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<List<Chat>> chatsLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(chatsLiveData).when(chatsRepositoryMock).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        chatsLiveData.setValue(getDefaultAllChats_1_2());

        getChatListByRoomIdUseCase = new GetChatListByRoomIdUseCase(chatsRepositoryMock);
    }

    @Test
    public void get_chats_list_by_room_id_from_firestore_live_data() {
        // WHEN
        List<Chat> chats = TestUtils.getValueForTesting(getChatListByRoomIdUseCase.getChatListByRoomId(DEFAULT_ROOM_ID_1_2));

        assertNotNull(chats);
        assertEquals(getDefaultAllChats_1_2(), chats);

        verify(chatsRepositoryMock, atLeastOnce()).getChatListByRoomId(DEFAULT_ROOM_ID_1_2);

        verifyNoMoreInteractions(chatsRepositoryMock);
    }

    //region ======================================== DEFAULT CHATS =========================================

    private List<Chat> getDefaultAllChats_1_2() {
        List<Chat> chats = new ArrayList<>();

        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));
        chats.add(new Chat(DEFAULT_CHAT_ID_1_2, WORKMATE_ID_VALUE, DEFAULT_MESSAGE, DEFAULT_TIMESTAMP_LONG));

        return chats;
    }

    //endregion
}