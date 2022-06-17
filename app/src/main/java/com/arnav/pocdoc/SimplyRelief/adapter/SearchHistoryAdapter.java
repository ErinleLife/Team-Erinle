package com.arnav.pocdoc.SimplyRelief.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;

import java.util.List;


public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    private static final String TAG = "SearchHistoryAdapter";

    private final List<String> itemList;
    private final Context mContext;
    private final ItemSelectListener itemSelectListener;

    public SearchHistoryAdapter(Context mContext, List<String> itemList, ItemSelectListener itemSelectListener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.itemSelectListener = itemSelectListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = itemList.get(position);
        holder.txtName.setText(item);

        holder.layoutMain.setOnClickListener(v -> {
            itemSelectListener.onItemSelected(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface ItemSelectListener {
        void onItemSelected(String item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutMain;
        TextView txtName;
//        ImageView imgRemove;

        public ViewHolder(View view) {
            super(view);

            layoutMain = view.findViewById(R.id.layoutMain);
            txtName = view.findViewById(R.id.txtName);
//            imgRemove = view.findViewById(R.id.imgRemove);
        }
    }

}
