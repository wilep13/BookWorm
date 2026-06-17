package com.example.bookworm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bookworm.R;
import com.example.bookworm.model.Book;
import com.example.bookworm.view.BookCoverView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    private final List<Book> books;
    private final boolean showTag;
    private OnBookClickListener listener;

    public BookAdapter(List<Book> books, boolean showTag) {
        this.books   = books;
        this.showTag = showTag;
    }

    public void setOnBookClickListener(OnBookClickListener l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book_card, parent, false);
        return new BookViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder h, int position) {
        Book book = books.get(position);
        h.coverView.setBook(book);
        h.tvTitle.setText(book.getTitle());
        h.tvAuthor.setText(book.getAuthor());

        if (showTag) {
            h.tvTag.setVisibility(View.VISIBLE);
            h.tvTag.setText(book.getTag());
        } else {
            h.tvTag.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onBookClick(book);
        });
    }

    @Override
    public int getItemCount() { return books.size(); }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        final BookCoverView coverView;
        final TextView tvTag, tvTitle, tvAuthor;

        BookViewHolder(@NonNull View itemView) {
            super(itemView);
            coverView = itemView.findViewById(R.id.book_cover_view);
            tvTag     = itemView.findViewById(R.id.tv_tag);
            tvTitle   = itemView.findViewById(R.id.tv_title);
            tvAuthor  = itemView.findViewById(R.id.tv_author);
        }
    }
}
