package com.ood.clean.waterball.a1a2bsdk.core.model;


import java.util.Date;

public class ChatMessage extends SerializableEntity{
    private Player poster;
    private String content;
    private Date postDate = new Date();

    public ChatMessage(Player poster, String content) {
        this.poster = poster;
        this.content = content;
    }

    public ChatMessage(Player poster, String content, Date postDate) {
        this.poster = poster;
        this.content = content;
        this.postDate = postDate;
    }

    public Player getPoster() {
        return poster;
    }

    public void setPoster(Player poster) {
        this.poster = poster;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    @Override
    public String toString() {
        return poster.getName() + ": " + content;
    }
}
