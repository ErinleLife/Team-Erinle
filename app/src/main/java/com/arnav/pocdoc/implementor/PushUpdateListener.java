package com.arnav.pocdoc.implementor;

import com.arnav.pocdoc.data.model.chat.DataChat;

public interface PushUpdateListener {
    void onPushReceived(DataChat dataChat);
}
