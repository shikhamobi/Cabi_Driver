/*
package com.cabi.driver.utils;


import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.cabi.driver.R;

*/
/**
 * Created by developer on 29/5/18.
 *//*


public class Utils {
    private static AlertDialog alert;
    private static AlertDialog gpsAlert;

    */
/**
     * To check Lollipop verison sdk
     *
     * @return true equal and higher , false for lower 5.0
     *//*

    public static boolean HigherThanLollipop() {

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    */
/**
     * Reveal animate the view
     *
     * @param viewRoot src view
     * @return @{@link Animator}
     *//*

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator animateRevealWithoutColorFromCoordinates(ViewGroup viewRoot) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        //  anim.setDuration(viewRoot.getResources().getInteger(R.integer.anim_duration_long_medium));
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;
    }

    */
/**
     * Reveal animate the view
     *
     * @param viewRoot src view
     * @param color    background color
     * @return @{@link Animator}
     *//*

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Animator animateRevealColorFromCoordinates(ViewGroup viewRoot, @ColorRes int color) {
        int cx = (viewRoot.getLeft() + viewRoot.getRight()) / 2;
        int cy = (viewRoot.getTop() + viewRoot.getBottom()) / 2;
        float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, cx, cy, 0, finalRadius);
        viewRoot.setBackgroundColor(ContextCompat.getColor(viewRoot.getContext(), color));
        //anim.setDuration(viewRoot.getResources().getInteger(R.integer.anim_duration_long_medium));
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.start();
        return anim;
    }


  */
/*  public static void alert_view(final Context mContext, String title, String message,
                                  String success_txt, String failure_txt,
                                  Boolean cancelable_val, final ClickInterface dialogInterface, final String s) {
      *//*
*/
/*  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(cancelable_val);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(success_txt,positive_listner);
        dialog.setNegativeButton(failure_txt,negative_listner);
        final AlertDialog alert = dialog.create();
        alert.show();*//*
*/
/*
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(cancelable_val);
        dialog.setMessage(message);
        dialog.setPositiveButton(success_txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogInterface.positiveButtonClick(dialog, id, s);
            }
        })
                .setNegativeButton(failure_txt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialogInterface.negativeButtonClick(dialog, id, s);
                    }
                });

        alert = dialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.button_accept));
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.black));

            }
        });
        alert.show();


    }*//*


    public static void closeDialog() {
        try {
            System.out.println("closeDialogCalling");
            if (alert != null && alert.isShowing()) {
                alert.dismiss();
                alert = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeGPSDialog() {
        try {
            System.out.println("closeDialogCalling");
            if (gpsAlert != null && gpsAlert.isShowing()) {
                gpsAlert.dismiss();
                gpsAlert = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */
/* AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
         dialog.setCancelable(false);
         dialog.setMessage("Are you sure you want to delete this entry?");
         dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int id) {
             //Action for "Delete".
         }
     })
             .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
             //Action for "Cancel".
         }
     });

     final AlertDialog alert = dialog.create();
         alert.show();

 *//*

    public static void alert_view_dialog(final Context mContext, String title, String message,
                                         String success_txt, String failure_txt,
                                         Boolean cancelable_val, final DialogInterface.OnClickListener postive_dialogInterface, final DialogInterface.OnClickListener negative_dialogInterface, final String s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(cancelable_val);
        dialog.setMessage(message);
        dialog.setPositiveButton(success_txt, postive_dialogInterface)
                .setNegativeButton(failure_txt, negative_dialogInterface);

        alert = dialog.create();
        alert.setOnShowListener(
                new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.button_accept));
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.black));

                    }
                });
        alert.show();


    }


    public static void alert_view_dialog_GPS(final Context mContext, String title, String message,
                                             String success_txt, String failure_txt,
                                             Boolean cancelable_val, final DialogInterface.OnClickListener postive_dialogInterface, final DialogInterface.OnClickListener negative_dialogInterface, final String s) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setCancelable(cancelable_val);
        dialog.setMessage(message);
        dialog.setPositiveButton(success_txt, postive_dialogInterface)
                .setNegativeButton(failure_txt, negative_dialogInterface);

        gpsAlert = dialog.create();
        gpsAlert.setOnShowListener(
                new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        gpsAlert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.button_accept));
                        gpsAlert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.black));

                    }
                });
        gpsAlert.show();


    }
}
*/
