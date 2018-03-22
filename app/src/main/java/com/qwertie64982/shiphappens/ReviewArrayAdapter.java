/**
 * An app for truckers to share information on which companies to work with or avoid.
 *
 * @author Maxwell Sherman
 */

package com.qwertie64982.shiphappens;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ReviewArrayAdapter extends ArrayAdapter<Review> {
    private final int MAX_CHARACTER_COUNT = 30; // Long term: This should change depending on the screen width

    private Context context;
    private List<Review> reviews;

    /**
     * EVC
     * @param context adapter's context
     * @param resource resource ID of which layout to follow (unused)
     * @param objects which Review objects to display
     */
    public ReviewArrayAdapter(Context context, int resource, ArrayList<Review> objects) {
        super(context, resource, objects);

        this.context = context;
        this.reviews = objects;
    }

    /**
     * Runs when rendering each Review's View
     * @param position index of item in the list
     * @param convertView old View to reuse
     * @param parent parent where this View will attach
     * @return View object representing this Review
     */
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Review review = reviews.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_review, null);
        // TODO: Recycle view

        RatingBar smallRatingBar = (RatingBar) view.findViewById(R.id.smallRatingBar);
        TextView smallAuthorTextView = (TextView) view.findViewById(R.id.smallAuthorTextView);
        TextView smallMessageTextView = (TextView) view.findViewById(R.id.smallMessageTextView);

        smallRatingBar.setRating(review.getRating());
        smallAuthorTextView.setText(review.getAuthor());
        if (review.getMessage().length() >= MAX_CHARACTER_COUNT) {
            smallMessageTextView.setText(context.getString(R.string.small_message_ellipse,
                    review.getMessage().substring(0, MAX_CHARACTER_COUNT)));
        } else {
            smallMessageTextView.setText(review.getMessage());
        }

        return view;
    }
}
