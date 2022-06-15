package com.arnav.pocdoc.symptomchecker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.Recomendation;
import com.arnav.pocdoc.databinding.RowRecommendationBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class PreventionAdapter extends RecyclerView.Adapter<PreventionAdapter.ViewHolder> {
    private final List<Recomendation> list;
    public Activity activity;
    private RecyclerViewItemClickListener listener;

    public PreventionAdapter(Activity activity, List<Recomendation> list) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public PreventionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowRecommendationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_recommendation, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PreventionAdapter.ViewHolder holder, int position) {
        holder.binding.tvTitle.setText(list.get(position).getName());
        if(list.get(position).getFrontImage() != null && !list.get(position).getFrontImage().equals("")) {
            Glide.with(activity)
                    .load(list.get(position).getFrontImage())
                    .apply(new RequestOptions().dontAnimate())
                    .into(holder.binding.iv);
            holder.binding.ivDelete.setVisibility(View.VISIBLE);
            holder.binding.iv.setVisibility(View.VISIBLE);
        }else{
            holder.binding.iv.setImageResource(R.drawable.app_icon);
            holder.binding.ivDelete.setVisibility(View.GONE);
            holder.binding.iv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        listener = recyclerViewItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowRecommendationBinding binding;

        public ViewHolder(@NonNull RowRecommendationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
            binding.ivDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.ivDelete) {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition(), 22, v);
                }
            } else {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition(), 11, v);
                }
            }

        }
    }
}
