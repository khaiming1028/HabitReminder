package com.example.asgmandrid;

public class Habit {
    private String title;
    private String description;
    private String startingTime;
    private String endingTime;

    public Habit(String title, String description, String startingTime, String endingTime) {
        this.title = title;
        this.description = description;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getStartingTime() { return startingTime; }
    public String getEndingTime() { return endingTime; }
}
