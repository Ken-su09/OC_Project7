package com.suonk.oc_project7.ui.chat.list;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ChatsListViewState {

    @NonNull
    private final String id;

    @NonNull
    private final String workmateNames;

    @NonNull
    private final String pictureUrl;

    @NonNull
    private final String lastMessage;

    @NonNull
    private final String timestamp;

    @NonNull
    private final String workmateId;

    public ChatsListViewState(@NonNull String id,
                              @NonNull String workmateNames,
                              @NonNull String pictureUrl,
                              @NonNull String lastMessage,
                              @NonNull String timestamp,
                              @NonNull String workmateId) {
        this.id = id;
        this.workmateNames = workmateNames;
        this.pictureUrl = pictureUrl;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.workmateId = workmateId;
    }


    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getWorkmateNames() {
        return workmateNames;
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

    @NonNull
    public String getWorkmateId() {
        return workmateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatsListViewState that = (ChatsListViewState) o;
        return id.equals(that.id) && workmateNames.equals(that.workmateNames) && pictureUrl.equals(that.pictureUrl) && lastMessage.equals(that.lastMessage) && timestamp.equals(that.timestamp) && workmateId.equals(that.workmateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmateNames, pictureUrl, lastMessage, timestamp, workmateId);
    }

    @NonNull
    @Override
    public String toString() {
        return "ChatsListViewState{" +
                "id='" + id + '\'' +
                ", workmateNames='" + workmateNames + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", lastMessage='" + lastMessage + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", workmateId='" + workmateId + '\'' +
                '}';
    }
}