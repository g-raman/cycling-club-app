package com.example.gcc;

public class Event {

    private String name;
    private ClubOwner host;

    private eventType type;
    private String[] participants;
    private String startTime;
    private String location;

    private Float pace;

    private Integer level;

    public Event(String name,eventType type, String[] participants, String startTime, String location,Float pace, Integer level){
        this.name = name;
        this.type=type;
        this.participants=participants;
        this.startTime=startTime;
        this.location=location;
        this.pace=pace;
        this.level=level;

    }
    public String getName(){
        return this.name;
    }
    public Float getPace(){
        return this.pace;
    }
    public Integer getLevel(){
        return this.level;  
    }

    public ClubOwner getHost() {
        return this.host;
    }

    public String[] getParticipants() {
        return this.participants;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public String getLocation() {
        return this.location;
    }

    public eventType getType(){
        return type;
    }
    public void setHost(ClubOwner host) {
        this.host = host;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
