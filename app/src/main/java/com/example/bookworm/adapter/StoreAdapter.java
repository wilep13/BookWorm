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

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private final List<Store> stores;

    public StoreAdapter(List<Store> stores) {
        this.stores = stores;
    }

    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_store_card, parent, false);
        return new StoreViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder h, int position) {
        Store store = stores.get(position);

        Glide.with(h.ivStore.getContext())
                .load(store.getImageResId())
                .centerCrop()
                .into(h.ivStore);

        h.tvName   .setText(store.getName());
        h.tvAddress.setText(store.getAddress());
        h.tvContact.setText(store.getContact());
        h.tvHours  .setText(store.getHours());
    }

    @Override
    public int getItemCount() { return stores.size(); }

    static class StoreViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivStore;
        final TextView tvName, tvAddress, tvContact, tvHours;

        StoreViewHolder(@NonNull View itemView) {
            super(itemView);
            ivStore    = itemView.findViewById(R.id.iv_store);
            tvName     = itemView.findViewById(R.id.tv_name);
            tvAddress  = itemView.findViewById(R.id.tv_address);
            tvContact  = itemView.findViewById(R.id.tv_contact);
            tvHours    = itemView.findViewById(R.id.tv_hours);
        }
    }
}
