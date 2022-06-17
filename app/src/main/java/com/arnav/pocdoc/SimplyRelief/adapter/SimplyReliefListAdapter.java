package com.arnav.pocdoc.SimplyRelief.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.SimplyRelief.models.DataReliefItem;

import java.util.List;


public class SimplyReliefListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_LOADING = 0;
    private static final int TYPE_DATA = 1;
    public OnItemClick listener;
    Context context;
    private final List<DataReliefItem> details;

    public SimplyReliefListAdapter(Context context, List<DataReliefItem> events, OnItemClick listener) {
        this.context = context;
        this.details = events;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADING:
                return new LoadingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loading, parent, false));

            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            DataReliefItem item = details.get(position);
            viewHolder.txtName.setText(item.getPrimarySymptom());
            viewHolder.txtGroup.setText(item.getSymptomGroup());

            viewHolder.layoutMain.setOnClickListener(v -> {
                listener.itemClicked(item);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        DataReliefItem item = details.get(position);
        if (item == null) {
            return TYPE_LOADING;
        } else {
            return TYPE_DATA;
        }
    }

    @Override
    public int getItemCount() {
        if (details == null) return 0;
        return details.size();
    }

    public interface OnItemClick {
        void itemClicked(DataReliefItem detail);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutMain;
        TextView txtName, txtGroup;
//        ImageView imgRemove;

        public ViewHolder(View view) {
            super(view);

            layoutMain = view.findViewById(R.id.layoutMain);
            txtName = view.findViewById(R.id.txtName);
            txtGroup = view.findViewById(R.id.txtGroup);
//            imgRemove = view.findViewById(R.id.imgRemove);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}