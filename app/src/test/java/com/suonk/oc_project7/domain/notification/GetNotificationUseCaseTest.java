package com.suonk.oc_project7.domain.notification;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.model.data.notification.NotificationEntity;
import com.suonk.oc_project7.model.data.user.CustomFirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.notification.NotificationRepository;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class GetNotificationUseCaseTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GetNotificationUseCase getNotificationUseCase;

    //region ================================================================= MOCK =================================================================

    @NonNull
    private final NotificationRepository notificationRepositoryMock = mock(NotificationRepository.class);
    @NonNull
    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepository.class);
    @NonNull
    private final UserRepository userRepository = mock(UserRepository.class);
    @NonNull
    private final Application application = mock(Application.class);
    @NonNull
    private final NotificationCallback mockCallback = mock(NotificationCallback.class);

    @NonNull
    Task<Workmate> workmateTask = mock(Task.class);
    @NonNull
    Task<List<Workmate>> workmatesListTask = mock(Task.class);


    //endregion

    //region ============================================================ DEFAULT VALUES ============================================================

    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";

    private static final String DEFAULT_ID = "DEFAULT_ID";
    private static final String DEFAULT_ID_2 = "DEFAULT_ID_2";
    private static final String DEFAULT_ID_3 = "DEFAULT_ID_3";
    private static final String DEFAULT_NAME = "DEFAULT_NAME";
    private static final String DEFAULT_NAME_2 = "DEFAULT_NAME_2";
    private static final String DEFAULT_NAME_3 = "DEFAULT_NAME_3";
    private static final String DEFAULT_MAIL = "DEFAULT_MAIL";
    private static final String DEFAULT_MAIL_2 = "DEFAULT_MAIL_2";
    private static final String DEFAULT_MAIL_3 = "DEFAULT_MAIL_3";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String DEFAULT_RESTAURANT_ID = "DEFAULT_RESTAURANT_ID";
    private static final String DEFAULT_RESTAURANT_NAME = "DEFAULT_RESTAURANT_NAME";

    private static final String DEFAULT_RESTAURANT_ID_EMPTY = "";
    private static final String DEFAULT_RESTAURANT_NAME_EMPTY = "";

    private static final String NAME_THAT_JOINING = DEFAULT_NAME_2 + ", " + DEFAULT_NAME_3;
    private static final String DEFAULT_JOINING = DEFAULT_NAME_2 + ", " + DEFAULT_NAME_3 + " are joining you";

    private static final String NAME_THAT_JOINING_2 = DEFAULT_NAME_2;
    private static final String DEFAULT_JOINING_2 = DEFAULT_NAME_2 + " is joining you";
    private static final String NO_ONE_IS_JOINING = "No one is joining you";

    //endregion

    @Before
    public void setup() {
        getNotificationUseCase = new GetNotificationUseCase(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_returns_false() {
        // GIVEN
        doReturn(false).when(notificationRepositoryMock).getNotificationEnabled();

        // WHEN
        getNotificationUseCase.invoke(new NotificationCallback() {
            @Override
            public void onNotificationRetrieved(NotificationEntity notificationEntity) {

            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("Exception", "" + exception);
            }
        });

        // THEN
        verify(notificationRepositoryMock).getNotificationEnabled();
        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_returns_true_and_custom_firebase_user_null() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(null).when(userRepository).getCustomFirebaseUser();

        // WHEN
        getNotificationUseCase.invoke(new NotificationCallback() {
            @Override
            public void onNotificationRetrieved(NotificationEntity notificationEntity) {

            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("Exception", "" + exception);
            }
        });

        // THEN
        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();
        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_returns_true_and_workmate_task_returns_false() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(false).when(workmateTask).isSuccessful();

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_and_workmate_task_returns_true_but_workmates_list_task_returns_false() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        doReturn(workmatesListTask).when(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmateTask).isSuccessful();
        ArgumentCaptor<OnCompleteListener<List<Workmate>>> workmatesCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(false).when(workmatesListTask).isSuccessful();

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        verify(workmatesListTask).addOnCompleteListener(workmatesCompleteListenerCaptor.capture());
        workmatesCompleteListenerCaptor.getValue().onComplete(workmatesListTask);
        verify(workmatesListTask).isSuccessful();

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_and_workmate_task_workmates_list_task_returns_true_but_get_result_returns_null() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        doReturn(workmatesListTask).when(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmateTask).isSuccessful();
        doReturn(null).when(workmateTask).getResult();
        ArgumentCaptor<OnCompleteListener<List<Workmate>>> workmatesCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmatesListTask).isSuccessful();

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        verify(workmatesListTask).addOnCompleteListener(workmatesCompleteListenerCaptor.capture());
        workmatesCompleteListenerCaptor.getValue().onComplete(workmatesListTask);
        verify(workmatesListTask).isSuccessful();

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_and_workmate_task_workmates_list_task_returns_true_get_result_returns_workmate_with_restaurant_data_empty() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        doReturn(workmatesListTask).when(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmateTask).isSuccessful();
        doReturn(getDefaultWorkmateWithEmptyRestaurantData()).when(workmateTask).getResult();
        ArgumentCaptor<OnCompleteListener<List<Workmate>>> workmatesCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmatesListTask).isSuccessful();

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        verify(workmatesListTask).addOnCompleteListener(workmatesCompleteListenerCaptor.capture());
        workmatesCompleteListenerCaptor.getValue().onComplete(workmatesListTask);
        verify(workmatesListTask).isSuccessful();

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_and_workmate_task_workmates_list_task_returns_true_get_result_returns_default_workmate_but_list_of_workmates_null() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        doReturn(workmatesListTask).when(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmateTask).isSuccessful();
        doReturn(getDefaultWorkmate()).when(workmateTask).getResult();
        ArgumentCaptor<OnCompleteListener<List<Workmate>>> workmatesCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmatesListTask).isSuccessful();
        doReturn(null).when(workmatesListTask).getResult();

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        verify(workmatesListTask).addOnCompleteListener(workmatesCompleteListenerCaptor.capture());
        workmatesCompleteListenerCaptor.getValue().onComplete(workmatesListTask);
        verify(workmatesListTask).isSuccessful();

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_and_workmate_task_workmates_list_task_returns_true_get_result_returns_default_workmate_but_list_of_workmates_empty() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        doReturn(workmatesListTask).when(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        doReturn(NO_ONE_IS_JOINING).when(application).getString(R.string.no_one_is_joining_you);

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmateTask).isSuccessful();
        doReturn(getDefaultWorkmate()).when(workmateTask).getResult();
        ArgumentCaptor<OnCompleteListener<List<Workmate>>> workmatesCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmatesListTask).isSuccessful();
        doReturn(getWorkmatesListEmpty()).when(workmatesListTask).getResult();

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        verify(workmatesListTask).addOnCompleteListener(workmatesCompleteListenerCaptor.capture());
        workmatesCompleteListenerCaptor.getValue().onComplete(workmatesListTask);
        verify(workmatesListTask).isSuccessful();
        verify(application).getString(R.string.no_one_is_joining_you);

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_and_workmate_task_workmates_list_task_returns_true_get_result_returns_default_workmate_and_list_of_workmates_size_below_two() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        doReturn(workmatesListTask).when(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        doReturn(DEFAULT_JOINING_2).when(application).getString(R.string.is_joining_you, NAME_THAT_JOINING_2);

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmateTask).isSuccessful();
        doReturn(getDefaultWorkmate()).when(workmateTask).getResult();
        ArgumentCaptor<OnCompleteListener<List<Workmate>>> workmatesCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmatesListTask).isSuccessful();
        doReturn(getWorkmatesListSizeBelowTwo()).when(workmatesListTask).getResult();
        ArgumentCaptor<NotificationEntity> notificationEntityCaptor = ArgumentCaptor.forClass(NotificationEntity.class);

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        verify(workmatesListTask).addOnCompleteListener(workmatesCompleteListenerCaptor.capture());
        workmatesCompleteListenerCaptor.getValue().onComplete(workmatesListTask);
        verify(workmatesListTask).isSuccessful();

        verify(mockCallback).onNotificationRetrieved(notificationEntityCaptor.capture());

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();
        verify(application).getString(R.string.is_joining_you, NAME_THAT_JOINING_2);

        NotificationEntity notificationEntity = notificationEntityCaptor.getValue();
        assertNotNull(notificationEntity);

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    @Test
    public void get_notification_enabled_and_workmate_task_workmates_list_task_returns_true_get_result_returns_default_workmate_and_list_of_workmates() {
        // GIVEN
        doReturn(true).when(notificationRepositoryMock).getNotificationEnabled();
        doReturn(getCustomFirebaseUser()).when(userRepository).getCustomFirebaseUser();
        doReturn(workmateTask).when(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        doReturn(workmatesListTask).when(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        doReturn(DEFAULT_JOINING).when(application).getString(R.string.are_joining_you, NAME_THAT_JOINING);

        ArgumentCaptor<OnCompleteListener<Workmate>> workmateCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmateTask).isSuccessful();
        doReturn(getDefaultWorkmate()).when(workmateTask).getResult();
        ArgumentCaptor<OnCompleteListener<List<Workmate>>> workmatesCompleteListenerCaptor = ArgumentCaptor.forClass(OnCompleteListener.class);
        doReturn(true).when(workmatesListTask).isSuccessful();
        doReturn(getWorkmatesList()).when(workmatesListTask).getResult();
        ArgumentCaptor<NotificationEntity> notificationEntityCaptor = ArgumentCaptor.forClass(NotificationEntity.class);

        // WHEN
        getNotificationUseCase.invoke(mockCallback);

        // THEN
        verify(workmatesRepositoryMock).getCurrentUserWhoHasChosenTodayFromFirestore(DEFAULT_ID);
        verify(workmateTask).addOnCompleteListener(workmateCompleteListenerCaptor.capture());
        workmateCompleteListenerCaptor.getValue().onComplete(workmateTask);
        verify(workmateTask).isSuccessful();

        verify(workmatesRepositoryMock).getAllWorkmatesThatHaveChosenToday();
        verify(workmatesListTask).addOnCompleteListener(workmatesCompleteListenerCaptor.capture());
        workmatesCompleteListenerCaptor.getValue().onComplete(workmatesListTask);
        verify(workmatesListTask).isSuccessful();

        verify(mockCallback).onNotificationRetrieved(notificationEntityCaptor.capture());

        verify(notificationRepositoryMock).getNotificationEnabled();
        verify(userRepository, atLeastOnce()).getCustomFirebaseUser();
        verify(application).getString(R.string.are_joining_you, NAME_THAT_JOINING);

        NotificationEntity notificationEntity = notificationEntityCaptor.getValue();
        assertNotNull(notificationEntity);

        verifyNoMoreInteractions(application, userRepository, workmatesRepositoryMock, notificationRepositoryMock);
    }

    private CustomFirebaseUser getCustomFirebaseUser() {
        return new CustomFirebaseUser(DEFAULT_ID, DEFAULT_NAME, DEFAULT_MAIL, PICTURE_URL);
    }

    private List<Workmate> getWorkmatesList() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultWorkmate());
        workmates.add(new Workmate(DEFAULT_ID_2, DEFAULT_NAME_2, DEFAULT_MAIL_2, PICTURE_URL, DEFAULT_RESTAURANT_ID, DEFAULT_RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate(DEFAULT_ID_3, DEFAULT_NAME_3, DEFAULT_MAIL_3, PICTURE_URL, DEFAULT_RESTAURANT_ID, DEFAULT_RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }

    private List<Workmate> getWorkmatesListSizeBelowTwo() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultWorkmate());
        workmates.add(new Workmate(DEFAULT_ID_2, DEFAULT_NAME_2, DEFAULT_MAIL_2, PICTURE_URL, DEFAULT_RESTAURANT_ID, DEFAULT_RESTAURANT_NAME, new ArrayList<>()));

        return workmates;
    }

    private List<Workmate> getWorkmatesListEmpty() {
        return new ArrayList<>();
    }

    private Workmate getDefaultWorkmate() {
        return new Workmate(DEFAULT_ID, DEFAULT_NAME, DEFAULT_MAIL, PICTURE_URL, DEFAULT_RESTAURANT_ID, DEFAULT_RESTAURANT_NAME, new ArrayList<>());
    }

    private Workmate getDefaultWorkmateWithEmptyRestaurantData() {
        return new Workmate(DEFAULT_ID, DEFAULT_NAME, DEFAULT_MAIL, PICTURE_URL, DEFAULT_RESTAURANT_ID_EMPTY, DEFAULT_RESTAURANT_NAME_EMPTY, new ArrayList<>());
    }
}