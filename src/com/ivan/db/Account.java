package com.ivan.db;

public class Account {
    public String login;
    public String password;
    public String token;

    public Account(String login, String password, String token) {
        this.login = login;
        this.password = password;
        this.token = token;
    }
}
