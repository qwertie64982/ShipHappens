/**
 * An app for truckers to share information on which companies to work with or avoid.
 *
 * @author Maxwell Sherman
 *
 * @version v0.3-demo
 */

package com.qwertie64982.shiphappens;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;

// TODO: Credit
// Icon made by Freepik (http://www.freepik.com/) from www.flaticon.com (link)

// TODO: About menu for credit and whatnot

// TODO: Long click brings up dialog to delete

// TODO: What to display if the ListView is empty

// Long term: When the intent from ReviewActivity is received, add the user's review to a special
//            location so they can edit/delete it.
//            This is all once Firebase is implemented, of course.
//            Once there are multiple companies, Firebase will give their info and fill the Views.

/**
 * Activity where the user can view reviews for a company
 */
public class CompanyActivity extends AppCompatActivity {
    // Main
    private final String TAG = "CompanyActivity";
    private final int SUBMIT_REVIEW_REQUEST_CODE = 0;

    // Intent keys
    private final String POST_AUTHOR = "author";
    private final String POST_MESSAGE = "message";
    private final String POST_RATING = "rating";

    // User
    final String authorName = "John Doe";

    // Views
    Button addReviewButton;

    // Logic
    private boolean hasLeftReview;
    private ArrayList<Review> reviewArrayList;
    private ArrayAdapter<Review> reviewArrayAdapter;

    /**
     * onCreate
     * @param savedInstanceState savedInstanceState Bundle (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        // Rename the action bar to the proper title (changing it in the Manifest renames it in the launcher too)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.temp_company_name);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        // this will be determined by Firebase in the future
        hasLeftReview = false;

        addReviewButton = (Button) findViewById(R.id.addReviewButton);
        if (hasLeftReview) {
            addReviewButton.setVisibility(View.GONE);
        }

        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyActivity.this, ReviewActivity.class);
                intent.putExtra(POST_AUTHOR, authorName);
                startActivityForResult(intent, SUBMIT_REVIEW_REQUEST_CODE);
            }
        });

        reviewArrayList = new ArrayList<>();
        // this is were we pull the existing reviews from Firebase
        reviewArrayList.add(new Review("Test Name", "Test message", 3));

        // TODO: make a custom layout with name, rating, and comments
        reviewArrayAdapter = new ArrayAdapter<Review>(this, android.R.layout.simple_list_item_1, reviewArrayList);
        NonScrollListView reviewsListView = (NonScrollListView) findViewById(R.id.reviewsListView);
        reviewsListView.setAdapter(reviewArrayAdapter);
    }

    /**
     * Runs when the user leaves the review screen (whether or not they submitted)
     * @param requestCode which request started the Activity
     * @param resultCode whether or not the Activity finished successfully
     * @param data data sent back here
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SUBMIT_REVIEW_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String message = data.getStringExtra(POST_MESSAGE);
            int rating = data.getIntExtra(POST_RATING, 3);
            // default of 3 so a strange error won't skew things too badly

            Review newReview = new Review(authorName, message, rating);
            Log.d(TAG, "onActivityResult: Received review: " + newReview);

            reviewArrayList.add(newReview);
            Log.d(TAG, "onActivityResult: List now contains: " + reviewArrayList);
            reviewArrayAdapter.notifyDataSetChanged();
            hasLeftReview = true;
            addReviewButton.setVisibility(View.GONE);
        }
    }
}
