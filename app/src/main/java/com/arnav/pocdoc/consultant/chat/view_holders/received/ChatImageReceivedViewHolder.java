package com.arnav.pocdoc.consultant.chat.view_holders.received;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.databinding.ItemChatImageReceivedBinding;

public class ChatImageReceivedViewHolder extends RecyclerView.ViewHolder {

    private final ItemChatImageReceivedBinding mBinding;
    public String baseURL;

    public void bind(DataChat bean) {
        mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ChatItem chatItem = new ChatItem();
//                chatItem.setMedia(bean.getMedia());
//                chatItem.setType(Constants.IMAGE);
//                chatItem.setGroup(bean.isGroup);
//                chatItem.setMe(BaseApplication.preference.getUToken().equals(bean.getSender_token()));
//                EventBus.getDefault().post(chatItem);
            }
        });
        mBinding.setBaseURL(baseURL);
        mBinding.setBean(bean);
        mBinding.executePendingBindings();
    }

    public ChatImageReceivedViewHolder(ItemChatImageReceivedBinding binding, String baseURL) {
        super(binding.getRoot());
        mBinding = binding;
        this.baseURL = baseURL;
    }
}