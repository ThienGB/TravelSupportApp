package com.example.mapdemo.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapdemo.R;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.databinding.LayoutItemGridFavoriteBinding;
import com.squareup.picasso.Picasso;


public class FavoriteAdapter extends ListAdapter<Accommodation, FavoriteAdapter.MyViewHolder> {
    private final OnItemClickListener listener;
    private final OnFavoriteListener listener2;

    public FavoriteAdapter(OnItemClickListener listener, OnFavoriteListener listener2) {
        super(diffCallback);
        this.listener = listener;
        this.listener2 = listener2;
    }

    private static final DiffUtil.ItemCallback<Accommodation> diffCallback = new DiffUtil.ItemCallback<Accommodation>() {
        @Override
        public boolean areItemsTheSame(@NonNull Accommodation oldItem, @NonNull Accommodation newItem) {
            return oldItem.getAccommodationId().equals(newItem.getAccommodationId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Accommodation oldItem, @NonNull Accommodation newItem) {
            return oldItem.getAccommodationId().equals(newItem.getAccommodationId());
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemGridFavoriteBinding binding = LayoutItemGridFavoriteBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Accommodation currentItem = getItem(position);
        holder.bind(currentItem);
    }

    public interface OnItemClickListener {
        void onItemClick(Accommodation accommodation);
    }
    public interface OnFavoriteListener {
        void onFavoriteClick(Accommodation accommodation, boolean isFavorite);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final LayoutItemGridFavoriteBinding binding;

        @SuppressLint("ClickableViewAccessibility")
        public MyViewHolder(LayoutItemGridFavoriteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    if (position != RecyclerView.NO_POSITION && FavoriteAdapter.this.listener != null) {
                        Accommodation accommodation = getItem(position);
                        boolean isFavorite;
                        if (binding.btnFavorite.getDrawable().getConstantState().equals(
                                ContextCompat.getDrawable(v.getContext(), R.drawable.icon_favorite).getConstantState())) {
                            isFavorite = false;
                            binding.btnFavorite.setImageResource(R.drawable.icon_not_favorite);
                        } else {
                            isFavorite = true;
                            binding.btnFavorite.setImageResource(R.drawable.icon_favorite);
                        }
                        FavoriteAdapter.this.listener2.onFavoriteClick(accommodation, isFavorite);
                    }
                }
            });
            binding.btnFavorite.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;  // false để sự kiện click vẫn được xử lý bởi OnClickListener
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && FavoriteAdapter.this.listener != null) {
                        Accommodation accommodation = getItem(position);
                        FavoriteAdapter.this.listener.onItemClick(accommodation);
                    }
                }
            });

        }
        public void bind(Accommodation accommodation) {
            binding.tvxName.setText(accommodation.getName());
            binding.txvAddress.setText(accommodation.getAddress());
            Picasso.get().load(accommodation.getImage()).into(binding.imgAccom);
            binding.btnFavorite.setImageResource(R.drawable.icon_favorite);
        }
    }
}

