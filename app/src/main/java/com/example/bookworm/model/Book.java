package com.example.bookworm.model;

public class Book {
    private final String id;
    private final String title;
    private final String author;
    private final String category;
    private final String tag;
    private final String clothColor;
    private final String accentColor;
    private final String year;
    private final int pages;
    private final int price;
    private final String blurb;

    public Book(String id, String title, String author, String category, String tag,
                String clothColor, String accentColor, String year, int pages, int price, String blurb) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.tag = tag;
        this.clothColor = clothColor;
        this.accentColor = accentColor;
        this.year = year;
        this.pages = pages;
        this.price = price;
        this.blurb = blurb;
    }

    public String getId()         { return id; }
    public String getTitle()      { return title; }
    public String getAuthor()     { return author; }
    public String getCategory()   { return category; }
    public String getTag()        { return tag; }
    public String getClothColor() { return clothColor; }
    public String getAccentColor(){ return accentColor; }
    public String getYear()       { return year; }
    public int    getPages()      { return pages; }
    public int    getPrice()      { return price; }
    public String getBlurb()      { return blurb; }

    public String getPriceFormatted() {
        return "Rp " + String.format("%,d", price).replace(',', '.');
    }
}
