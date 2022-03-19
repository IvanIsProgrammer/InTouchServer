package com.ivan.db;

import com.ivan.web.WebServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DB {
    private static final String TABLE_ACCOUNTS = "Accounts";
    private static final String TABLE_MESSAGES = "Messages";

    private Statement statement;

    private static final DB instance = new DB();
    public static DB getInstance(){
        return instance;
    }

    DB() {
        try {
            Properties props = WebServer.props;
            Connection connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Account> getAccountByLogin() {
        ArrayList<Account> accounts = new ArrayList<>();
        String req = "SELECT Login, Password, Token FROM " + TABLE_ACCOUNTS + ";";
        try {
            ResultSet result = statement.executeQuery(req);
            while (result.next()) {
                String login = result.getString(1);
                String password = result.getString(2);
                String token = result.getString(3);
                accounts.add(new Account(login, password, token));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public Account getAccountByLogin(String login) {
        Account account = null;
        String req = "SELECT Login, Password, Token FROM " + TABLE_ACCOUNTS + " WHERE Login='" +login+ "';";
        try {
            ResultSet result = statement.executeQuery(req);
            if (result.next()) {
                account = new Account(result.getString(1),
                        result.getString(2),
                        result.getString(3)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account getAccountByToken(String token) {
        Account account = null;
        String req = "SELECT Login, Password, Token FROM " + TABLE_ACCOUNTS + " WHERE Token='" +token+ "';";
        try {
            ResultSet result = statement.executeQuery(req);
            if (result.next()) {
                account = new Account(result.getString(1),
                        result.getString(2),
                        result.getString(3)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account addAccount(String login, String password, String token) {
        int id = getMaxID("A_ID", TABLE_ACCOUNTS)+1;

        String req = "INSERT INTO "+ TABLE_ACCOUNTS +" (A_ID, Login, Password, Token) " +
                "VALUES ('"+id+"', '"+login+"', '"+password+"', '"+token+"');";
        Account account = null;
        try {
            statement.execute(req);
            account = new Account(login, password, token);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }



    public Message addMessage(String sender, String content) {
        int id = getMaxID("M_ID", TABLE_MESSAGES)+1;

        String req = "INSERT INTO "+ TABLE_MESSAGES +" (M_ID, Sender, Content) " +
                "VALUES ('"+id+"', '"+sender+"', '"+content+"');";
        Message message = null;
        try {
            statement.execute(req);
            message = new Message(id, sender, content);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    public Message getMessage(int id) {
        String req = "SELECT M_ID, Sender, Content FROM "+ TABLE_MESSAGES +" WHERE M_ID = "+id+";";
        Message message = null;
        try {
            ResultSet result = statement.executeQuery(req);
            if (result.next()) {
                message = new Message(
                        result.getInt(1),
                        result.getString(2),
                        result.getString(3)
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return message;
    }

    public int getPreviousMessageId(int currentId) {
        String req = "SELECT MAX(M_ID) FROM "+ TABLE_MESSAGES +" WHERE M_ID < "+currentId+";";
        try {
            ResultSet result = statement.executeQuery(req);
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getNextMessageId(int currentId) {
        String req = "SELECT MIN(M_ID) FROM "+ TABLE_MESSAGES +" WHERE M_ID > "+currentId+";";
        try {
            ResultSet result = statement.executeQuery(req);
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public int getLastMessageId() {
        String req = "SELECT MAX(M_ID) FROM "+ TABLE_MESSAGES +";";
        try {
            ResultSet result = statement.executeQuery(req);
            if (result.next()) {
                return result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }



    public boolean checkToken(String token) {
        String req = "SELECT Token FROM " + TABLE_ACCOUNTS + " WHERE Token='" +token+ "';";
        try {
            ResultSet resultSet = statement.executeQuery(req);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getMaxID(String id, String tables) {
        String req = "SELECT MAX("+id+") FROM "+tables+";";
        try {
            ResultSet resultSet = statement.executeQuery(req);
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
