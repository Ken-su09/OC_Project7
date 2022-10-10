package com.suonk.oc_project7.domain.chat.get;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import java.util.List;

import javax.inject.Singleton;

@Singleton
public class GetChatListByRoomIdUseCase {

    @NonNull
    private final ChatsRepository chatsRepository;

    public GetChatListByRoomIdUseCase(@NonNull ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    public LiveData<List<Chat>> getChatListByRoomId(@NonNull String id) {
        return chatsRepository.getChatListByRoomId(id);
    }
}
