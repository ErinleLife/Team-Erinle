package com.arnav.pocdoc.consultant.newconversation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.data.model.cosultantlist.DataConsultant;
import com.arnav.pocdoc.data.network.APIClient;
import com.arnav.pocdoc.databinding.RowConsultantListBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.List;

public class NewConsultantListAdapter extends RecyclerView.Adapter<NewConsultantListAdapter.MyViewHolder> {
    private final Context context;
    private final List<DataConsultant> list;

    public NewConsultantListAdapter(Context context, List<DataConsultant> list) {
        this.context = context;
        this.list = list;
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
        DataConsultant data = list.get(position);
        if (data.getAvatar() != null && !data.getAvatar().equals("")) {
            Glide.with(context)
                    .load(APIClient.USER_PROFILE + data.getAvatar())
                    .placeholder(R.drawable.ic_logo)
                    .into(holder.binding.ivProfile);
        } else {
            holder.binding.ivProfile.setImageResource(R.drawable.ic_logo);
        }
        holder.binding.tvTitle.setText(data.getName());
        holder.binding.tvTime.setText(Utils.GetDateOnRequireFormat(data.getCreatedAt(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_HH_MM_AA_FORMAT));
        holder.binding.tvMessage.setText(data.getBody());
        holder.binding.tvMessage.setVisibility(data.getBody() == null || data.getBody().equals("") ? View.GONE : View.VISIBLE);
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
