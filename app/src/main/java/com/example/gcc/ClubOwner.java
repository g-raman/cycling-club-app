package com.example.gcc;

public class ClubOwner extends Account {
    public String clubName;
    public String password;
    public Event[] hostedEvents;

    public ClubOwner(String clubName, String password) {
        this.clubName = clubName;
        this.password = password;
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
