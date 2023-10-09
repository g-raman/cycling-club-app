package com.example.gcc;

public class User extends Account {
    public String role;
    public Event[] joinedEvents;

    public User(String password, String role) {
        this.password = password;
        this.role = role;
    }

    public void getJoinedEvents() throws Exception {
        throw new Exception("To be implemented");
    }

    public void joinEvent() throws Exception {
        throw new Exception("To be implemented");
    }
}
