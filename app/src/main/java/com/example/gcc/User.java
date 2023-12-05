package com.example.gcc;

import java.io.Serializable;

public class User extends Account implements Serializable {
    private int age;
    private int preferredLevel;
    private float preferredPace;

    public Event[] joinedEvents;

    public User(String password, String role, String username, int level, float pace) {
        this.password = password;
        this.role = role;
        this.username = username;
        this.preferredLevel = level;
        this.preferredPace = pace;
    }

    public User() {
    }

    public User(String password, String role, String username) {
        this.password = password;
        this.role = role;
        this.username = username;
    }

    public User(String password, String role) {
        this.password = password;
        this.role = role;
    }

    public void getJoinedEvents() throws Exception {
        throw new Exception("To be implemented");
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPreferredLevel(int level) {
        this.preferredLevel = level;
    }

    public void setPreferredPace(float pace) {
        this.preferredPace = pace;
    }

    public int getAge() {
        return this.age;
    }

    public int getPreferredLevel() {
        return this.preferredLevel;
    }

    public float getPreferredPace() {
        return this.preferredPace;
    }
}
