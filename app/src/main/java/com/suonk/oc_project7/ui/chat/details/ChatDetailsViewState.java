package com.suonk.oc_project7.ui.chat.details;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ChatDetailsViewState {

    @NonNull
    private final String id;

    private final int textColor;

    private final int backgroundColor;

    @NonNull
    private final String content;

    @NonNull
    private final String timestamp;

    private final boolean isSendByMe;

    @NonNull
    private final String pictureUrl;

    public ChatDetailsViewState(
            @NonNull String id,
            int textColor,
            int backgroundColor,
            @NonNull String content,
            @NonNull String timestamp,
            boolean isSendByMe,
            @NonNull String pictureUrl
    ) {
        this.id = id;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.content = content;
        this.timestamp = timestamp;
        this.isSendByMe = isSendByMe;
        this.pictureUrl = pictureUrl;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    @NonNull
    public String getTimestamp() {
        return timestamp;
    }

    public boolean getIsSendByMe() {
        return isSendByMe;
    }

    @NonNull
    public String getPictureUrl() {
        return pictureUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatDetailsViewState that = (ChatDetailsViewState) o;
        return textColor == that.textColor && backgroundColor == that.backgroundColor && isSendByMe == that.isSendByMe && id.equals(that.id) && content.equals(that.content) && timestamp.equals(that.timestamp) && pictureUrl.equals(that.pictureUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, textColor, backgroundColor, content, timestamp, isSendByMe, pictureUrl);
    }

    @NonNull
    @Override
    public String toString() {
        return "ChatDetailsViewState{" +
                "id='" + id + '\'' +
                ", textColor=" + textColor +
                ", backgroundColor=" + backgroundColor +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", isSendByMe=" + isSendByMe +
                ", pictureUrl='" + pictureUrl + '\'' +
                '}';
    }
}