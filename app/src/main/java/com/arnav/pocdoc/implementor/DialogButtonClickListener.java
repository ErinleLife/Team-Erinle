package com.arnav.pocdoc.implementor;

public interface DialogButtonClickListener {
    void onPositiveButtonClick();

    default void onNegativeButtonClick() {
    }

}
