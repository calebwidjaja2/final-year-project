package com.example.selftourismapp;

public class user_message {
    private String text;
    private MemberData data;
    private boolean currentUser;

    public user_message(String text, MemberData data, boolean currentUser){
        this.text = text;
        this.data = data;
        this.currentUser = currentUser;

    }

    public String getText(){
        return text;
    }
    public MemberData getData(){
        return data;

    }
    public boolean isCurrentUser(){
        return currentUser;
    }
}
