package com.example.bookworm.model;

public class Store {
    private final String id;
    private final String name;
    private final int imageResId;
    private final String city;
    private final String address;
    private final String contact;
    private final String hours;

    public Store(String id, String name, int imageResId, String city,
                 String address, String contact, String hours) {
        this.id = id;
        this.name = name;
        this.imageResId = imageResId;
        this.city = city;
        this.address = address;
        this.contact = contact;
        this.hours = hours;
    }

    public String getId()        { return id; }
    public String getName()      { return name; }
    public int    getImageResId(){ return imageResId; }
    public String getCity()      { return city; }
    public String getAddress()   { return address; }
    public String getContact()   { return contact; }
    public String getHours()     { return hours; }
}
