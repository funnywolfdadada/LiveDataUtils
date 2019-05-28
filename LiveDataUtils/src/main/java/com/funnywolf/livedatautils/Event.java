package com.funnywolf.livedatautils;

public class Event extends TypeData {
    private Event(int type) {
        super(type);
    }

    public static Event get(int type) {
        return new Event(type);
    }
}
