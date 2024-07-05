package com.example.bt1buoi6quanlypb.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt1buoi6quanlypb.data.model.PhongBan;
import com.example.bt1buoi6quanlypb.databinding.LayoutItemGridBinding;

public class PhongBanListAdapter extends ListAdapter<PhongBan, PhongBanListAdapter.PBViewHolder> {
    private OnItemClickListener listener;

    public PhongBanListAdapter(OnItemClickListener listener) {
        super(diffCallback);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<PhongBan> diffCallback = new DiffUtil.ItemCallback<PhongBan>() {
        @Override
        public boolean areItemsTheSame(@NonNull PhongBan oldItem, @NonNull PhongBan newItem) {
            return oldItem.getMa().equals(newItem.getMa());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PhongBan oldItem, @NonNull PhongBan newItem) {
            return oldItem.getMa().equals(newItem.getMa());
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
        PhongBan currentItem = getItem(position);
        holder.bind(currentItem);
    }

    public interface OnItemClickListener {
        void onItemClick(PhongBan phongBan);
    }

    class PBViewHolder extends RecyclerView.ViewHolder {
        private LayoutItemGridBinding binding;

        public PBViewHolder(LayoutItemGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && PhongBanListAdapter.this.listener != null) {
                        PhongBan phongBan = getItem(position);
                        PhongBanListAdapter.this.listener.onItemClick(phongBan);
                    }
                }
            });
        }

        public void bind(PhongBan phongBan) {
            binding.tvxName.setText(phongBan.getTenpb());
        }
    }
}

