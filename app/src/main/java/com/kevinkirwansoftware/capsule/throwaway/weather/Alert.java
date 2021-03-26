package com.kevinkirwansoftware.capsule.throwaway.weather;

public class Alert {
    public String sender_name;
    public String event;
    public int start;
    public int end;
    public String description;

    public Alert(String sender_name, String event, int start, int end, String description) {
        this.sender_name = sender_name;
        this.event = event;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getEvent() {
        return event;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getDescription() {
        return description;
    }
}
