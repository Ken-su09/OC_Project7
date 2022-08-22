package com.suonk.oc_project7.repositories.current_user_search;

import androidx.lifecycle.LiveData;

public interface CurrentUserSearchRepository {

    void setCurrentUserSearch(CharSequence query);

    LiveData<CharSequence> getCurrentUserSearchLiveData();
}