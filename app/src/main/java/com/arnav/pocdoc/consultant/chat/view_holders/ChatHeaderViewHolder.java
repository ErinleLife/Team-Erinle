package com.arnav.pocdoc.consultant.chat.view_holders;

import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.databinding.ItemChatHeaderBinding;
import com.arnav.pocdoc.utils.Constants;
import com.arnav.pocdoc.utils.Utils;

public class ChatHeaderViewHolder extends RecyclerView.ViewHolder {

    private final ItemChatHeaderBinding mBinding;

    public void bind(DataChat bean) {
        mBinding.setBean(bean);
        mBinding.text.setText(Utils.GetDateOnRequireFormat(bean.getCreated_at(), Constants.DATE_YYYY_MM_DD_HH_MM_AA_FORMAT, Constants.DATE_EEE_DD_MM_FORMAT));

        mBinding.executePendingBindings();
    }

    public ChatHeaderViewHolder(ItemChatHeaderBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
    }

}