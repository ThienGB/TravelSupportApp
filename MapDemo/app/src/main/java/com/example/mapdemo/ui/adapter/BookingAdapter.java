package com.example.mapdemo.ui.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mapdemo.data.model.Accommodation;
import com.example.mapdemo.data.model.Booking;
import com.example.mapdemo.databinding.LayoutItemGridBookingBinding;
import com.example.mapdemo.ui.viewmodel.UserBookingListViewModel;
import com.squareup.picasso.Picasso;

public class BookingAdapter extends ListAdapter<Booking, BookingAdapter.MyViewHolder> {
    private final OnItemClickListener listener;
    private final OnItemLongClickListener listener2;
    UserBookingListViewModel userBookingListViewModel;

    public BookingAdapter(OnItemClickListener listener, OnItemLongClickListener listener2,
                          UserBookingListViewModel userBookingListViewModel) {
        super(diffCallback);
        this.listener = listener;
        this.listener2 = listener2;
        this.userBookingListViewModel = userBookingListViewModel;
    }

    private static final DiffUtil.ItemCallback<Booking> diffCallback = new DiffUtil.ItemCallback<Booking>() {
        @Override
        public boolean areItemsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.getIdBooking().equals(newItem.getIdBooking());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Booking oldItem, @NonNull Booking newItem) {
            return oldItem.getIdBooking().equals(newItem.getIdBooking());
        }
    };

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        LayoutItemGridBookingBinding binding = LayoutItemGridBookingBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Booking currentItem = getItem(position);
        holder.bind(currentItem);
    }

    public interface OnItemClickListener {
        default void onItemClick(){}
    }
    public interface OnItemLongClickListener {
        default void onItemLongClick(){}
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private final LayoutItemGridBookingBinding binding;

        public MyViewHolder(LayoutItemGridBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && BookingAdapter.this.listener != null) {
                    BookingAdapter.this.listener.onItemClick();
                }
            });
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && BookingAdapter.this.listener != null) {
                    BookingAdapter.this.listener2.onItemLongClick();
                    return true;
                }
                return false;
            });
        }
        public void bind(Booking booking) {
            String target = booking.getIdTarget();
            Accommodation accommodation = userBookingListViewModel.getAccomById(target);
            binding.txvAccomName.setText(accommodation.getName());
            String numOfRoom;
            if (booking.getNumOfRooms()>1) {
                numOfRoom= booking.getNumOfRooms() + " rooms";
            } else {
                numOfRoom= booking.getNumOfRooms() + " room";
            }
            binding.txvNumOfRoom.setText(numOfRoom);
            binding.txvAddress.setText(accommodation.getAddress());
            binding.txvDescription.setText(accommodation.getDescription());
            binding.txvPrice.setText(String.valueOf(booking.getPrice()));
            binding.txvBookingDate.setText(booking.getStartDay().toString());
            binding.txvCheckOutDate.setText(booking.getEndDay().toString());
            Picasso.get().load(accommodation.getImage()).into(binding.imgAccom);
        }
    }
}

