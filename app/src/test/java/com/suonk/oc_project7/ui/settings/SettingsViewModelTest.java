package com.suonk.oc_project7.ui.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;
import com.suonk.oc_project7.repositories.notification.NotificationRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.ui.main.MainViewState;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private SettingsViewModel viewModel;

    //region ============================================= MOCK =============================================

    private final UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
    private final NotificationRepository notificationRepositoryMock = Mockito.mock(NotificationRepository.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";

    private static final String DEFAULT_ID = "DEFAULT_ID";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_MAIL = "DEFAULT_MAIL";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final Boolean DEFAULT_ENABLED = true;

    //endregion

    private final MutableLiveData<MainViewState> mainViewStateLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> notificationEnabledLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        doReturn(getCustomFirebaseUser()).when(userRepositoryMock).getCustomFirebaseUser();
        doReturn(DEFAULT_ENABLED).when(notificationRepositoryMock).getNotificationEnabled();

        mainViewStateLiveData.setValue(getDefaultMainViewState());
        notificationEnabledLiveData.setValue(DEFAULT_ENABLED);

        viewModel = new SettingsViewModel(userRepositoryMock, notificationRepositoryMock);

        verify(notificationRepositoryMock).getNotificationEnabled();
    }

    @Test
    public void get_main_view_state_live_data() {
        // GIVEN

        // WHEN
        MainViewState mainViewState = TestUtils.getValueForTesting(viewModel.getMainViewStateLiveData());

        // THEN
        assertNotNull(mainViewState);
        assertEquals(getDefaultMainViewState(), mainViewState);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();

        Mockito.verifyNoMoreInteractions(userRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_main_view_state_live_data_if_get_custom_firebase_user_null() {
        // GIVEN
        doReturn(null).when(userRepositoryMock).getCustomFirebaseUser();

        // WHEN
        MainViewState mainViewState = TestUtils.getValueForTesting(viewModel.getMainViewStateLiveData());

        // THEN
        assertNotNull(mainViewState);
        assertEquals(getDefaultMainViewStateEmpty(), mainViewState);

        verify(userRepositoryMock, atLeastOnce()).getCustomFirebaseUser();

        Mockito.verifyNoMoreInteractions(userRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_live_data() {
        // GIVEN

        // WHEN
        Boolean isNotificationEnabled = TestUtils.getValueForTesting(viewModel.getNotificationEnabled());

        // THEN
        assertNotNull(isNotificationEnabled);
        assertEquals(DEFAULT_ENABLED, isNotificationEnabled);

        Mockito.verifyNoMoreInteractions(userRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void set_notification_enabled() {
        // WHEN
        viewModel.setNotificationEnabled(false);

        // THEN
        verify(notificationRepositoryMock).setNotificationEnabled(false);

        Mockito.verifyNoMoreInteractions(userRepositoryMock, notificationRepositoryMock);
    }

    private MainViewState getDefaultMainViewState() {
        return new MainViewState(
                DEFAULT_NAME,
                DEFAULT_MAIL,
                PICTURE_URL
        );
    }

    private MainViewState getDefaultMainViewStateEmpty() {
        return new MainViewState(
                "",
                "",
                ""
        );
    }

    private CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(
                DEFAULT_ID,
                DEFAULT_NAME,
                DEFAULT_MAIL,
                PICTURE_URL
        );
    }
}