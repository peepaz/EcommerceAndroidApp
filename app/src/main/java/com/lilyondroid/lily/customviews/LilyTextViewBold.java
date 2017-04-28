package com.lilyondroid.lily.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by jason on 21/04/2017.
 */

public class LilyTextViewBold extends android.support.v7.widget.AppCompatTextView{

    public LilyTextViewBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LilyTextViewBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LilyTextViewBold(Context context) {
        super(context);
        init();
    }
    protected void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
            setTypeface(tf);
        }
    }
}