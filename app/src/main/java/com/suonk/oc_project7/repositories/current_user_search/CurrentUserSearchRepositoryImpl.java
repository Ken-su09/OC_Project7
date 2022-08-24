package com.suonk.oc_project7.repositories.current_user_search;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.suonk.oc_project7.model.data.place_auto_complete.CustomSpannable;

import java.util.Locale;

import javax.inject.Inject;

public class CurrentUserSearchRepositoryImpl implements CurrentUserSearchRepository {

    private final MutableLiveData<CharSequence> currentUserSearchLiveData = new MutableLiveData<>();

    @Inject
    public CurrentUserSearchRepositoryImpl() {

    }

    @Override
    public void setCurrentUserSearch(CharSequence query) {
        currentUserSearchLiveData.setValue(query);
    }

    @Override
    public LiveData<CharSequence> getCurrentUserSearchLiveData() {
        return currentUserSearchLiveData;
    }
}
