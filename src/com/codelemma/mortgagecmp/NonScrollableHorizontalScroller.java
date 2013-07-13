package com.codelemma.mortgagecmp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class NonScrollableHorizontalScroller extends HorizontalScrollView {

 	public NonScrollableHorizontalScroller(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NonScrollableHorizontalScroller(Context context) {
		super(context);
	}

	public NonScrollableHorizontalScroller(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //int action = ev.getAction();
        //switch (action) {
        //case MotionEvent.ACTION_DOWN:
            // Disallow ScrollView to intercept touch events.
            this.getParent().requestDisallowInterceptTouchEvent(true);
          //  break;
        //case MotionEvent.ACTION_UP:
            // Allow ScrollView to intercept touch events.
          //  this.getParent().requestDisallowInterceptTouchEvent(false);
            //break;
        //}

        super.onTouchEvent(ev);
        return true;
    }
}