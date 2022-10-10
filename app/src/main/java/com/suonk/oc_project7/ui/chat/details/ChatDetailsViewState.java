package com.suonk.oc_project7.ui.chat.details;

import androidx.annotation.NonNull;

import java.util.Objects;

public class ChatDetailsViewState {
    @NonNull
    private final String id;

    @NonNull
    private final String workmateName;

    @NonNull
    private final String pictureUrl;

    private final int textColor;

    private final int backgroundColor;

    @NonNull
    private final String content;

    @NonNull
    private final String timestamp;

    public ChatDetailsViewState(
            @NonNull String id,
            @NonNull String workmateName,
            @NonNull String pictureUrl,
            int textColor,
            int backgroundColor,
            @NonNull String content,
            @NonNull String timestamp
    ) {
        this.id = id;
        this.workmateName = workmateName;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.pictureUrl = pictureUrl;
        this.content = content;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatDetailsViewState that = (ChatDetailsViewState) o;
        return textColor == that.textColor && backgroundColor == that.backgroundColor && id.equals(that.id) && workmateName.equals(that.workmateName) && pictureUrl.equals(that.pictureUrl) && content.equals(that.content) && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workmateName, pictureUrl, textColor, backgroundColor, content, timestamp);
    }

    @NonNull
    @Override
    public String toString() {
        return "ChatDetailsViewState{" +
                "id='" + id + '\'' +
                ", workmateName='" + workmateName + '\'' +
                ", pictureUrl='" + pictureUrl + '\'' +
                ", textColor=" + textColor +
                ", backgroundColor=" + backgroundColor +
                ", content='" + content + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}