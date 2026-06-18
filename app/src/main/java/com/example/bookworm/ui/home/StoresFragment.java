package com.example.bookworm.ui.home;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookworm.R;
import com.example.bookworm.adapter.StoreAdapter;
import com.example.bookworm.data.Catalogue;
import com.example.bookworm.ui.MainActivity;

public class StoresFragment extends Fragment {

    private NestedScrollView nsvStores;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stores, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nsvStores = view.findViewById(R.id.nsv_stores);
        setupStoreList(view);

        ImageView avatar = view.findViewById(R.id.iv_avatar);
        if (avatar != null) avatar.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showAvatarMenu(v));
    }

    private void setupStoreList(View root) {
        RecyclerView rv = root.findViewById(R.id.rv_stores);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(new StoreAdapter(Catalogue.STORES));
        rv.addItemDecoration(new StoreItemDecoration(dp(16)));
        rv.setNestedScrollingEnabled(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && nsvStores != null) nsvStores.scrollTo(0, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        nsvStores = null;
    }

    private int dp(int val) {
        return Math.round(val * requireContext().getResources().getDisplayMetrics().density);
    }

    private static class StoreItemDecoration extends RecyclerView.ItemDecoration {
        private final int gap;
        StoreItemDecoration(int gap) { this.gap = gap; }

        @Override
        public void getItemOffsets(@NonNull Rect out, @NonNull View v,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State s) {
            if (parent.getChildAdapterPosition(v) > 0) out.top = gap;
        }
    }
}
