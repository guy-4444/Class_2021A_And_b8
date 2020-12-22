package com.classy.class_2021a_and_b8;

import java.util.ArrayList;

public class ContactsDB {

    private String userName = "";
    private long date = 0;
    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactsDB() { }

    public String getUserName() {
        return userName;
    }

    public ContactsDB setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public long getDate() {
        return date;
    }

    public ContactsDB setDate(long date) {
        this.date = date;
        return this;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public ContactsDB setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        return this;
    }


}
