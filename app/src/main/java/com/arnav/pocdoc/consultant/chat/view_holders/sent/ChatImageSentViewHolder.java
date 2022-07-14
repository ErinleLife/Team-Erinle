package com.arnav.pocdoc.consultant.chat.view_holders.sent;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.databinding.ItemChatImageSentBinding;

public class ChatImageSentViewHolder extends RecyclerView.ViewHolder {

    private final ItemChatImageSentBinding mBinding;

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
        mBinding.setBean(bean);
        mBinding.executePendingBindings();
    }

    public ChatImageSentViewHolder(ItemChatImageSentBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

}