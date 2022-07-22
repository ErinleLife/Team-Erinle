package com.arnav.pocdoc.consultant.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arnav.pocdoc.base.BaseApplication;
import com.arnav.pocdoc.consultant.chat.view_holders.ChatHeaderViewHolder;
import com.arnav.pocdoc.consultant.chat.view_holders.received.ChatImageReceivedViewHolder;
import com.arnav.pocdoc.consultant.chat.view_holders.received.ChatTextReceivedViewHolder;
import com.arnav.pocdoc.consultant.chat.view_holders.sent.ChatImageSentViewHolder;
import com.arnav.pocdoc.consultant.chat.view_holders.sent.ChatTextSentViewHolder;
import com.arnav.pocdoc.data.model.chat.DataChat;
import com.arnav.pocdoc.databinding.ItemChatHeaderBinding;
import com.arnav.pocdoc.databinding.ItemChatImageReceivedBinding;
import com.arnav.pocdoc.databinding.ItemChatImageSentBinding;
import com.arnav.pocdoc.databinding.ItemChatTextReceivedBinding;
import com.arnav.pocdoc.databinding.ItemChatTextSentBinding;
import com.arnav.pocdoc.implementor.RecyclerViewItemClickListener;
import com.arnav.pocdoc.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;
    private final List<DataChat> mBeans;

    private static final int CHAT_HEADER = 1;
    private static final int CHAT_TEXT_RECEIVED = 2;
    private static final int CHAT_TEXT_SENT = 3;
    private static final int CHAT_IMAGE_RECEIVED = 4;
    private static final int CHAT_IMAGE_SENT = 5;
    public String baseURL = "";

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public ChatAdapter(Context context) {
        mContext = context;
        mBeans = new ArrayList<>();
    }

    private RecyclerViewItemClickListener listener;

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        listener = recyclerViewItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CHAT_HEADER:
                return new ChatHeaderViewHolder(ItemChatHeaderBinding.inflate(LayoutInflater.from(mContext), parent, false));
            case CHAT_IMAGE_RECEIVED:
                return new ChatImageReceivedViewHolder(ItemChatImageReceivedBinding.inflate(LayoutInflater.from(mContext), parent, false), baseURL);
            case CHAT_IMAGE_SENT:
                return new ChatImageSentViewHolder(ItemChatImageSentBinding.inflate(LayoutInflater.from(mContext), parent, false), baseURL);
            case CHAT_TEXT_RECEIVED:
                return new ChatTextReceivedViewHolder(ItemChatTextReceivedBinding.inflate(LayoutInflater.from(mContext), parent, false));
            case CHAT_TEXT_SENT:
            default:
                return new ChatTextSentViewHolder(ItemChatTextSentBinding.inflate(LayoutInflater.from(mContext), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DataChat bean = mBeans.get(position);
        if (holder instanceof ChatHeaderViewHolder) {
            ((ChatHeaderViewHolder) holder).bind(bean);
        } else if (holder instanceof ChatTextReceivedViewHolder) {
            ((ChatTextReceivedViewHolder) holder).bind(bean);
        } else if (holder instanceof ChatTextSentViewHolder) {
            ((ChatTextSentViewHolder) holder).bind(bean);
        } else if (holder instanceof ChatImageReceivedViewHolder) {
            ((ChatImageReceivedViewHolder) holder).bind(bean);
        } else if (holder instanceof ChatImageSentViewHolder) {
            ((ChatImageSentViewHolder) holder).bind(bean);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (mBeans.get(position).getType()) {
            case Constants.HEADER:
                return CHAT_HEADER;
            case Constants.IMAGE:
                if (isMe(position))
                    return CHAT_IMAGE_SENT;
                else
                    return CHAT_IMAGE_RECEIVED;
            case Constants.TEXT:
            default:
                if (isMe(position))
                    return CHAT_TEXT_SENT;
                else
                    return CHAT_TEXT_RECEIVED;
        }
    }

    private boolean isMe(int position) {
        return BaseApplication.preferences.getUserId().equals(String.valueOf(mBeans.get(position).getFrom_id()));
    }

    @Override
    public int getItemCount() {
        return mBeans.size();
    }

    public void addItems(List<DataChat> messageList) {
        mBeans.addAll(messageList);
        notifyDataSetChanged();
    }

    public void addItem(DataChat dataChat) {
        mBeans.add(0, dataChat);
        notifyDataSetChanged();
    }

    public DataChat getItemAtPosition(int position) {
        return mBeans.get(position);
    }
}