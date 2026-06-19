package com.example.bookworm.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.bookworm.R;
import com.example.bookworm.adapter.BookAdapter;
import com.example.bookworm.adapter.CarouselAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.data.UserSession;
import com.example.bookworm.model.Book;
import com.example.bookworm.ui.MainActivity;
import com.example.bookworm.view.NavbarView;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final long AUTO_SLIDE_DELAY_MS = 3_000L;

    private NestedScrollView nsvHome;
    private ViewPager2 vpCarousel;
    private LinearLayout llDots;
    private int actualCount;

    private final Handler autoSlideHandler = new Handler(Looper.getMainLooper());
    private final Runnable autoSlideRunnable = new Runnable() {
        @Override public void run() {
            if (vpCarousel != null) {
                vpCarousel.setCurrentItem(vpCarousel.getCurrentItem() + 1, true);
                autoSlideHandler.postDelayed(this, AUTO_SLIDE_DELAY_MS);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nsvHome = view.findViewById(R.id.nsv_home);
        setupGreeting(view);
        setupCarousel(view);
        setupBookGrid(view);
        setupAvatar(view);
    }

    private void setupGreeting(View root) {
        TextView tvGreeting = root.findViewById(R.id.tv_greeting);
        if (tvGreeting != null && !UserSession.username.isEmpty()) {
            tvGreeting.setText("Hello, " + UserSession.username + "!");
        }
    }

    private void setupCarousel(View root) {
        vpCarousel = root.findViewById(R.id.vp_carousel);
        llDots     = root.findViewById(R.id.ll_dots);

        actualCount = Catalogue.STORES.size();
        CarouselAdapter adapter = new CarouselAdapter(Catalogue.STORES);
        adapter.setOnSlideClickListener(() ->
                ((MainActivity) requireActivity()).switchToTab(NavbarView.SLOT_STORES));

        vpCarousel.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override public void onPageSelected(int position) { buildDots(position % actualCount); }
        });
        vpCarousel.setAdapter(adapter);
        int initialPage = actualCount * (CarouselAdapter.LOOP_FACTOR / 2);
        vpCarousel.setCurrentItem(initialPage, false);
        buildDots(0);

        ImageView btnPrev = root.findViewById(R.id.btn_carousel_prev);
        ImageView btnNext = root.findViewById(R.id.btn_carousel_next);
        btnPrev.setOnClickListener(v ->
                vpCarousel.setCurrentItem(vpCarousel.getCurrentItem() - 1, true));
        btnNext.setOnClickListener(v ->
                vpCarousel.setCurrentItem(vpCarousel.getCurrentItem() + 1, true));
    }

    private void buildDots(int activeIndex) {
        llDots.removeAllViews();
        int dpActive = dp(20), dpInactive = dp(8), height = dp(8), gap = dp(7);
        for (int i = 0; i < actualCount; i++) {
            View dot = new View(requireContext());
            boolean active = i == activeIndex;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    active ? dpActive : dpInactive, height);
            if (i > 0) lp.leftMargin = gap;
            dot.setLayoutParams(lp);
            dot.setBackgroundResource(active ? R.drawable.bg_dot_active : R.drawable.bg_dot_inactive);
            llDots.addView(dot);
        }
    }

    private void setupBookGrid(View root) {
        List<Book> featured = Catalogue.getFeatured();
        RecyclerView rv = root.findViewById(R.id.rv_books);
        rv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        BookAdapter adapter = new BookAdapter(featured, false);
        adapter.setOnBookClickListener(book -> {
            Intent intent = new Intent(requireContext(), BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        rv.setAdapter(adapter);
        rv.addItemDecoration(new BookGridDecoration(dp(16), dp(30)));
    }

    private void setupAvatar(View root) {
        ImageView avatar = root.findViewById(R.id.iv_avatar);
        if (avatar != null) avatar.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showAvatarMenu(v));

        TextView tvSeeAll = root.findViewById(R.id.tv_see_all);
        if (tvSeeAll != null) tvSeeAll.setOnClickListener(v ->
                ((MainActivity) requireActivity()).switchToTab(NavbarView.SLOT_BOOKS));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isHidden()) autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY_MS);
    }

    @Override
    public void onStop() {
        super.onStop();
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            autoSlideHandler.removeCallbacks(autoSlideRunnable);
        } else {
            if (nsvHome != null) nsvHome.scrollTo(0, 0);
            autoSlideHandler.removeCallbacks(autoSlideRunnable);
            autoSlideHandler.postDelayed(autoSlideRunnable, AUTO_SLIDE_DELAY_MS);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
        vpCarousel = null;
        llDots = null;
        nsvHome = null;
    }

    private int dp(int val) {
        return Math.round(val * requireContext().getResources().getDisplayMetrics().density);
    }
}
