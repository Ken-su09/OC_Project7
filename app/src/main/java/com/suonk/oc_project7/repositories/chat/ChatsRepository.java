package com.suonk.oc_project7.repositories.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;

import java.util.List;

public interface ChatsRepository {

    LiveData<List<Room>> getAllRoomsFromFirestoreLiveData();

    LiveData<Room> getRoomByIdFromFirestoreLiveData(@NonNull List<String> ids);

    LiveData<List<Chat>> getChatListByRoomId(@NonNull String id);

    void addNewRoomToFirestore(@NonNull String id, @NonNull Room room);

    void addNewChatToRoom(@NonNull String id, @NonNull Chat chat);
}
