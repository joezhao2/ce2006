package com.example.gmodsv1;

//import org.parceler.Parcel;

import java.util.ArrayList;


public class Thread {
    String userName;
    String message;
    int score = 0;
    ArrayList<String> comments = new ArrayList<>();
    private String pushId;

    public Thread(String userName, String message) {
        this.userName = userName;
        this.message = message;
        this.comments = new ArrayList<>();
    }

    public Thread() {}

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public String getPushId() {
        return pushId = pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public void upVote() {
        score ++;
    }

    public void downVote() {
        score --;
    }
}