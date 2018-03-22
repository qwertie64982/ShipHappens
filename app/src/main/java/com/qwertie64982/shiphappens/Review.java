/**
 * An app for truckers to share information on which companies to work with or avoid.
 *
 * @author Maxwell Sherman
 */

package com.qwertie64982.shiphappens;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Review object, used to store an individual review from a user
 */
public class Review implements Parcelable{
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
     * Parcel constructor
     * @param in input Parcel
     */
    public Review(Parcel in) {
        this.author = in.readString();
        this.message = in.readString();
        this.rating = in.readInt();
    }

    /**
     * Author getter
     * @return author name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Message getter
     * @return message contents
     */
    public String getMessage() {
        return message;
    }

    /**
     * Rating getter
     * @return rating value
     */
    public int getRating() {
        return rating;
    }

    /**
     * Describes the contents
     * @return 0 since this object does not include file descriptors
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Packs the Review object into a Parcel
     * @param parcel Parcel that this Review will be packed into
     * @param i additional flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeString(message);
        parcel.writeInt(rating);
    }

    /**
     * Parcelable creator
     */
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    /**
     * toString override
     * @return Review object as a string
     */
    public String toString() {
        return this.author + ", " + this.message + ", " + this.rating;
    }
}
