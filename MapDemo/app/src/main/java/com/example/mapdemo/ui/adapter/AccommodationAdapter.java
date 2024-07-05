package com.example.mapdemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.data.model.City;
import com.example.mapdemo.databinding.LayoutItemGridAccomBinding;
import com.example.mapdemo.databinding.LayoutItemGridBinding;
import com.squareup.picasso.Picasso;


public class AccommodationAdapter extends ListAdapter<Accommodation, AccommodationAdapter.MyViewHolder> {
    private OnItemClickListener listener;
    private OnItemLongClickListener listener2;

    public AccommodationAdapter(OnItemClickListener listener, OnItemLongClickListener listener2) {
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
        LayoutItemGridAccomBinding binding = LayoutItemGridAccomBinding.inflate(inflater, parent, false);
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
    public interface OnItemLongClickListener {
        void onItemLongClick(Accommodation accommodation);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemGridAccomBinding binding;

        public MyViewHolder(LayoutItemGridAccomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && AccommodationAdapter.this.listener != null) {
                        Accommodation accommodation = getItem(position);
                        AccommodationAdapter.this.listener.onItemClick(accommodation);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && AccommodationAdapter.this.listener != null) {
                        Accommodation accommodation = getItem(position);
                        AccommodationAdapter.this.listener2.onItemLongClick(accommodation);
                        return true;
                    }
                    return false;
                }
            });
        }
        public void bind(Accommodation accommodation) {
            binding.tvxName.setText(accommodation.getName());
            String freeRoomStr ="";
            int freeRoom = accommodation.getFreeroom();
            if (freeRoom == 0){
                freeRoomStr = "Out of room";
            }else if (freeRoom == 1){
                freeRoomStr = "1 room left";
            }else {
                freeRoomStr = freeRoom + " rooms left";
            }
            binding.txvFreeRoom.setText(freeRoomStr);
            Picasso.get().load(accommodation.getImage()).into(binding.imgAccom);
        }
    }
}

