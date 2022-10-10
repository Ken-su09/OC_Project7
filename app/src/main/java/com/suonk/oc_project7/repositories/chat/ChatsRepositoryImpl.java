package com.suonk.oc_project7.repositories.chat;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ChatsRepositoryImpl implements ChatsRepository {

    @NonNull
    private final FirebaseFirestore firebaseFirestore;

    private static final String ALL_ROOMS = "all_rooms";
    private static final String ALL_CHATS = "all_chats";

    @Inject
    public ChatsRepositoryImpl(@NonNull FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    @Override
    public LiveData<List<Room>> getAllRoomsFromFirestoreLiveData() {
        final MutableLiveData<List<Room>> roomsLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(ALL_ROOMS)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            roomsLiveData.setValue(querySnapshot.toObjects(Room.class));
                        } catch (Exception e) {
                            Log.e("getChatDetails", "" + e);
                        }
                    }
                });

        return roomsLiveData;
    }

    @Override
    public LiveData<Room> getRoomByIdFromFirestoreLiveData(@NonNull List<String> ids) {
        final MutableLiveData<Room> roomLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(ALL_ROOMS)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            for (Room room : querySnapshot.toObjects(Room.class)) {
                                if (room.getWorkmateIds().equals(ids)) {
                                    roomLiveData.setValue(room);
                                    break;
                                } else {
                                    Collections.reverse(ids);
                                    if (room.getWorkmateIds().equals(ids)) {
                                        roomLiveData.setValue(room);
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e("getChatDetails", "" + e);
                        }
                    }
                });

        return roomLiveData;
    }

    @Override
    public LiveData<List<Chat>> getChatListByRoomId(@NonNull String id) {
        final MutableLiveData<List<Chat>> chatListLiveData = new MutableLiveData<>();

        firebaseFirestore.collection(ALL_ROOMS)
                .document(id)
                .collection(ALL_CHATS)
                .addSnapshotListener((querySnapshot, error) -> {
                    if (querySnapshot != null) {
                        try {
                            chatListLiveData.setValue(querySnapshot.toObjects(Chat.class));
                        } catch (Exception e) {
                            Log.e("getChatListByRoomId", "" + e);
                        }
                    }
                });

        return chatListLiveData;
    }

    @Override
    public void addNewRoomToFirestore(@NonNull String id, @NonNull Room room) {
        firebaseFirestore.collection(ALL_ROOMS)
                .document(id)
                .set(room);
    }

    @Override
    public void addNewChatToRoom(@NonNull String id, @NonNull Chat chat) {
        firebaseFirestore.collection(ALL_ROOMS)
                .document(id)
                .collection(ALL_CHATS)
                .add(chat);
    }
}
