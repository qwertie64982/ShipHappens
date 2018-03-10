/**
 * An app for truckers to share information on which companies to work with or avoid.
 *
 * @author Maxwell Sherman
 *
 * @version v0.3-demo
 */

package com.qwertie64982.shiphappens;

/**
 * Review object, used to store an individual review from a user
 */
public class Review {
    private String author;
    private String message;
    private int rating;

    /**
     * EVC
     */
    public Review() {
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
    public Review(String author, String message, int rating) {
        this.author = author;
        this.message = message;
        this.rating = rating;
    }

    /**
     * toString override
     * @return Name of review's author
     */
    @Override
    public String toString() {
        return this.author + ", " + this.message + ", " + this.rating;
    }
}
