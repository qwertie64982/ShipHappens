/**
 * An app for truckers to share information on which companies to work with or avoid.
 *
 * @author Maxwell Sherman
 */

package com.qwertie64982.shiphappens;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

// TODO: Custom ArrayAdapter layout with name, rating, and comments (first 50 chars max, then ellipsis)
// there is a "Small" RatingBar style that would work well for this

// TODO: My item is at the top, and I can edit/delete it

// TODO: Click ListView item brings up scrollable Dialog with their review in it

// Long term: When the intent from ReviewActivity is received, add the user's review to a special
//            location so they can edit/delete it.
//            This is all once Firebase is implemented, of course.
//            Once there are multiple companies, Firebase will give their info and fill the Views.
//            It should refresh when starting the activity, but not when rotating the screen.
//            When refreshing, this should be a method that includes updating all the UI as well.
// Long term: Fix average rating bar so it shows values with more accuracy than 0.5
//            And maybe add "Terrible"/"Bad"/"OK"/"Good"/"Excellent" or whatever Google Maps has

/**
 * Activity where the user can view reviews for a company
 */
public class CompanyActivity extends AppCompatActivity {
    // Main
    private final String TAG = "CompanyActivity";
    private final int SUBMIT_REVIEW_REQUEST_CODE = 0;
    private final String GITHUB_URL = "https://github.com/qwertie64982/ShipHappens";

    // SavedInstanceState keys
    private final String HAS_LEFT_REVIEW = "hasLeftReview";
    private final String REVIEW_ARRAY_LIST = "reviewArrayList";

    // Intent keys
    private final String POST_AUTHOR = "author";
    private final String POST_MESSAGE = "message";
    private final String POST_RATING = "rating";

    // User
    final String authorName = "John Doe";

    // Views
    private TextView numberReviewsTextView;
    private Button addReviewButton;
    private TextView noReviewsTextView;
    private RatingBar averageRatingBar;

    // Logic
    private boolean hasLeftReview;
    private ArrayList<Review> reviewArrayList;
    private ArrayAdapter<Review> reviewArrayAdapter;
    private AlertDialog aboutDialog;

    /**
     * onCreate
     * @param savedInstanceState things that need to be preserved when the Activity is recreated
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
        if (savedInstanceState != null) {
            hasLeftReview = savedInstanceState.getBoolean(HAS_LEFT_REVIEW);
        } else {
            hasLeftReview = false;
        }

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

        if (savedInstanceState != null) {
            reviewArrayList = (ArrayList<Review>) savedInstanceState.<Review>getParcelableArrayList(REVIEW_ARRAY_LIST);
        } else {
            reviewArrayList = new ArrayList<>();
            reviewArrayList.add(new Review("Test name", "This is a test message", 3));
            reviewArrayList.add(new Review("Bob Smith", "I'm saying something different", 4));
        }

        // this is were we pull the existing reviews from Firebase
        // the stuff that happens after this should happen after the AsyncTask finishes
        noReviewsTextView = (TextView) findViewById(R.id.noReviewsTextView);
        if (reviewArrayList.size() > 0) {
            noReviewsTextView.setVisibility(View.GONE);
        }

        numberReviewsTextView = (TextView) findViewById(R.id.numberReviewsTextView);
        numberReviewsTextView.setText(getString(R.string.number_reviews, reviewArrayList.size()));

        reviewArrayAdapter = new ArrayAdapter<Review>(this, android.R.layout.simple_list_item_1, reviewArrayList);
        NonScrollListView reviewsListView = (NonScrollListView) findViewById(R.id.reviewsListView);
        reviewsListView.setAdapter(reviewArrayAdapter);

        averageRatingBar = (RatingBar) findViewById(R.id.averageRatingBar);
        updateAverageRatingBar();
    }

    /**
     * onStop
     * Dismisses any open dialogs so they don't leak
     */
    @Override
    protected void onStop() {
        if (aboutDialog != null) {
            aboutDialog.dismiss();
        }
        super.onStop();
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

            updateAverageRatingBar();

            hasLeftReview = true;
            addReviewButton.setVisibility(View.GONE);
            noReviewsTextView.setVisibility(View.GONE);

            numberReviewsTextView.setText(getString(R.string.number_reviews, reviewArrayList.size()));
        }
    }

    /**
     * Initializes the options menu
     * @param menu Menu to be displayed
     * @return true if the menu should be displayed, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_company, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Runs when a menu item is selected
     * @param item which item was selected
     * @return true if the event was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.aboutMenuItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyActivity.this);
                builder.setTitle(R.string.about_title)
                        .setMessage(R.string.about_message)
                        .setPositiveButton(R.string.button_label_ok, null)
                        .setNeutralButton(R.string.github_button, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(GITHUB_URL));
                                startActivity(intent);
                            }
                        });
                aboutDialog = builder.create();
                aboutDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Runs whenever the instance is temporarily saved
     * @param outState Bundle given to onCreate when the instance is loaded again
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(HAS_LEFT_REVIEW, hasLeftReview);
        outState.putParcelableArrayList(REVIEW_ARRAY_LIST, reviewArrayList);
        super.onSaveInstanceState(outState);
    }

    /**
     * Updates averageRatingBar with every review from reviewArrayList
     */
    private void updateAverageRatingBar() {
        if (reviewArrayList.size() > 0) {
            float total = 0;
            for (Review listViewReview : reviewArrayList) {
                total += listViewReview.getRating();
            }
            averageRatingBar.setRating(total / reviewArrayList.size());
        }
    }
}
