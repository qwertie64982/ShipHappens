package com.qwertie64982.shiphappens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Require user to enter a star rating (so it doesn't return 0)

public class RatingActivity extends AppCompatActivity {
    private final String TAG = "RatingActivity";
    private final int MAX_MESSAGE_LENGTH = 500;

    // Intent keys
    private final String POST_AUTHOR = "author";
    private final String POST_MESSAGE = "message";
    private final String POST_RATING = "rating";

    // User
    private String authorName;

    // Views
    TextView authorTextView;
    RatingBar ratingBar;
    EditText messageEditText;
    TextView characterCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

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
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

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

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        messageEditText.addTextChangedListener(characterCountTextWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_rating, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder builder = new AlertDialog.Builder(RatingActivity.this);
                builder.setTitle(R.string.rating_warning_title)
                        .setMessage(R.string.rating_warning_message)
                        .setPositiveButton(R.string.rating_warning_leave, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                onBackPressed();
                            }
                        })
                        .setNegativeButton(R.string.rating_warning_stay, null);
                builder.create().show();
                return true;
            case R.id.submitMenuItem:
                if (ratingBar.getRating() == 0) {
                    Toast enterStarsToast = Toast.makeText(RatingActivity.this,
                            getString(R.string.enter_stars_toast), Toast.LENGTH_SHORT);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RatingActivity.this);
        builder.setTitle(R.string.rating_warning_title)
                .setMessage(R.string.rating_warning_message)
                .setPositiveButton(R.string.rating_warning_leave, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.rating_warning_stay, null);
        builder.create().show();
    }
}
