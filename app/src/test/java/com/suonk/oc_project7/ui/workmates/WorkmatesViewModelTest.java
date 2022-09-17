package com.suonk.oc_project7.ui.workmates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.workmates.WorkmatesUseCases;
import com.suonk.oc_project7.domain.workmates.get.GetAllWorkmatesFromFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetCurrentUserUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmatesHaveChosenTodayUseCase;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.current_user_search.CurrentUserSearchRepository;
import com.suonk.oc_project7.utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class WorkmatesViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    //region ============================================= MOCK =============================================

    private final WorkmatesUseCases workmatesUseCasesMock = mock(WorkmatesUseCases.class);
    private final GetCurrentUserUseCase getCurrentUserUseCaseMock = mock(GetCurrentUserUseCase.class);
    private final GetAllWorkmatesFromFirestoreUseCase getAllWorkmatesFromFirestoreUseCaseMock = mock(GetAllWorkmatesFromFirestoreUseCase.class);
    private final GetWorkmatesHaveChosenTodayUseCase getWorkmatesHaveChosenTodayUseCaseMock = mock(GetWorkmatesHaveChosenTodayUseCase.class);

    private final FirebaseAuth auth = mock(FirebaseAuth.class);
    private final FirebaseUser firebaseUser = mock(FirebaseUser.class);
    private final CurrentUserSearchRepository currentUserSearchRepository = Mockito.mock(CurrentUserSearchRepository.class);
    private final Application application = Mockito.mock(Application.class);

    //endregion

    //region ======================================== DEFAULTS VALUES =======================================

    private static final String RESTAURANT_NAME = "PIZZA HUT";
    private static final String RESTAURANT_NAME_1 = "PIZZA N PASTA";
    private static final String RESTAURANT_NAME_2 = "PASTA";

    private static final int TEXT_COLOR_HAS_DECIDED = -16777216;
    private static final int TEXT_STYLE_HAS_DECIDED = 0;
    private static final int TEXT_COLOR_HAS_NOT_DECIDED = -7829368;
    private static final int TEXT_STYLE_HAS_NOT_DECIDED = 2;
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;

    private static final String UID = "UID";
    private static final String U_NAME = "U_NAME";
    private static final String EMAIL = "EMAIL";

    private static final String TEXT_TO_HIGHLIGHT = "PIZ";

    private static final String WORKMATE_HAS_CHOSEN = "WORKMATE_HAS_CHOSEN";
    private static final String WORKMATE_HAS_NOT_CHOSEN_YET = "WORKMATE_HAS_NOT_CHOSEN_YET";

    private static final String TEXT_WORKMATE_HAS_CHOSEN = WORKMATE_HAS_CHOSEN + " has chosen " + RESTAURANT_NAME;
    private static final String TEXT_WORKMATE_HAS_CHOSEN_1 = WORKMATE_HAS_CHOSEN + " has chosen " + RESTAURANT_NAME_1;
    private static final String TEXT_WORKMATE_HAS_CHOSEN_2 = WORKMATE_HAS_CHOSEN + " has chosen " + RESTAURANT_NAME_2;

    private static final String TEXT_WORKMATE_HAS_NOT_CHOSEN_YET = " has not chosen yet";

    //endregion

    //region =========================================== LIVEDATA ===========================================

    private final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmatesHaveChosenMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<CharSequence> currentUserSearchLiveData = new MutableLiveData<>();
    private final MutableLiveData<Workmate> currentUserLiveData = new MutableLiveData<>();

    //endregion

    @Before
    public void setup() {
        doReturn(getAllWorkmatesFromFirestoreUseCaseMock).when(workmatesUseCasesMock).getGetAllWorkmatesFromFirestoreUseCase();
        doReturn(getWorkmatesHaveChosenTodayUseCaseMock).when(workmatesUseCasesMock).getGetWorkmatesHaveChosenTodayUseCase();
        doReturn(getCurrentUserUseCaseMock).when(workmatesUseCasesMock).getGetCurrentUserUseCase();

        doReturn(workmatesMutableLiveData).when(getAllWorkmatesFromFirestoreUseCaseMock).getAllWorkmatesFromFirestoreLiveData();
        doReturn(workmatesHaveChosenMutableLiveData).when(getWorkmatesHaveChosenTodayUseCaseMock).getWorkmatesHaveChosenTodayLiveData();
        doReturn(currentUserSearchLiveData).when(currentUserSearchRepository).getCurrentUserSearchLiveData();

        doReturn(firebaseUser).when(auth).getCurrentUser();
        doReturn(UID).when(firebaseUser).getUid();
        doReturn(currentUserLiveData).when(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);

        doReturn(TEXT_WORKMATE_HAS_CHOSEN).when(application).getString(R.string.has_chosen,
                WORKMATE_HAS_CHOSEN, RESTAURANT_NAME);
        doReturn(TEXT_WORKMATE_HAS_CHOSEN_1).when(application).getString(R.string.has_chosen,
                WORKMATE_HAS_CHOSEN, RESTAURANT_NAME_1);
        doReturn(TEXT_WORKMATE_HAS_CHOSEN_2).when(application).getString(R.string.has_chosen,
                WORKMATE_HAS_CHOSEN, RESTAURANT_NAME_2);

        doReturn(TEXT_WORKMATE_HAS_NOT_CHOSEN_YET).when(application).getString(R.string.has_not_chosen_yet,
                WORKMATE_HAS_NOT_CHOSEN_YET);

        workmatesMutableLiveData.setValue(getDefaultWorkmates());
        workmatesHaveChosenMutableLiveData.setValue(getDefaultWorkmatesHaveChosen());
        currentUserSearchLiveData.setValue(TEXT_TO_HIGHLIGHT);
        currentUserLiveData.setValue(getDefaultCurrentUser());
    }

    @After
    public void tearDown() {
        verify(getAllWorkmatesFromFirestoreUseCaseMock).getAllWorkmatesFromFirestoreLiveData();
        verify(getWorkmatesHaveChosenTodayUseCaseMock).getWorkmatesHaveChosenTodayLiveData();
        verify(currentUserSearchRepository).getCurrentUserSearchLiveData();
        verify(auth, atLeastOnce()).getCurrentUser();

        verify(workmatesUseCasesMock).getGetAllWorkmatesFromFirestoreUseCase();
        verify(workmatesUseCasesMock).getGetWorkmatesHaveChosenTodayUseCase();

        Mockito.verifyNoMoreInteractions(workmatesUseCasesMock, currentUserSearchRepository, auth);
    }

    @Test
    public void get_all_workmates_from_firestore_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(7, allWorkmatesViewState.size());
        assertEquals(getDefaultWorkmatesItemViewState(), allWorkmatesViewState);


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_with_search() {
        // GIVEN
        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(3, allWorkmatesViewState.size());
        assertEquals(getDefaultWorkmatesItemViewStateWithSearch(), allWorkmatesViewState);


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(7, allWorkmatesViewState.size());
        assertEquals(getDefaultWorkmatesItemViewState(), allWorkmatesViewState);


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_both_lists_are_null_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        workmatesHaveChosenMutableLiveData.setValue(null);
        workmatesMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_both_lists_are_null_with_search() {
        // GIVEN
        workmatesHaveChosenMutableLiveData.setValue(null);
        workmatesMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_both_lists_are_null_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);
        workmatesHaveChosenMutableLiveData.setValue(null);
        workmatesMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_all_workmates_are_null_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        workmatesMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_all_workmates_are_null_with_search() {
        // GIVEN
        workmatesMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_all_workmates_are_null_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);
        workmatesMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_workmates_have_chosen_are_null_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        workmatesHaveChosenMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_workmates_have_chosen_are_null_with_search() {
        // GIVEN
        workmatesHaveChosenMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_workmates_have_chosen_are_null_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);
        workmatesHaveChosenMutableLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_list_is_empty_with_search_empty() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        workmatesHaveChosenMutableLiveData.setValue(new ArrayList<>());
        workmatesMutableLiveData.setValue(new ArrayList<>());

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_list_is_empty_with_search() {
        // GIVEN
        workmatesHaveChosenMutableLiveData.setValue(new ArrayList<>());
        workmatesMutableLiveData.setValue(new ArrayList<>());

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_if_list_is_empty_with_search_null() {
        // GIVEN
        currentUserSearchLiveData.setValue(null);
        workmatesHaveChosenMutableLiveData.setValue(new ArrayList<>());
        workmatesMutableLiveData.setValue(new ArrayList<>());

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_with_search_empty_and_current_user_live_data_null() {
        // GIVEN
        currentUserSearchLiveData.setValue("");
        currentUserLiveData.setValue(null);

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(8, allWorkmatesViewState.size());


        verify(workmatesUseCasesMock).getGetCurrentUserUseCase();
        verify(getCurrentUserUseCaseMock).getCurrentUserByIdLiveData(UID);
        verify(firebaseUser).getUid();
    }

    @Test
    public void get_all_workmates_from_firestore_with_search_empty_and_firebase_user_null() {
        // GIVEN
        doReturn(null).when(auth).getCurrentUser();
        currentUserSearchLiveData.setValue("");

        WorkmatesViewModel workmatesViewModel = new WorkmatesViewModel(
                workmatesUseCasesMock,
                currentUserSearchRepository,
                auth,
                application);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(8, allWorkmatesViewState.size());
    }

    //region ======================================= GET DEFAULT USER =======================================

    private Workmate getDefaultCurrentUser() {
        return new Workmate(UID, U_NAME, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>());
    }

    //endregion

    private List<Workmate> getDefaultWorkmates() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultCurrentUser());
        workmates.add(new Workmate("2", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("3", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("4", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_1, new ArrayList<>()));
        workmates.add(new Workmate("5", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_2, new ArrayList<>()));
        workmates.add(new Workmate("6", WORKMATE_HAS_NOT_CHOSEN_YET, EMAIL, PICTURE_URL, "1", "", new ArrayList<>()));
        workmates.add(new Workmate("7", WORKMATE_HAS_NOT_CHOSEN_YET, EMAIL, PICTURE_URL, "1", "", new ArrayList<>()));
        workmates.add(new Workmate("8", WORKMATE_HAS_NOT_CHOSEN_YET, EMAIL, PICTURE_URL, "1", "", new ArrayList<>()));


        return workmates;
    }

    private List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(getDefaultCurrentUser());
        workmates.add(new Workmate("2", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("3", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME, new ArrayList<>()));
        workmates.add(new Workmate("4", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_1, new ArrayList<>()));
        workmates.add(new Workmate("5", WORKMATE_HAS_CHOSEN, EMAIL, PICTURE_URL, "1", RESTAURANT_NAME_2, new ArrayList<>()));

        return workmates;
    }

    private List<WorkmateItemViewState> getDefaultWorkmatesItemViewState() {
        List<WorkmateItemViewState> workmates = new ArrayList<>();

        workmates.add(new WorkmateItemViewState("2", TEXT_WORKMATE_HAS_CHOSEN, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("3", TEXT_WORKMATE_HAS_CHOSEN, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("4", TEXT_WORKMATE_HAS_CHOSEN_1, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("5", TEXT_WORKMATE_HAS_CHOSEN_2, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));

        workmates.add(new WorkmateItemViewState("6", TEXT_WORKMATE_HAS_NOT_CHOSEN_YET, PICTURE_URL, TEXT_COLOR_HAS_NOT_DECIDED, TEXT_STYLE_HAS_NOT_DECIDED));
        workmates.add(new WorkmateItemViewState("7", TEXT_WORKMATE_HAS_NOT_CHOSEN_YET, PICTURE_URL, TEXT_COLOR_HAS_NOT_DECIDED, TEXT_STYLE_HAS_NOT_DECIDED));
        workmates.add(new WorkmateItemViewState("8", TEXT_WORKMATE_HAS_NOT_CHOSEN_YET, PICTURE_URL, TEXT_COLOR_HAS_NOT_DECIDED, TEXT_STYLE_HAS_NOT_DECIDED));

        return workmates;
    }

    private List<WorkmateItemViewState> getDefaultWorkmatesItemViewStateWithSearch() {
        List<WorkmateItemViewState> workmates = new ArrayList<>();

        workmates.add(new WorkmateItemViewState("2", TEXT_WORKMATE_HAS_CHOSEN, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("3", TEXT_WORKMATE_HAS_CHOSEN, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("4", TEXT_WORKMATE_HAS_CHOSEN_1, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));

        return workmates;
    }
}