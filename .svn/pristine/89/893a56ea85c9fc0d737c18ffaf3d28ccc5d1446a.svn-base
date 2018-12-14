package com.cabi.driver.utils;

import android.graphics.Color;

import com.cabi.driver.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 29/11/16.
 */

/**
 * Used to Store and get color files from Hash map
 */
public class CL {
    public static HashMap<Integer, String> nfields_byID = new HashMap<>();
    public static HashMap<String, String> nfields_byName = new HashMap<>();


    public static ArrayList<String> fields = new ArrayList<>();
    public static ArrayList<String> fields_value = new ArrayList<>();

    public static HashMap<String, Integer> fields_id = new HashMap<>();


    static CL CL = null;

    static CL getInstance() {
        if (CL == null)
            CL = new CL();
        else
            CL = CL;
        return CL;
    }

    public static CL getResources() {
        return getInstance();
    }

    public static CL getActivity() {
        return getInstance();
    }

    public static int getColor(int c) {
        String text = "";
        if (fields_value.contains(text)) {

        }
        if (nfields_byID.get(c) != null)
            return Color.parseColor(nfields_byID.get(c));
        else {
            System.out.println("nulllccc" + c + "____" + nfields_byID.size() + SessionSave.getSession("wholekeyColor", MainActivity.context));
            ColorRestore.getAndStoreColorValues(SessionSave.getSession("wholekeyColor", MainActivity.context), MainActivity.context);
//            if(MainActivity.context!=null && MainActivity.context.getResources().getColor(Integer.parseInt(nfields_byID.get(c)))!=-1)
//                return  MainActivity.context.getResources().getColor(Integer.parseInt(nfields_byID.get(c)));
//            else


//            if(MainActivity.context!=null && MainActivity.context.getResources().getColor(Integer.parseInt(nfields_byID.get(c)))!=-1)
//                return  MainActivity.context.getResources().getColor(Integer.parseInt(nfields_byID.get(c)));
//            else
            if (nfields_byID.get(c) == null)
                return MainActivity.context.getResources().getColor(c);
            else
                return Color.parseColor(nfields_byID.get(c));
        }
    }
}
