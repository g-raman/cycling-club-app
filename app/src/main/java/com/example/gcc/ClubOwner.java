package com.example.gcc;

public class ClubOwner extends Account {
    public String clubName;
    public Event[] hostedEvents;

    public ClubOwner(String clubName, String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.clubName = clubName;
    }

    public ClubOwner(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public ClubOwner(String password, String role) {
        this.password = password;
        this.role = role;
    }



    public void createEvent() throws Exception {
        throw new Exception("To be implemented");
    }

    public void deleteEvent() throws Exception {
        throw new Exception("To be implemented");
    }

    public User[] getMembers() throws Exception {
        throw new Exception("To be implemented");
    }
}
