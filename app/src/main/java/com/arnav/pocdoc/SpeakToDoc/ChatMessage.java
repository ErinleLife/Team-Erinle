package com.arnav.pocdoc.SpeakToDoc;

/**
 * Created by Yomna on 12/1/2017.
 */

public class ChatMessage {
    public int isMine;
    public String message;

    public ChatMessage(int isMine, String message) {
        super();
        this.message = message;
        if (message.equals("Are you male or female?")) {
            this.isMine = 2;
        } else {
            if (message.length() > 4 && message.startsWith("Here")) {
                this.isMine = 3;
            } else {
                if (message.length() > 4 && message.startsWith("Your")) {
                    this.isMine = 4;
                } else {
                    if (message.length() > 15 && !message.startsWith("How old are you") && message.endsWith("?") && message.charAt(0) != 'W') {
                        this.isMine = 5;
                    } else {
                        if (message.length() > 9 && message.startsWith("conditions")) {
                            this.isMine = 6;
                        } else
                            this.isMine = isMine;
                    }
                }
            }
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int isMine() {
        return isMine;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }
}
