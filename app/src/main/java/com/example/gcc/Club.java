package com.example.gcc;

public class Club {

    String Name;

    String Rating;

    String Comment;

    Club(String Name,String Rating, String Comment){
        this.Name=Name;
        this.Rating=Rating;
        this.Comment=Comment;
    }
    Club(String Name,String Rating){
        this.Name=Name;
        this.Rating=Rating;
    }

    public String getName() {
        return Name;
    }

    public String getRating() {
        return Rating;
    }

    public String getComment() {
        return Comment;
    }
}
