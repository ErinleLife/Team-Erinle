package com.arnav.pocdoc.utils;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.arnav.pocdoc.R;
import com.arnav.pocdoc.implementor.DialogButtonClickListener;
import com.arnav.pocdoc.implementor.NaturalDialogButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {

    public static void showLogoutConformationDialog(Context context, final DialogButtonClickListener listener) {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(context);
        dialogBuilder.setTitle(context.getResources().getString(R.string.app_name));
        dialogBuilder.setIcon(R.mipmap.ic_launcher_round);
        dialogBuilder.setMessage(context.getResources().getString(R.string.dialog_logout));
        dialogBuilder.setNegativeButton(
                context.getResources().getString(R.string.no),
                (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onNegativeButtonClick();

                });
        dialogBuilder.setPositiveButton(
                context.getResources().getString(R.string.yes),
                (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onPositiveButtonClick();
                });
        AlertDialog alert = dialogBuilder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }


    /*  public static void showConfirmationDialog(Context context, final DialogButtonClickListener listener,
                                                String strMsg, String strPositive, String strNegative) {
          showCommonConfirmationDialog(context, listener, strMsg,
                  strPositive,
                  strNegative);
      }*/
    public static void showRequestConfirmationDialog(Context context, final DialogButtonClickListener listener,
                                                     String strMsg) {
//        showCommonConfirmationDialog(context, listener, strMsg,
//                context.getResources().getString(R.string.text_yes),
//                context.getResources().getString(R.string.text_no));
    }

    public static void showConfirmationDialog(Context context, final DialogButtonClickListener listener,
                                              String strMsg, String strPositive, String strNegative) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage(strMsg);
        builder.setPositiveButton(
                strPositive,
                (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onPositiveButtonClick();
                });
        builder.setNegativeButton(
                strNegative,
                (dialog, which) -> {

                    dialog.dismiss();
                    if (listener != null)
                        listener.onNegativeButtonClick();

                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }

    public static void showNaturalConfirmationDialog(Context context, final NaturalDialogButtonClickListener listener,
                                                     String strMsg, String strPositive, String strNegative, String strNeutral) {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                context);
        builder.setTitle(context.getResources().getString(R.string.app_name));
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage(strMsg);
        builder.setPositiveButton(
                strPositive,
                (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onPositiveButtonClick();
                });
        builder.setNegativeButton(
                strNegative,
                (dialog, which) -> {

                    dialog.dismiss();
                    if (listener != null)
                        listener.onNegativeButtonClick();

                });
        builder.setNeutralButton(
                strNeutral,
                (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onNaturalButtonClick();
                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }

    public static void showInformationDialog(Context context, final DialogButtonClickListener listener,
                                             String strTitle, String strMsg, String strPositive) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(strTitle);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setMessage(strMsg);
        builder.setPositiveButton(
                strPositive,
                (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null)
                        listener.onPositiveButtonClick();
                });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.show();
    }
}
