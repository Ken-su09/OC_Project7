package com.suonk.oc_project7.domain.chat.get;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GetRoomIdByWorkmateIdsUseCase {

    @NonNull
    private final ChatsRepository chatsRepository;

    @Inject
    public GetRoomIdByWorkmateIdsUseCase(@NonNull ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    public LiveData<String> getRoomIdByWorkmateIds(@NonNull List<String> ids) {
        return chatsRepository.getRoomIdByWorkmateIds(ids);
    }
}
