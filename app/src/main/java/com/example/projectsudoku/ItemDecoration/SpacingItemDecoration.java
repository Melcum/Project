package com.example.projectsudoku.ItemDecoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpaceWide;

    public SpacingItemDecoration(int horizontalSpaceWide){
        this.horizontalSpaceWide = horizontalSpaceWide;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.right = horizontalSpaceWide;
    }
}
