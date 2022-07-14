package com.arnav.pocdoc.hospitalLocator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class HospitallLocatorUploadImageAdapter extends RecyclerView.Adapter<HospitallLocatorUploadImageAdapter.ViewHolder> {

    ArrayList<String> uploadImageArray;
    Activity activity;

    public HospitallLocatorUploadImageAdapter(ArrayList<String> uploadImageArray, Activity activity) {
        this.uploadImageArray = uploadImageArray;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_locator_uploadimage_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(activity)
                .load(uploadImageArray.get(position))
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploadImageArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ShapeableImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_upload_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == itemView) {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition(), Constants.ITEM_CLICK, view);
                }
            }
        }
    }

    private RecyclerViewItemClickListener listener;

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        listener = recyclerViewItemClickListener;
    }
}
