package com.example.bookworm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.bookworm.R;
import com.example.bookworm.model.Store;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    public interface OnSlideClickListener {
        void onSlideClick();
    }

    private final List<Store> stores;
    private OnSlideClickListener listener;

    public CarouselAdapter(List<Store> stores) {
        this.stores = stores;
    }

    public void setOnSlideClickListener(OnSlideClickListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carousel_slide, parent, false);
        return new CarouselViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder h, int position) {
        Store store = stores.get(position);

        Glide.with(h.ivSlide.getContext())
                .load(store.getImageResId())
                .centerCrop()
                .into(h.ivSlide);

        h.tvCity.setText(store.getCity());

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSlideClick();
        });
    }

    @Override
    public int getItemCount() { return stores.size(); }

    static class CarouselViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivSlide;
        final TextView  tvCity;

        CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSlide = itemView.findViewById(R.id.iv_slide);
            tvCity  = itemView.findViewById(R.id.tv_city);
        }
    }
}
