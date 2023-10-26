package com.example.gcc;

public class eventType {
    private String Name;

    private String Description;

    private Integer Level;
    private Float PaceMin;
    private Float PaceMax;
    private Integer age;

    public eventType(String Name, String Description, Integer Level, Float PaceMin, Float PaceMax, Integer age){
        this.Name=Name;
        this.Description=Description;
        this.Level=Level;
        this.PaceMin=PaceMin;
        this.PaceMax=PaceMax;
        this.age=age;
    }

    public String getName() {
        return Name;
    }
    public String getDescription(){
        return Description;
    }

    public Integer getLevel(){
        return Level;
    }


}
