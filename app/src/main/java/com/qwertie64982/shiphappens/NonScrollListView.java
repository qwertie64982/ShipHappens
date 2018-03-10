/**
 * An app for truckers to share information on which companies to work with or avoid.
 *
 * @author Maxwell Sherman
 */

package com.qwertie64982.shiphappens;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Non-scrolling ListView, so the user can scroll the entire page of a company's reviews
 * rather than just the list of reviews
 */
public class NonScrollListView extends ListView {
    public NonScrollListView(Context context) {
        super(context);
    }

    public NonScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Overrides onMeasure make the ListView never need to use its own scrolling functionality
     * @param widthMeasureSpec horizontal space constraints given by parent
     * @param heightMeasureSpec vertical space constraints given by parent
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Allow the height to be as big (vertically) as it can be
        // so that it never needs to implement its own scrolling
        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 1 , MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = getMeasuredHeight();
    }
}
