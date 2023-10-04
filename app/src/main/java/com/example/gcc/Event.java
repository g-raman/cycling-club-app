package com.example.gcc;

public class Event {
    private ClubOwner host;
    private User[] participants;
    private String startTime;
    private String endTime;
    private String location;
    private String description;

    public ClubOwner getHost() {
        return this.host;
    }

    public User[] getParticipants() {
        return this.participants;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public String getEndTime() {
        return this.endTime;
    }
    public String getLocation() {
        return this.location;
    }

    public String getDescription() {
        return this.description;
    }

    public void setHost(ClubOwner host) {
        this.host = host;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public void setEndTime(String time) {
        this.endTime = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription() {
        this.description = description;
    }
}
