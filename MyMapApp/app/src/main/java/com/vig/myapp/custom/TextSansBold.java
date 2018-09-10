package com.vig.myapp.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class TextSansBold extends android.support.v7.widget.AppCompatTextView {
    public TextSansBold(Context context) {
        super(context);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Bold.ttf");
        this.setTypeface(face);
        init();
    }

    public TextSansBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Bold.ttf");
        this.setTypeface(face);
    }

    public TextSansBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "fonts/GoogleSans-Bold.ttf");
        this.setTypeface(face);
    }


    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/GoogleSans-Bold.ttf");
            setTypeface(tf);
        }
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

}

