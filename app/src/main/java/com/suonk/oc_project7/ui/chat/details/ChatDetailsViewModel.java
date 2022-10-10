package com.suonk.oc_project7.ui.chat.details;

import static com.suonk.oc_project7.ui.chat.details.ChatDetailsActivity.WORKMATE_ID;

import android.annotation.SuppressLint;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.suonk.oc_project7.R;
import com.suonk.oc_project7.domain.chat.add.AddNewChatToRoomUseCase;
import com.suonk.oc_project7.domain.chat.add.AddNewRoomToFirestoreUseCase;
import com.suonk.oc_project7.domain.chat.get.GetChatListByRoomIdUseCase;
import com.suonk.oc_project7.domain.chat.get.GetRoomByIdFromFirestoreUseCase;
import com.suonk.oc_project7.domain.workmates.get.GetWorkmateByIdUseCase;
import com.suonk.oc_project7.model.data.chat.Chat;
import com.suonk.oc_project7.model.data.chat.Room;
import com.suonk.oc_project7.model.data.workmate.Workmate;
import com.suonk.oc_project7.repositories.user.UserRepository;
import com.suonk.oc_project7.utils.SingleLiveEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ChatDetailsViewModel extends ViewModel {

    private Workmate currentUser;

    private Workmate currentWorkmate;

    private final String workmateId;

    private String roomId;

    private final ArrayList<String> ids;

    @NonNull
    private final AddNewChatToRoomUseCase addNewChatToRoomUseCase;

    @NonNull
    private final AddNewRoomToFirestoreUseCase addNewRoomToFirestoreUseCase;

    @NonNull
    private final MediatorLiveData<List<ChatDetailsViewState>> viewStateLiveData = new MediatorLiveData<>();

    @NonNull
    private final SingleLiveEvent<Boolean> isMessageEmpty = new SingleLiveEvent<>();

    @Inject
    public ChatDetailsViewModel(
            @NonNull AddNewChatToRoomUseCase addNewChatToRoomUseCase,
            @NonNull AddNewRoomToFirestoreUseCase addNewRoomToFirestoreUseCase,
            @NonNull GetRoomByIdFromFirestoreUseCase getRoomByIdFromFirestoreUseCase,
            @NonNull GetChatListByRoomIdUseCase getChatListByRoomIdUseCase,
            @NonNull GetWorkmateByIdUseCase getWorkmateByIdUseCase,
            @NonNull SavedStateHandle savedStateHandle,
            @NonNull UserRepository userRepository) {
        this.addNewChatToRoomUseCase = addNewChatToRoomUseCase;
        this.addNewRoomToFirestoreUseCase = addNewRoomToFirestoreUseCase;
        workmateId = savedStateHandle.get(WORKMATE_ID);

        ids = new ArrayList<>();

        LiveData<Workmate> currentWorkmateLiveData;
        if (workmateId != null) {
            ids.add(workmateId);
            currentWorkmateLiveData = getWorkmateByIdUseCase.getWorkmateByIdLiveData(workmateId);
        } else {
            currentWorkmateLiveData = new MutableLiveData<>();
        }

        LiveData<Workmate> currentUserLiveData;
        if (userRepository.getCustomFirebaseUser() != null) {
            ids.add(userRepository.getCustomFirebaseUser().getId());
            currentUserLiveData = getWorkmateByIdUseCase.getWorkmateByIdLiveData(
                    userRepository.getCustomFirebaseUser().getId());
        } else {
            currentUserLiveData = new MutableLiveData<>();
        }

        LiveData<Room> roomLiveData = getRoomByIdFromFirestoreUseCase.getRoomByIdFromFirestore(ids);

        LiveData<List<Chat>> chatsLiveData = Transformations.switchMap(roomLiveData, room -> {
            roomId = room.getId();
            return getChatListByRoomIdUseCase.getChatListByRoomId(room.getId());
        });

        viewStateLiveData.addSource(chatsLiveData, chats -> combine(chats, currentUserLiveData.getValue(), currentWorkmateLiveData.getValue()));
        viewStateLiveData.addSource(currentUserLiveData, user -> combine(chatsLiveData.getValue(), user, currentWorkmateLiveData.getValue()));
        viewStateLiveData.addSource(currentWorkmateLiveData, workmate -> combine(chatsLiveData.getValue(), currentUserLiveData.getValue(), workmate));
    }

    private void combine(@Nullable List<Chat> chats, @Nullable Workmate user, @Nullable Workmate workmate) {
        List<ChatDetailsViewState> chatDetailsViewStateList = new ArrayList<>();

        if (user != null) {
            currentUser = user;
        }

        if (workmate != null) {
            currentWorkmate = workmate;
        }

        if (chats != null && user != null) {
            Collections.sort(chats, Comparator.comparing(Chat::getTimestamp));

            for (Chat chat : chats) {
                int textColor = Color.WHITE;
                int backgroundColor = R.drawable.custom_message_background_sender;

                if (chat.getSenderId().equals(workmateId)) {
                    textColor = Color.BLACK;
                    backgroundColor = R.drawable.custom_message_background_receiver;
                }

                @SuppressLint("SimpleDateFormat")
                String timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(chat.getTimestamp()));

                chatDetailsViewStateList.add(new ChatDetailsViewState(
                        chat.getId(),
                        "",
                        "",
                        textColor,
                        backgroundColor,
                        chat.getMessage(),
                        timestamp
                ));
            }
        }

        viewStateLiveData.setValue(chatDetailsViewStateList);
    }

    public LiveData<List<ChatDetailsViewState>> getChatDetails() {
        return viewStateLiveData;
    }

    public void addMessage(String message) {
        if (message.equals("") || message.isEmpty()) {
            isMessageEmpty.setValue(true);
        } else {
            isMessageEmpty.setValue(false);

            if (roomId == null) {
                roomId = UUID.randomUUID().toString();
            }

            addNewRoomToFirestoreUseCase.addNewRoomToFirestore(roomId, new Room(
                    roomId,
                    ids,
                    new ArrayList<>(Arrays.asList(currentUser.getName(), currentWorkmate.getName())),
                    new ArrayList<>(Arrays.asList(currentUser.getPictureUrl(), currentWorkmate.getPictureUrl())),
                    message,
                    System.currentTimeMillis()));

            addNewChatToRoomUseCase.addNewChatToRoom(
                    roomId,
                    new Chat(
                            UUID.randomUUID().toString(),
                            currentUser.getId(),
                            message,
                            System.currentTimeMillis()
                    ));
        }
    }

    @NonNull
    public SingleLiveEvent<Boolean> getIsMessageEmpty() {
        return isMessageEmpty;
    }
}