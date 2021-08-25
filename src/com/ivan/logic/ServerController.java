package com.ivan.logic;

import com.ivan.db.*;

import java.util.Random;

public class ServerController {
    private static final ServerController instance = new ServerController();
    public static ServerController getInstance() {
        return instance;
    }

    private final DB db = DB.getInstance();

    public String authorization(String login, String password) {
        String response;

        Account user = db.getAccountByLogin(login);
        if (user == null) {
            response = "failed";
        } else if (login.equals(user.login) && password.equals(user.password)) {
            response = user.token; //ToDO: Reload token...
        } else {
            response = "failed";
        }

        return response;
    }

    public String registration(String login, String password) {
        String response;

        String token;
        do {
            token = genToken();
        } while (db.checkToken(token));

        Account user = db.getAccountByLogin(login);
        if (user == null) {
            db.addAccount(login, password, token);
            response = "successful";
        } else {
            response = "failed";
        }
        return response;
    }

    public String sendMessage(String token, String content) {
        Account account = db.getAccountByToken(token);
        if (account != null) {
            db.addMessage(account.login, content);
            return "successful";
        } else {
            return "failed";
        }
    }

    public Message getMessage(String token, int currentId) {
        Account account = db.getAccountByToken(token);
        if (account != null) {
            return db.getMessage(currentId);
        } else {
            return null;
        }
    }

    public Message getPreviousMessage(String token, int currentId) {
        Account account = db.getAccountByToken(token);
        if (account != null) {
            return db.getMessage(db.getPreviousMessageId(currentId));
        } else {
            return null;
        }
    }

    public Message getNextMessage(String token, int currentId) {
        Account account = db.getAccountByToken(token);
        if (account != null) {
            return db.getMessage(db.getNextMessageId(currentId));
        } else {
            return null;
        }
    }

    public Message getLastMessage(String token) {
        Account account = db.getAccountByToken(token);
        if (account != null) {
            return db.getMessage(db.getLastMessageId());
        } else {
            return null;
        }
    }

    private String genToken() {
        StringBuilder token = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < 40; i++) {
            char c;
            int numb = r.nextInt(36);
            if (numb < 10)
                c =  (char) (numb + '0');
            else
                c = (char) (numb-10 + 'A');
            token.append(c);
        }
        return token.toString();
    }
}
