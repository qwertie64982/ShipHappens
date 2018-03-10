/**
 * An app for truckers to share information on which companies to work with or avoid.
 *
 * @author Maxwell Sherman
 */

package com.qwertie64982.shiphappens;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

// Long term: Use an AsyncTask to send the review to Firebase here.
//            This is the Activity that will handle whether or not it was successful.
//            The review will still be sent back to CompanyActivity, though, so that
//            it can display the user's rating separate from the others in case they want to
//            edit or delete it.

/**
 * Activity where the user enters their review for a company
 */
public class ReviewActivity extends AppCompatActivity {
    // Main
    private final String TAG = "ReviewActivity";
    private final int MAX_MESSAGE_LENGTH = 500;

    // Intent keys
    private final String POST_AUTHOR = "author";
    private final String POST_MESSAGE = "message";
    private final String POST_RATING = "rating";

    // User
    private String authorName;

    // Views
    private TextView authorTextView;
    private RatingBar ratingBar;
    private EditText messageEditText;
    private TextView characterCountTextView;

    /**
     * onCreate
     * @param savedInstanceState savedInstanceState Bundle (unused)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Intent intent = getIntent();
        authorName = intent.getStringExtra(POST_AUTHOR);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        authorTextView = (TextView) findViewById(R.id.authorTextView);
        authorTextView.setText(getString(R.string.posting_as_author, authorName));
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_MESSAGE_LENGTH)});

        characterCountTextView = (TextView) findViewById(R.id.characterCountTextView);
        characterCountTextView.setText(getApplicationContext().getString(R.string.character_count, 0, MAX_MESSAGE_LENGTH));
        TextWatcher characterCountTextWatcher = new TextWatcher() {
            /**
             * Runs right before characters are changed
             * @param charSequence text where characters will be changed
             * @param i starting point for where characters will be changed
             * @param i1 how many characters will be changed
             * @param i2 length of characters that will replace the ones beginning at i
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Runs while characters are changed
             * @param charSequence text where characters are being changed
             * @param i starting point for where characters are being changed
             * @param i1 how many characters are being changed
             * @param i2 length of characters replacing the ones beginning at i
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                characterCountTextView.setText(getApplicationContext().getString(
                        R.string.character_count, messageEditText.length(), MAX_MESSAGE_LENGTH));
                if (messageEditText.length() >= MAX_MESSAGE_LENGTH) {
                    characterCountTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    characterCountTextView.setTextColor(getResources().getColor(android.R.color.black));
                }
            }

            /**
             * Runs after characters are changed
             * @param editable text where characters were changed
             */
            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        messageEditText.addTextChangedListener(characterCountTextWatcher);
    }

    /**
     * Initializes the options menu
     * @param menu Menu to be displayed
     * @return true if the menu should be displayed, false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_review, menu);
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
            case android.R.id.home:
                if (messageEditText.length() > 0 || ratingBar.getRating() > 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                    builder.setTitle(R.string.review_warning_title)
                            .setMessage(R.string.review_warning_message)
                            .setPositiveButton(R.string.review_warning_leave, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.review_warning_stay, null);
                    builder.create().show();
                }
                return true;
            case R.id.submitMenuItem:
                if (ratingBar.getRating() == 0 || messageEditText.length() == 0) {
                    Toast enterStarsToast = Toast.makeText(ReviewActivity.this,
                            getString(R.string.incomplete_entry_toast), Toast.LENGTH_SHORT);
                    enterStarsToast.setGravity(Gravity.TOP, 0, 0);
                    enterStarsToast.show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(POST_MESSAGE, messageEditText.getText().toString());
                    intent.putExtra(POST_RATING, (int) ratingBar.getRating());

                    Log.d(TAG, "onClick: Packed intent with " + authorName + ", " + messageEditText.getText().toString() + ", " + ratingBar.getNumStars());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Runs when the back button (navigation bar) was pressed
     */
    @Override
    public void onBackPressed() {
        if (messageEditText.length() > 0 || ratingBar.getRating() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
            builder.setTitle(R.string.review_warning_title)
                    .setMessage(R.string.review_warning_message)
                    .setPositiveButton(R.string.review_warning_leave, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.review_warning_stay, null);
            builder.create().show();
        }
    }
}
