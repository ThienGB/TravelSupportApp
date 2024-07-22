package com.example.mapdemo.ui.adapter;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class BindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url).into(view);
        } else {
            view.setImageDrawable(null);
        }
    }
}
