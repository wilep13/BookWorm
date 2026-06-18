package com.example.bookworm.ui.home;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookworm.R;
import com.example.bookworm.adapter.BookAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.model.Book;
import com.example.bookworm.ui.BaseActivity;
import com.example.bookworm.ui.MainActivity;
import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment {

    private NestedScrollView nsvBooks;
    private BookAdapter bookAdapter;
    private final List<Book> currentList = new ArrayList<>();
    private String activeTab = "Non-Fiction";
    private View tabIndicator;
    private TextView tabNonFiction, tabFiction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_books, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nsvBooks = view.findViewById(R.id.nsv_books);
        setupTabs(view);
        setupBookGrid(view);

        ImageView avatar = view.findViewById(R.id.iv_avatar);
        if (avatar != null) avatar.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showAvatarMenu(v));

        applyTab("Non-Fiction", false);
    }

    private void setupTabs(View root) {
        tabNonFiction = root.findViewById(R.id.tab_non_fiction);
        tabFiction    = root.findViewById(R.id.tab_fiction);
        tabIndicator  = root.findViewById(R.id.tab_indicator);

        tabNonFiction.setOnClickListener(v -> applyTab("Non-Fiction", true));
        tabFiction   .setOnClickListener(v -> applyTab("Fiction", true));

        // On the first show, the fragment is still GONE during onViewCreated so
        // getWidth() returns 0 inside post(). This one-shot listener fires after
        // the real layout pass to correctly initialize the indicator.
        tabNonFiction.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int l, int t, int r, int b,
                                       int ol, int ot, int or, int ob) {
                int w = r - l;
                if (w > 0) {
                    v.removeOnLayoutChangeListener(this);
                    tabIndicator.getLayoutParams().width = w;
                    tabIndicator.requestLayout();
                    tabIndicator.setTranslationX(activeTab.equals("Non-Fiction") ? 0f : w);
                }
            }
        });
    }

    private void applyTab(String tab, boolean animate) {
        activeTab = tab;
        tabNonFiction.setTextColor(requireContext().getColor(
                tab.equals("Non-Fiction") ? android.R.color.white : R.color.color_primary_3));
        tabFiction.setTextColor(requireContext().getColor(
                tab.equals("Fiction") ? android.R.color.white : R.color.color_primary_3));

        tabNonFiction.post(() -> {
            int halfW = tabNonFiction.getWidth();
            if (halfW == 0) return;
            tabIndicator.getLayoutParams().width = halfW;
            tabIndicator.requestLayout();

            float targetX = tab.equals("Non-Fiction") ? 0f : halfW;
            if (animate) {
                ObjectAnimator.ofFloat(tabIndicator, "translationX", targetX)
                        .setDuration(320).start();
            } else {
                tabIndicator.setTranslationX(targetX);
            }
        });

        currentList.clear();
        for (Book b : Catalogue.BOOKS) {
            if (b.getCategory().equals(tab)) currentList.add(b);
        }
        if (bookAdapter != null) bookAdapter.notifyDataSetChanged();
    }

    private void setupBookGrid(View root) {
        RecyclerView rv = root.findViewById(R.id.rv_books);
        rv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        bookAdapter = new BookAdapter(currentList, true);
        bookAdapter.setOnBookClickListener(book -> {
            Intent intent = new Intent(requireContext(), BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        rv.setAdapter(bookAdapter);
        rv.addItemDecoration(new BaseActivity.BookGridDecoration(dp(16), dp(30)));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (nsvBooks != null) nsvBooks.scrollTo(0, 0);
            // Views were GONE while hidden so getWidth() was 0 during initial applyTab.
            // Re-apply now that the fragment is visible and views are measurable.
            if (tabNonFiction != null) applyTab(activeTab, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        nsvBooks = null;
        tabIndicator = null;
        tabNonFiction = null;
        tabFiction = null;
        bookAdapter = null;
    }

    private int dp(int val) {
        return Math.round(val * requireContext().getResources().getDisplayMetrics().density);
    }
}
