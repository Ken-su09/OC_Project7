package com.suonk.oc_project7.ui.workmates;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepository;
import com.suonk.oc_project7.repositories.workmates.WorkmatesRepositoryImpl;
import com.suonk.oc_project7.utils.TestUtils;

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

    private WorkmatesViewModel workmatesViewModel;

    private final WorkmatesRepository workmatesRepositoryMock = mock(WorkmatesRepositoryImpl.class);
    private final FirebaseAuth auth = mock(FirebaseAuth.class);
    private final FirebaseUser firebaseUser = mock(FirebaseUser.class);

    private static final String TEXT_WORKMATE_HAS_DECIDED = " has decided";
    private static final String TEXT_WORKMATE_HAS_NOT_DECIDED = " hasn't decided yet";
    private static final String EMAIL = "EMAIL";
    private static final int TEXT_COLOR_HAS_DECIDED = -16777216;
    private static final int TEXT_STYLE_HAS_DECIDED = 0;
    private static final int TEXT_COLOR_HAS_NOT_DECIDED = -7829368;
    private static final int TEXT_STYLE_HAS_NOT_DECIDED = 2;
    private static final String PHOTO_REFERENCE = "PHOTO_REFERENCE";
    private static final String PICTURE_URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&key=key&photo_reference=" + PHOTO_REFERENCE;
    private static final String UID = "UID";

    private final MutableLiveData<List<Workmate>> workmatesMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Workmate>> workmatesHaveChosenMutableLiveData = new MutableLiveData<>();

    @Before
    public void setup() throws Exception {
        doReturn(workmatesMutableLiveData).when(workmatesRepositoryMock).getAllWorkmatesFromFirestoreLiveData();
        doReturn(workmatesHaveChosenMutableLiveData).when(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();
        doReturn(firebaseUser).when(auth).getCurrentUser();
        doReturn(UID).when(firebaseUser).getUid();

        workmatesViewModel = new WorkmatesViewModel(workmatesRepositoryMock, auth);

        workmatesHaveChosenMutableLiveData.setValue(getDefaultWorkmatesHaveChosen());
        workmatesMutableLiveData.setValue(getDefaultWorkmates());

        verify(workmatesRepositoryMock).getAllWorkmatesFromFirestoreLiveData();
        verify(workmatesRepositoryMock).getWorkmatesHaveChosenTodayLiveData();
        verify(auth).getCurrentUser();
    }

    @Test
    public void get_all_workmates_from_firestore_if_all_workmates_and_workmates_have_chosen_are_null() {
        // GIVEN
        workmatesHaveChosenMutableLiveData.setValue(null);
        workmatesMutableLiveData.setValue(null);

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());

        Mockito.verifyNoMoreInteractions(workmatesRepositoryMock, auth);
    }

    @Test
    public void get_all_workmates_from_firestore_if_list_is_empty() {
        // GIVEN
        workmatesHaveChosenMutableLiveData.setValue(new ArrayList<>());
        workmatesMutableLiveData.setValue(new ArrayList<>());

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(0, allWorkmatesViewState.size());

        Mockito.verifyNoMoreInteractions(workmatesRepositoryMock, auth);
    }

    @Test
    public void get_all_workmates_from_firestore() {
        // GIVEN

        // WHEN
        List<WorkmateItemViewState> allWorkmatesViewState = TestUtils.getValueForTesting(
                workmatesViewModel.getWorkmatesLiveData());

        // THEN
        assertNotNull(allWorkmatesViewState);
        assertEquals(6, allWorkmatesViewState.size());
        assertEquals(getDefaultWorkmatesItemViewState(), allWorkmatesViewState);

        Mockito.verifyNoMoreInteractions(workmatesRepositoryMock, auth);
    }

    private List<Workmate> getDefaultWorkmates() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(new Workmate("1", "workmate1", EMAIL, PICTURE_URL, "1"));
        workmates.add(new Workmate("2", "workmate2", EMAIL, PICTURE_URL, "1"));
        workmates.add(new Workmate("3", "workmate3", EMAIL, PICTURE_URL, "1"));
        workmates.add(new Workmate("4", "workmate4", EMAIL, PICTURE_URL, "1"));
        workmates.add(new Workmate("5", "workmate5", EMAIL, PICTURE_URL, "1"));
        workmates.add(new Workmate("6", "workmate6", EMAIL, PICTURE_URL, "1"));

        return workmates;
    }

    private List<Workmate> getDefaultWorkmatesHaveChosen() {
        List<Workmate> workmates = new ArrayList<>();

        workmates.add(new Workmate("1", "workmate1", EMAIL, PICTURE_URL, "1"));
        workmates.add(new Workmate("2", "workmate2", EMAIL, PICTURE_URL, "1"));
        workmates.add(new Workmate("3", "workmate3", EMAIL, PICTURE_URL, "1"));

        return workmates;
    }

    private List<WorkmateItemViewState> getDefaultWorkmatesItemViewState() {
        List<WorkmateItemViewState> workmates = new ArrayList<>();

        workmates.add(new WorkmateItemViewState("1", "workmate1" + TEXT_WORKMATE_HAS_DECIDED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("2", "workmate2" + TEXT_WORKMATE_HAS_DECIDED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("3", "workmate3" + TEXT_WORKMATE_HAS_DECIDED, PICTURE_URL, TEXT_COLOR_HAS_DECIDED, TEXT_STYLE_HAS_DECIDED));
        workmates.add(new WorkmateItemViewState("4", "workmate4" + TEXT_WORKMATE_HAS_NOT_DECIDED, PICTURE_URL, TEXT_COLOR_HAS_NOT_DECIDED, TEXT_STYLE_HAS_NOT_DECIDED));
        workmates.add(new WorkmateItemViewState("5", "workmate5" + TEXT_WORKMATE_HAS_NOT_DECIDED, PICTURE_URL, TEXT_COLOR_HAS_NOT_DECIDED, TEXT_STYLE_HAS_NOT_DECIDED));
        workmates.add(new WorkmateItemViewState("6", "workmate6" + TEXT_WORKMATE_HAS_NOT_DECIDED, PICTURE_URL, TEXT_COLOR_HAS_NOT_DECIDED, TEXT_STYLE_HAS_NOT_DECIDED));

        return workmates;
    }
}