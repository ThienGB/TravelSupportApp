package com.example.mapdemo.ui.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageView;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import java.util.Objects;

public class BindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Picasso.get().load(url).into(view);
        } else {
            view.setImageDrawable(null);
        }
    }

//    @BindingAdapter("text")
//    public static void setText(TextInputEditText view, String text) {
//        if (text != null && !text.equals(Objects.requireNonNull(view.getText()).toString())) {
//            view.setText(text);
//        }
//    }
//
//    @InverseBindingAdapter(attribute = "text")
//    public static String getText(TextInputEditText view) {
//        return Objects.requireNonNull(view.getText()).toString();
//    }
//
//    @BindingAdapter("textAttrChanged")
//    public static void setTextWatcher(TextInputEditText view, final InverseBindingListener textAttrChanged) {
//        if (textAttrChanged == null) {
//            return;
//        }
//
//        view.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                textAttrChanged.onChange();
//            }
//        });
//    }
}
