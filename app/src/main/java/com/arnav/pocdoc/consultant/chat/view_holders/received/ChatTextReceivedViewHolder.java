package com.arnav.pocdoc.consultant.chat.view_holders.received;

import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.databinding.ItemChatTextReceivedBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;

public class ChatTextReceivedViewHolder extends RecyclerView.ViewHolder {

    private final ItemChatTextReceivedBinding mBinding;

    public void bind(DataChat bean) {
        mBinding.setBean(bean);
        mBinding.executePendingBindings();
    }

    public ChatTextReceivedViewHolder(ItemChatTextReceivedBinding binding, RecyclerViewItemClickListener listener) {
        super(binding.getRoot());
        mBinding = binding;
    }
}