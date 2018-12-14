package com.cabi.driver.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

//*
// * Created by developer on 4/25/16.
// * display the toast at the center of the screen


public class CToast {
    public static void ShowToast(Context context, String s) {
        Toast toast = Toast.makeText(context, "" + s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
