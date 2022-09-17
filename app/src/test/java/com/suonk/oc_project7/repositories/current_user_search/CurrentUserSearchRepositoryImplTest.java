package com.suonk.oc_project7.repositories.current_user_search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.suonk.oc_project7.utils.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CurrentUserSearchRepositoryImplTest {

    @Rule
    public final InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private static final String DEFAULT_QUERY = "DEFAULT_QUERY";

    private CurrentUserSearchRepository repository;

    @Before
    public void setup() {
        repository = new CurrentUserSearchRepositoryImpl();

        repository.setCurrentUserSearch(getDefaultQuery());
    }

    @Test
    public void get_current_user_live_data() {
        // GIVEN

        // WHEN
        CharSequence currentUserSearchLiveData = TestUtils.getValueForTesting(
                repository.getCurrentUserSearchLiveData());

        assertNotNull(currentUserSearchLiveData);
        assertEquals(getDefaultQuery(), currentUserSearchLiveData);
    }

    @Test
    public void get_current_user_live_data_if_query_is_null() {
        // GIVEN
        repository.setCurrentUserSearch(null);

        // WHEN
        CharSequence currentUserSearchLiveData = TestUtils.getValueForTesting(
                repository.getCurrentUserSearchLiveData());

        assertNotNull(currentUserSearchLiveData);
        assertEquals("", currentUserSearchLiveData);
    }

    @Test
    public void get_current_user_live_data_if_query_is_empty() {
        // GIVEN
        repository.setCurrentUserSearch("");

        // WHEN
        CharSequence currentUserSearchLiveData = TestUtils.getValueForTesting(
                repository.getCurrentUserSearchLiveData());

        assertNotNull(currentUserSearchLiveData);
        assertEquals("", currentUserSearchLiveData);
    }

    public CharSequence getDefaultQuery() {
        return DEFAULT_QUERY;
    }
}