package com.suonk.oc_project7.domain.chat.add;

import androidx.annotation.NonNull;

import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.repositories.chat.ChatsRepository;

import javax.inject.Singleton;

@Singleton
public class AddNewChatToRoomUseCase {

    @NonNull
    private final ChatsRepository chatsRepository;

    public AddNewChatToRoomUseCase(@NonNull ChatsRepository chatsRepository) {
        this.chatsRepository = chatsRepository;
    }

    public void addNewChatToRoom(@NonNull String id, @NonNull Chat chat) {
        chatsRepository.addNewChatToRoom(id, chat);
    }
}
