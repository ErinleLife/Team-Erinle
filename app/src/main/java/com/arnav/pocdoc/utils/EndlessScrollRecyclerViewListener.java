package com.arnav.pocdoc.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessScrollRecyclerViewListener extends RecyclerView.OnScrollListener {
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    private int currentPage = 0;

    @Override
    public void onScrolled(@NonNull RecyclerView mRecyclerView, int dx, int dy) {
        super.onScrolled(mRecyclerView, dx, dy);
//        LogUtils.Print("EndlessScrollRecyclerViewListener", "onScrolled:- ");
        LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView
                .getLayoutManager();

        assert mLayoutManager != null;
        int visibleItemCount = mLayoutManager.getChildCount();
        int totalItemCount = mLayoutManager.getItemCount();
        int pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
//        LogUtils.Print("visibleItemCount",""+visibleItemCount);
//        LogUtils.Print("totalItemCount:- ",""+totalItemCount);
//        LogUtils.Print("pastVisibleItems:- ",""+pastVisibleItems);
        onScroll(pastVisibleItems, visibleItemCount, totalItemCount);
    }

    private void onScroll(int pastVisibleItems, int visibleItemCount, int totalItemCount) {
        if (pastVisibleItems + visibleItemCount >= totalItemCount) {
            onLoadMore();
        }
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore();

}