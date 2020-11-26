package com.example.drzavnamatura_endgame.RecyclerViewHelper;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
public class MyGridLayoutManager extends GridLayoutManager {



    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

}
