package com.suonk.oc_project7.ui.chat.list.rooms;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ConversationsViewState {

    @NonNull
    private final String id;

    @NonNull
    private final String workmateName;

    @NonNull
    private final String pictureUrl;

    @NonNull
    private final String lastMessage;

    @NonNull
    private final String timestamp;

    public ConversationsViewState(
            @NonNull String id,
            @NonNull String workmateName,
            @NonNull String pictureUrl,
            @NonNull String lastMessage,
            @NonNull String timestamp
    ) {
        this.id = id;
        this.workmateName = workmateName;
        this.pictureUrl = pictureUrl;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getWorkmateName() {
        return workmateName;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }


    @NonNull
    public String getLastMessage() {
        return lastMessage;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationsViewState that = (ConversationsViewState) o;
        return id.equals(that.id) && workmateName.equals(that.workmateName) && pictureUrl.equals(that.pictureUrl) && lastMessage.equals(that.lastMessage) && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmateName, pictureUrl, lastMessage, timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "ConversationsViewState{" +
                "id='" + id + '\'' +
                ", workmateName='" + workmateName + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
