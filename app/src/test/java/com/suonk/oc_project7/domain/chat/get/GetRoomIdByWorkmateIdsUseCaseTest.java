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

import com.suonk.oc_project7.repositories.chat.ChatsRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class GetRoomIdByWorkmateIdsUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GetRoomIdByWorkmateIdsUseCase getRoomIdByWorkmateIdsUseCase;

    //region ============================================= MOCK =============================================

    @NonNull
    private final ChatsRepository chatsRepositoryMock = mock(ChatsRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String WORKMATE_ID_1 = "WORKMATE_ID_1";
    private static final String WORKMATE_ID_2 = "WORKMATE_ID_2";

    private static final List<String> DEFAULT_WORKMATE_IDS_1_2 = Arrays.asList(
            WORKMATE_ID_1,
            WORKMATE_ID_2);

    private static final String DEFAULT_ROOM_ID = "DEFAULT_ROOM_ID";

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<String> roomIdLiveData = new MutableLiveData<>();

    //endregion


    @Before
    public void setup() {
        doReturn(roomIdLiveData).when(chatsRepositoryMock).getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2);

        roomIdLiveData.setValue(DEFAULT_ROOM_ID);

        getRoomIdByWorkmateIdsUseCase = new GetRoomIdByWorkmateIdsUseCase(chatsRepositoryMock);
    }

    @Test
    public void test_get_room_id_by_workmates_ids() {
        // WHEN
        String roomId = TestUtils.getValueForTesting(getRoomIdByWorkmateIdsUseCase.getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2));

        assertNotNull(roomId);
        assertEquals(DEFAULT_ROOM_ID, roomId);

        getRoomIdByWorkmateIdsUseCase.getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2);

        verify(chatsRepositoryMock, atLeastOnce()).getRoomIdByWorkmateIds(DEFAULT_WORKMATE_IDS_1_2);
        verifyNoMoreInteractions(chatsRepositoryMock);
    }
}