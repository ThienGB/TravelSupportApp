package com.example.mapdemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.LayoutItemGridBinding;
import com.squareup.picasso.Picasso;


public class CityAdapter extends ListAdapter<City, CityAdapter.PBViewHolder> {
    private final OnItemClickListener listener;
    private final OnItemLongClickListener listener2;

    public CityAdapter(OnItemClickListener listener, OnItemLongClickListener listener2) {
        super(diffCallback);
        this.listener = listener;
        this.listener2 = listener2;
    }

    private static final DiffUtil.ItemCallback<City> diffCallback = new DiffUtil.ItemCallback<City>() {
        @Override
        public boolean areItemsTheSame(@NonNull City oldItem, @NonNull City newItem) {
            return oldItem.getIdCity().equals(newItem.getIdCity());
        }

        @Override
        public boolean areContentsTheSame(@NonNull City oldItem, @NonNull City newItem) {
            return (areItemsTheSame (oldItem, newItem)
                    && (oldItem.getName().equals(newItem.getName())
                    && oldItem.getImage().equals(newItem.getImage()))
            );
        }
    };

    @NonNull
    @Override
    public PBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemGridBinding binding = LayoutItemGridBinding.inflate(inflater, parent, false);
        return new PBViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PBViewHolder holder, int position) {
        City currentItem = getItem(position);
        holder.bind(currentItem);
    }

    public interface OnItemClickListener {
        void onItemClick(City city);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(City city);
    }

    class PBViewHolder extends RecyclerView.ViewHolder {
        private final LayoutItemGridBinding binding;

        public PBViewHolder(LayoutItemGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && CityAdapter.this.listener != null) {
                    City city = getItem(position);
                    if (city != null)
                        CityAdapter.this.listener.onItemClick(city);
                }
            });
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && CityAdapter.this.listener != null) {
                    City city = getItem(position);
                    if (city != null)
                        CityAdapter.this.listener2.onItemLongClick(city);
                    return true;
                }
                return false;
            });
        }
        public void bind(City city) {
            binding.tvxName.setText(city.getName());
            Picasso.get().load(city.getImage()).into(binding.imgCity);
        }
    }
}

