package com.example.mapdemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapdemo.R;

import java.util.List;

public class IndicatorAdapter extends RecyclerView.Adapter<IndicatorAdapter.ViewHolder> {
    private final Context context;
    private final int itemCount;
    private int currentPosition = 0;

    public IndicatorAdapter(Context context, int itemCount) {
        this.context = context;
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_indicator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Hiển thị dấu chấm hoặc chỉ số ở đây
        // Ví dụ: Dấu chấm có thể là một hình tròn, với chỉ số thay đổi màu sắc khi được chọn
        // Trong trường hợp này, chỉ đơn giản hiển thị số thứ tự

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layIndicator);
        }

        void bind(int position) {
            if (position == currentPosition) {
                layout.setBackgroundResource(android.R.drawable.button_onoff_indicator_on);
            } else {
                layout.setBackgroundResource(android.R.drawable.button_onoff_indicator_off);
            }
        }
    }
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }
}
