package com.arnav.pocdoc.consultant.chat.view_holders.sent;

import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.databinding.ItemChatTextSentBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;

public class ChatTextSentViewHolder extends RecyclerView.ViewHolder {

    private final ItemChatTextSentBinding mBinding;

    public void bind(DataChat bean) {
        mBinding.setBean(bean);
        mBinding.executePendingBindings();
    }

    public ChatTextSentViewHolder(ItemChatTextSentBinding binding, RecyclerViewItemClickListener listener) {
        super(binding.getRoot());
        mBinding = binding;
    }
}