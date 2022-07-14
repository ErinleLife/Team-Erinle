package com.arnav.pocdoc.symptomchecker.adapter;

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
import com.arnav.pocdoc.SimplyRelief.models.DataReliefItem;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

public class SymptomsAdapter extends RecyclerView.Adapter<SymptomsAdapter.ViewHolder> {
    private final List<DataReliefItem> list;
    private final Context context;

    public RecyclerViewItemClickListener listener;

    public SymptomsAdapter(List<DataReliefItem> list, Context context) {
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
        holder.checkbox.setText(list.get(position).getSymptomGroup());
        Glide.with(context)
                .load(list.get(position).getImage())
                .into(holder.ivImage);
        if (list.get(position).isSelect()) {
            holder.ivBack.setBackgroundResource(R.drawable.card_bg_med);
        } else {
            holder.ivBack.setBackgroundResource(R.drawable.card_bg_med_blanck);
        }
        holder.itemView.setOnClickListener(v -> {
            if (position != RecyclerView.NO_POSITION) {
                if (listener != null) {
                    listener.onItemClick(position, 0, v);
                }
            }
            holder.ivBack.setBackgroundResource(R.drawable.card_bg_med);
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