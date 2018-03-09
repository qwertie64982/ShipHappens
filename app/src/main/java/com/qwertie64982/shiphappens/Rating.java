package com.qwertie64982.shiphappens;

public class Rating {
    private String author;
    private String message;
    private int rating;

    /**
     * EVC
     */
    public Rating() {
        this.author = "NO NAME";
        this.message = "NO MESSAGE";
        this.rating = 3;
    }

    /**
     * DVC
     * @param author author's name
     * @param message author's message body
     * @param rating author's rating, between 1 and 5
     */
    public Rating(String author, String message, int rating) {
        this.author = author;
        this.message = message;
        this.rating = rating;
    }

    /**
     * toString override
     * @return Name of rating's author
     */
    @Override
    public String toString() {
        return this.author + ", " + this.message + ", " + this.rating;
    }
}
