package com.arnav.pocdoc.otc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataOTCItem;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

public class OTCAdapter extends RecyclerView.Adapter<OTCAdapter.ViewHolder> {
    private final List<DataOTCItem> list;
    private final Context context;

    public RecyclerViewItemClickListener listener;

    public OTCAdapter(List<DataOTCItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_symptoms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkbox.setText(list.get(position).getTitle());
        Glide.with(context)
                .load(list.get(position).getImage())
                .into(holder.ivImage);
        holder.ivBack.setBackgroundResource(R.drawable.card_bg_med);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position, 0, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView checkbox;
        private final ConstraintLayout ivBack;
        private final ImageView ivImage;

        ViewHolder(@NonNull View view) {
            super(view);
            ivImage = view.findViewById(R.id.ivImage);
            ivBack = view.findViewById(R.id.ivBack);
            checkbox = view.findViewById(R.id.checkbox);
        }
    }
}