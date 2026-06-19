package com.example.bookworm.ui.home;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookGridDecoration extends RecyclerView.ItemDecoration {
    private final int colGap, rowGap;

    public BookGridDecoration(int colGap, int rowGap) {
        this.colGap = colGap;
        this.rowGap = rowGap;
    }

    @Override
    public void getItemOffsets(@NonNull Rect out, @NonNull View v,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State s) {
        int pos = parent.getChildAdapterPosition(v);
        int col = pos % 2;
        out.left  = col == 0 ? 0 : colGap / 2;
        out.right = col == 0 ? colGap / 2 : 0;
        if (pos >= 2) out.top = rowGap;
    }
}
