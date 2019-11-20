package com.lilyondroid.lily.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class LilyTextView extends androidx.appcompat.widget.AppCompatTextView {

    public LilyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public LilyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LilyTextView(Context context) {
        super(context);
        init();
    }

    protected void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
            setTypeface(tf);
        }
    }

}


