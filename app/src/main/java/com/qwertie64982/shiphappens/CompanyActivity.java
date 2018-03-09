package com.qwertie64982.shiphappens;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

// TODO: Credit
// Icon made by Freepik (http://www.freepik.com/) from www.flaticon.com (link)

// TODO: orientation changes - SavedInstanceState stuff

// TODO: About menu for credit and whatnot

// TODO: Long click brings up dialog to delete

// TODO: Proper documentation. Too tired right now but it's simple enough, I'll figure it out

public class CompanyActivity extends AppCompatActivity {
    private final String TAG = "CompanyActivity";
    private final int SUBMIT_RATING_REQUEST_CODE = 0;

    // Intent keys
    private final String POST_AUTHOR = "author";
    private final String POST_MESSAGE = "message";
    private final String POST_RATING = "rating";

    private boolean hasLeftReview;
    private ArrayList<Rating> ratingArrayList;
    private ArrayAdapter<Rating> ratingArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);

        // Rename the action bar to the proper title (changing it in the Manifest renames it in the launcher too)
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.temp_company_name);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        // check if has left review
        hasLeftReview = false;

        Button addReviewButton = (Button) findViewById(R.id.addReviewButton);
        if (hasLeftReview) {
            addReviewButton.setVisibility(View.GONE);
        }

        addReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyActivity.this, RatingActivity.class);
                startActivityForResult(intent, SUBMIT_RATING_REQUEST_CODE);
            }
        });

        ratingArrayList = new ArrayList<>();
        // this is were we pull the existing ratings from Firebase
        ratingArrayList.add(new Rating("Test Name", "Test message", 3));

        // TODO: make a custom layout with name, rating, and comments
        // and have it look different if the user leaves only whitespace as their message
        ratingArrayAdapter = new ArrayAdapter<Rating>(this, android.R.layout.simple_list_item_1, ratingArrayList);
        NonScrollListView ratingsListView = (NonScrollListView) findViewById(R.id.ratingsListView);
        ratingsListView.setAdapter(ratingArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SUBMIT_RATING_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String author = data.getStringExtra(POST_AUTHOR);
            String message = data.getStringExtra(POST_MESSAGE);
            int rating = data.getIntExtra(POST_RATING, 3); // default of 3 so an error won't skew things

            Rating newRating = new Rating(author, message, rating);
            Log.d(TAG, "onActivityResult: Received rating: " + newRating);

            ratingArrayList.add(newRating);
            Log.d(TAG, "onActivityResult: List now contains: " + ratingArrayList);
            ratingArrayAdapter.notifyDataSetChanged();
            hasLeftReview = true;
        }
    }
}
