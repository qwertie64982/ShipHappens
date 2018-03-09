package com.qwertie64982.shiphappens;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

// TODO: Add back arrow

// TODO: Move submit button to menu bar

// TODO: Require user to enter a star rating (so it doesn't return 0)

public class RatingActivity extends AppCompatActivity {
    private final String TAG = "RatingActivity";
    private final int MAX_MESSAGE_LENGTH = 500;

    // Intent keys
    private final String POST_AUTHOR = "author";
    private final String POST_MESSAGE = "message";
    private final String POST_RATING = "rating";

    private final String TEMP_AUTHOR = "John Doe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        // TODO: Put a counter so the user knows how many characters they have/can enter
        final EditText messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_MESSAGE_LENGTH)});

        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(POST_AUTHOR, TEMP_AUTHOR);
                intent.putExtra(POST_MESSAGE, messageEditText.getText().toString());
                intent.putExtra(POST_RATING, (int) ratingBar.getRating());

                Log.d(TAG, "onClick: Packed intent with " + TEMP_AUTHOR + ", " + messageEditText.getText().toString() + ", " + ratingBar.getNumStars());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
