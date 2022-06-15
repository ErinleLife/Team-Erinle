package com.arnav.pocdoc.symptomchecker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.arnav.pocdoc.R;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;

import java.util.List;

public class ImagesPagerAdapter extends PagerAdapter {

    private final List<String> list;
    private final LayoutInflater layoutInflater;
    private final Activity activity;

    public ImagesPagerAdapter(List<String> drawables, Activity activity) {
        this.list = drawables;
        this.activity = activity;
        layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.row_images_pager, null);
        container.addView(view);
        ImageViewHolder viewHolder = new ImageViewHolder(view);

        Glide.with(view.getContext())
                .load(list.get(position))
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .override(0, 0)
                .into(viewHolder.imageView);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;

        ImageView imageView = view.findViewById(R.id.image);
        imageView.setImageResource(0);

        container.removeView(view);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private class ImageViewHolder {
        private final ImageView imageView;

        ImageViewHolder(View view) {
            imageView = view.findViewById(R.id.image);

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(activity);
            imageView.setOnTouchListener(imageMatrixTouchHandler);
        }
    }
}