package com.vig.myapp.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class TextSansRegular extends android.support.v7.widget.AppCompatTextView {
    public TextSansRegular(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Regular.ttf");
        this.setTypeface(face);
        init();
    }

    public TextSansRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Regular.ttf");
        this.setTypeface(face);
    }

    public TextSansRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Regular.ttf");
        this.setTypeface(face);
    }


    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Regular.ttf");
            setTypeface(tf);
        }
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}