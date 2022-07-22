package com.arnav.pocdoc.symptomchecker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.databinding.RowStatementsBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.symptomchecker.AddSymptomActivity;
import com.arnav.pocdoc.symptomchecker.data.DataStatement;

import java.util.List;

public class StatementsAdapter extends RecyclerView.Adapter<StatementsAdapter.ViewHolder> {
    private final List<DataStatement> list;
    private RecyclerViewItemClickListener listener;

    public StatementsAdapter(List<DataStatement> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public StatementsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowStatementsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_statements, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StatementsAdapter.ViewHolder holder, int position) {
        holder.binding.tvTitle.setText(list.get(position).getTitle());
        holder.binding.rGroup.check(holder.binding.rGroup.getChildAt(list.get(position).getSelectedItem()).getId());
        if (position == 5) {
            if (AddSymptomActivity.userGender.toLowerCase().equals(AddSymptomActivity.userGenderFemale)) {
                holder.itemView.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
        } else if (position == 10) {
            if (list.get(9).getSelectedItem() == 0) {
                holder.itemView.setVisibility(View.VISIBLE);
            } else {
                holder.itemView.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        listener = recyclerViewItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RowStatementsBinding binding;

        public ViewHolder(@NonNull RowStatementsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.rGroup.setOnCheckedChangeListener((rGroup, checkedId) -> {
                int radioBtnID = rGroup.getCheckedRadioButtonId();
                View radioB = rGroup.findViewById(radioBtnID);
                int position = binding.rGroup.indexOfChild(radioB);
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition(), position, radioB);
                }
            });
        }
    }
}