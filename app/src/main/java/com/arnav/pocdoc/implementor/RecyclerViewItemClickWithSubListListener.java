package com.arnav.pocdoc.implementor;

import android.view.View;

public interface RecyclerViewItemClickWithSubListListener {
    public void onItemClick(int position, int subPostion, int flag, View view);
}