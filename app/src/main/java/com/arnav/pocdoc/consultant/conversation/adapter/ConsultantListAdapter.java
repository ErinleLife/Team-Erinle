package com.arnav.pocdoc.consultant.conversation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.data.model.conversation.DataConversation;
import com.arnav.pocdoc.databinding.RowConsultantListBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.List;

public class ConsultantListAdapter extends RecyclerView.Adapter<ConsultantListAdapter.MyViewHolder> {
    private final Context context;
    private final List<DataConversation> list;
    public String baseURL = "";

    public ConsultantListAdapter(Context context, List<DataConversation> list) {
        this.context = context;
        this.list = list;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowConsultantListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_consultant_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataConversation data = list.get(position);
        if (data.getImage() != null && !data.getImage().equals("")) {
            Glide.with(context)
                    .load(baseURL + data.getImage())
                    .placeholder(R.drawable.ic_logo)
                    .into(holder.binding.ivProfile);
        } else {
            holder.binding.ivProfile.setImageResource(R.drawable.ic_logo);
        }
        holder.binding.tvTitle.setText(data.getName());
        holder.binding.tvTime.setText(data.getTime());
        holder.binding.tvMessage.setText(data.getLastMessage());
        holder.binding.tvMessage.setVisibility(data.getLastMessage() == null || data.getLastMessage().equals("") ? View.GONE : View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RowConsultantListBinding binding;

        MyViewHolder(RowConsultantListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition(), Constants.ITEM_CLICK, v);
                }
            }
        }
    }

    private RecyclerViewItemClickListener listener;

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        listener = recyclerViewItemClickListener;
    }
}
