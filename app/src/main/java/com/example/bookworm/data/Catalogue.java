package com.example.bookworm.data;

import com.example.bookworm.R;
import com.example.bookworm.model.Book;
import com.example.bookworm.model.Store;
import java.util.Arrays;
import java.util.List;

public class Catalogue {

    public static final List<Book> BOOKS = Arrays.asList(
        // Non-Fiction
        new Book("meditations", "Meditations", "Marcus Aurelius",
            "Non-Fiction", "Stoic Philosophy",
            "#2E5248", "#E7C48B", "c. 180", 254, 91000,
            "The private notes of a Roman emperor to himself — a timeless manual on discipline, perspective and living with virtue amid chaos.",
            R.drawable.cover_art_meditations),
        new Book("republic", "The Republic", "Plato",
            "Non-Fiction", "Classical Thought",
            "#6E2B2B", "#E7C48B", "c. 375 BC", 416, 110000,
            "Plato's dialogue on justice, the ideal city and the nature of the good — the cornerstone of Western political philosophy.",
            R.drawable.cover_art_republic),
        new Book("walden", "Walden", "Henry David Thoreau",
            "Non-Fiction", "Reflection",
            "#3B5230", "#E7C48B", "1854", 352, 89000,
            "Two years, two months and two days at Walden Pond — a meditation on simplicity, self-reliance and the natural world.",
            R.drawable.cover_art_walden),
        new Book("seneca", "Letters from a Stoic", "Seneca",
            "Non-Fiction", "Stoic Philosophy",
            "#27384F", "#E7C48B", "c. 65", 254, 92000,
            "Warm, practical letters on friendship, mortality and tranquillity — Stoic wisdom that still reads like advice from a wise friend.",
            R.drawable.cover_art_seneca),
        // Fiction
        new Book("pride", "Pride and Prejudice", "Jane Austen",
            "Fiction", "Classic Romance",
            "#7C4A63", "#EBD0AE", "1813", 432, 99000,
            "Elizabeth Bennet and Mr Darcy spar their way toward love in Austen's sparkling comedy of manners, class and first impressions.",
            R.drawable.cover_art_pride),
        new Book("gatsby", "The Great Gatsby", "F. Scott Fitzgerald",
            "Fiction", "Jazz Age",
            "#1F2B47", "#E7C48B", "1925", 180, 89000,
            "A bootlegger's pursuit of a green light across the bay — Fitzgerald's shimmering elegy for the American dream.",
            R.drawable.cover_art_gatsby),
        new Book("janeeyre", "Jane Eyre", "Charlotte Brontë",
            "Fiction", "Gothic",
            "#5A2E33", "#E7C48B", "1847", 532, 115000,
            "An orphan governess, a brooding master and a secret in the attic — Brontë's fierce story of conscience and independence.",
            R.drawable.cover_art_janeeyre),
        new Book("moby", "Moby-Dick", "Herman Melville",
            "Fiction", "Adventure",
            "#374A54", "#E7C48B", "1851", 720, 129000,
            "Call me Ishmael. Captain Ahab's obsessive hunt for the white whale becomes a vast meditation on fate, nature and madness.",
            R.drawable.cover_art_moby)
    );

    public static final List<String> FEATURED_IDS = Arrays.asList("pride", "meditations", "gatsby", "republic");

    public static final List<Store> STORES = Arrays.asList(
        new Store("ivory-tower", "Ivory Tower Books",   R.drawable.store_1,
            "Bandung",   "Jl. Braga No. 18, Bandung",          "+62 812 3344 5566", "09.00 – 21.00"),
        new Store("platos",      "Plato's Atheneum",    R.drawable.store_2,
            "Yogyakarta","Jl. Malioboro No. 52, Yogyakarta",   "+62 877 1122 3344", "10.00 – 22.00"),
        new Store("serpents",    "The Serpent's Archive",R.drawable.store_4,
            "Jakarta",   "Jl. Kemang Raya No. 7, Jakarta",     "+62 813 9988 7766", "08.00 – 20.00"),
        new Store("alexandria",  "Alexandria Branch",   R.drawable.store_5,
            "Surabaya",  "Jl. Tunjungan No. 31, Surabaya",     "+62 856 4455 6677", "09.00 – 21.00")
    );

    public static Book findById(String id) {
        for (Book b : BOOKS) {
            if (b.getId().equals(id)) return b;
        }
        return null;
    }

    public static List<Book> getFeatured() {
        java.util.List<Book> result = new java.util.ArrayList<>();
        for (String id : FEATURED_IDS) {
            Book b = findById(id);
            if (b != null) result.add(b);
        }
        return result;
    }
}
