package com.ivan.db;

public class Message {
    public int id;
    public String sender;
    public String content;

    public Message(int id, String sender, String content) {
        this.id = id;
        this.sender = sender;
        this.content = content;
    }
}
