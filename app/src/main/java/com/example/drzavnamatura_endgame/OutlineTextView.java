package com.example.drzavnamatura_endgame;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class OutlineTextView extends androidx.appcompat.widget.AppCompatTextView {
    public OutlineTextView(Context context) {
        super(context);
    }

    public OutlineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OutlineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Constructors

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < 15; i++) {
            super.draw(canvas);
        }
    }

}
