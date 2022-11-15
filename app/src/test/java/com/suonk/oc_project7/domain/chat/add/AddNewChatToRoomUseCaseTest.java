package com.suonk.oc_project7.domain.chat.add;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class AddNewChatToRoomUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private AddNewChatToRoomUseCase addNewChatToRoomUseCase;

    //region ============================================= MOCK =============================================

    @NonNull
    private final ChatsRepository chatsRepositoryMock = mock(ChatsRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String DEFAULT_USER_ID = "DEFAULT_USER_ID";
    private static final String WORKMATE_ID_1 = "WORKMATE_ID_1";

    private static final String DEFAULT_ROOM_ID = "DEFAULT_ROOM_ID";

    private static final String DEFAULT_MESSAGE = "DEFAULT_MESSAGE";

    private static final List<String> DEFAULT_WORKMATE_IDS_1_2 = Arrays.asList(
            DEFAULT_USER_ID,
            WORKMATE_ID_1);


    //endregion

    @Before
    public void setup() {
        doNothing().when(chatsRepositoryMock).addNewRoomToFirestore(new Room(DEFAULT_ROOM_ID, DEFAULT_WORKMATE_IDS_1_2));
        doNothing().when(chatsRepositoryMock).addNewChatToRoom(DEFAULT_ROOM_ID, DEFAULT_USER_ID, DEFAULT_MESSAGE);

        addNewChatToRoomUseCase = new AddNewChatToRoomUseCase(chatsRepositoryMock);
    }

    @Test
    public void add_new_chat_to_room() {
        addNewChatToRoomUseCase.addNewChatToRoom(DEFAULT_ROOM_ID, DEFAULT_WORKMATE_IDS_1_2, DEFAULT_USER_ID, DEFAULT_MESSAGE);

        verify(chatsRepositoryMock, atLeastOnce()).addNewRoomToFirestore(new Room(DEFAULT_ROOM_ID, DEFAULT_WORKMATE_IDS_1_2));
        verify(chatsRepositoryMock, atLeastOnce()).addNewChatToRoom(DEFAULT_ROOM_ID, DEFAULT_USER_ID, DEFAULT_MESSAGE);

        verifyNoMoreInteractions(chatsRepositoryMock);
    }
}