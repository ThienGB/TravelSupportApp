package com.example.bt1buoi6quanlypb.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bt1buoi6quanlypb.R;
import com.example.bt1buoi6quanlypb.data.model.PhongBan;

import java.util.List;

public class PhongBanAdapter extends ArrayAdapter<PhongBan> {

    private Activity context;
    private int layoutId;
    private List<PhongBan> arrPhongBan;

    public PhongBanAdapter(Activity context, int textViewResourceId, List<PhongBan> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.layoutId = textViewResourceId;
        this.arrPhongBan = objects;
    }

    public void setData(List<PhongBan> arrPhongBan) {
        this.arrPhongBan = arrPhongBan;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtpb = convertView.findViewById(R.id.txtShortInfo);
            viewHolder.txtmotapb = convertView.findViewById(R.id.txtDetalInfo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get PhongBan object for this position
        PhongBan pb = arrPhongBan.get(position);

        viewHolder.txtpb.setText(pb.getTenpb());

        return convertView;
    }

    static class ViewHolder {
        TextView txtpb;
        TextView txtmotapb;
    }
}
