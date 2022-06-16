package com.arnav.pocdoc.hospitalLocator;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.R;
import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class HospitallLocatorSendScreenUploadImageAdapter extends RecyclerView.Adapter<HospitallLocatorSendScreenUploadImageAdapter.ViewHolder> {

    ArrayList<String> sendScreen_uploadImageArray;
    Activity activity;

    public HospitallLocatorSendScreenUploadImageAdapter(ArrayList<String> sendScreen_uploadImageArray, Activity activity) {
        this.sendScreen_uploadImageArray = sendScreen_uploadImageArray;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_locator_uploadimage_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(activity)
                .load(sendScreen_uploadImageArray.get(position))
                .centerCrop()
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return sendScreen_uploadImageArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_upload_image);
        }
    }
}
