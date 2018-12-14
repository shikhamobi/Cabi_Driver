package com.cabi.driver.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cabi.driver.R;


/**
 * Created by developer on 29/11/16.
 */

/**
 *Used to change color and string file for all ui components
 */
public class Colorchange {

    public static void ChangeColor(ViewGroup parentLayout, Context cc) {
        for (int count = 0; count < parentLayout.getChildCount(); count++) {
            View view = parentLayout.getChildAt(count);
            int color =0;
            Drawable background = view.getBackground();

           // Log.d("background","background"+background);

            if (background instanceof ShapeDrawable) {
                // cast to 'ShapeDrawable'
                ShapeDrawable shapeDrawable = (ShapeDrawable) background;
                shapeDrawable.getPaint().setColor(CL.getResources().getColor(R.color.com_facebook_button_background_color));
                color = ((ColorDrawable) background).getColor();

            } else if (background instanceof GradientDrawable) {
                // cast to 'GradientDrawable'
                GradientDrawable gradientDrawable = (GradientDrawable) background;
            //    gradientDrawable.setStroke(1,CL.getResources().getColor(R.color.linebottom_dark));

            } else if(background instanceof LayerDrawable){

                try {
                    LayerDrawable layerDrawable = (LayerDrawable) background;
                    GradientDrawable selectedItem = (GradientDrawable) layerDrawable.getDrawable(0);

                    if(layerDrawable.getId(0)>0){
                        String name= cc.getResources().getResourceEntryName(layerDrawable.getId(0));
                        selectedItem.setStroke(2, CL.getResources().getColor(R.color.button_accept));
                    }else{
                        selectedItem.setStroke(1, CL.getResources().getColor(R.color.linebottom_dark));
                    }
                }catch (Exception e){

                }

            }
            else if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
                String hexColor = String.format("#%06X", (0xFFFFFF & color));
//

                switch(hexColor.toUpperCase()) {
                    case "#F6F6F6" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.appbg));
                        break;
                    case "#F5F5F5" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.header_bgcolor));
                        break;
                    case "#6F6F6F" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.appbgdark));
                        break;
                    case "#404041" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.header_text));
                        break;
                    case "#47BF28" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.shiftoncolor));
                        break;
                    case "#9C9C9C" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.shiftoffcolor));
                        break;
                    case "#FF0000" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.highlightcolor));
                        break;
                    case "#666666" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.textviewcolor_dark));
                        break;
                    case "#A2A2A2" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.textviewcolor_light));
                        break;
                    case "#646464" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.linebottom_dark));
                        break;
                    case "#979797" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.bordercolor));
                        break;
                    case "#A6000000" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.notificationbg));
                        break;
                    case "#EE3324" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.button_accept));
                        break;
                    case "#8E1F16" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.button_reject));
                        break;
                    case "#00000000" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.transparent));
                        break;
                    case "#88676767" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.semitransparent));
                        break;
                    case "#87DC1F" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.btnaccept));
                        break;
                    case "#5F5F5F" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.trip_history_text_grey));
                        break;
                    case "#BDBDBD" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.light_white));
                        break;
                    case "#3F51B5" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.colorPrimary));
                        break;
                    case "#303F9F" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.colorPrimaryDark));
                        break;
                    case "#FF4081" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.colorAccent));
                        break;
                    case "#87CEFA" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.pastbookingcard));
                        break;
                    case "#A3A3A3" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.invite_gray));
                        break;
                    case "#000000" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.transparent));
                        break;
                    case "#FFFFFF" :
                        view.setBackgroundColor(CL.getResources().getColor(R.color.white));
                        break;

                    // You can have any number of case statements.

                }






            }



//            switch(hexColor) {
//                case "#C2C2C2" :
//                    view.setBackgroundColor(CL.getResources().getColor(R.color.green));
//                    break;
//
//            }

//            if (view instanceof EditText) {
//            //    ((EditText) view).setHintTextColor(CL.getResources().getColor(R.color.green));
//            } else if (view instanceof ViewGroup) {
//
//                ChangeColor((ViewGroup) view,cc);
//            }


            if (view instanceof EditText || view instanceof TextView || view instanceof Button ) {
                try {
                    String tt="";
                    String text="";
                    //  String tt = getStringResourceByName(((EditText) view).getHint().toString(), cc);
                    //  Field[] fields = R.string.class.getDeclaredFields(); // or Field[] fields = R.string.class.getFields();
//String txt=
//                   for (int  i =0; i < fields.length; i++) {
//                       int resId = fields[i];
                    //str += fields[i].getName() + " = ";
                    //   if (resId != 0) {
                    //  str += CL.getResources().getString(resId);
                    if(view instanceof EditText) {
                        EditText tv=(EditText)(view);

                        if(tv.getText().toString().trim().equals("")){
                            text=tv.getHint().toString();
                            if(NC.fields_value.indexOf(text)!=-1){
                                String keyValue=NC.fields.get(NC.fields_value.indexOf(text));
                                ((EditText)view).setHint(NC.nfields_byName.get(keyValue));


                            }else{
                                System.out.println("null");
                            }}
                            String hexColor = String.format("#%06X", (0xFFFFFF & tv.getCurrentHintTextColor()));


                            switch(hexColor.toUpperCase()) {
                                case "#F6F6F6" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.appbg));
                                    break;
                                case "#F5F5F5" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.header_bgcolor));
                                    break;
                                case "#6F6F6F" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.appbgdark));
                                    break;
                                case "#404041" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.header_text));
                                    break;
                                case "#47BF28" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.shiftoncolor));
                                    break;
                                case "#9C9C9C" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.shiftoffcolor));
                                    break;
                                case "#FF0000" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.highlightcolor));
                                    break;
                                case "#666666" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.textviewcolor_dark));
                                    break;
                                case "#A2A2A2" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.textviewcolor_light));
                                    break;
                                case "#646464" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.linebottom_dark));
                                    break;
                                case "#979797" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.bordercolor));
                                    break;
                                case "#A6000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.notificationbg));
                                    break;
                                case "#EE3324" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.button_reject));
                                    break;
                                case "#00000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.transparent));
                                    break;
                                case "#88676767" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.semitransparent));
                                    break;
                                case "#87DC1F" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.btnaccept));
                                    break;
                                case "#5F5F5F" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.trip_history_text_grey));
                                    break;
                                case "#BDBDBD" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.light_white));
                                    break;
                                case "#3F51B5" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.colorPrimary));
                                    break;
                                case "#303F9F" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.colorPrimaryDark));
                                    break;
                                case "#FF4081" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.colorAccent));
                                    break;
                                case "#87CEFA" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.pastbookingcard));
                                    break;
                                case "#A3A3A3" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.invite_gray));
                                    break;
                                case "#000000" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.black));
                                    break;
                                case "#FFFFFF" :
                                    tv.setHintTextColor(CL.getResources().getColor(R.color.white));
                                    break;

                                // You can have any number of case statements.

                            }












                            //  et.setHint(NC.nfields_byName.get(text));
                        //}
                        //  et.setT
                       // else{
                            text=tv.getText().toString();
                            if(NC.fields_value.indexOf(text)!=-1){
                                String keyValue=NC.fields.get(NC.fields_value.indexOf(text));
                                ((EditText)view).setText(NC.nfields_byName.get(keyValue));}

                            String hexColort = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));


                            switch(hexColort.toUpperCase()) {
                                case "#F6F6F6" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.appbg));
                                    break;
                                case "#F5F5F5" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.header_bgcolor));
                                    break;
                                case "#6F6F6F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.appbgdark));
                                    break;
                                case "#404041" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.header_text));
                                    break;
                                case "#47BF28" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.shiftoncolor));
                                    break;
                                case "#9C9C9C" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.shiftoffcolor));
                                    break;
                                case "#FF0000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.highlightcolor));
                                    break;
                                case "#666666" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.textviewcolor_dark));
                                    break;
                                case "#A2A2A2" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.textviewcolor_light));
                                    break;
                                case "#646464" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.linebottom_dark));
                                    break;
                                case "#979797" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.bordercolor));
                                    break;
                                case "#A6000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.notificationbg));
                                    break;
                                case "#EE3324" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.button_reject));
                                    break;
                                case "#00000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.transparent));
                                    break;
                                case "#88676767" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.semitransparent));
                                    break;
                                case "#87DC1F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.btnaccept));
                                    break;
                                case "#5F5F5F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.trip_history_text_grey));
                                    break;
                                case "#BDBDBD" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.light_white));
                                    break;
                                case "#3F51B5" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorPrimary));
                                    break;
                                case "#303F9F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorPrimaryDark));
                                    break;
                                case "#FF4081" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorAccent));
                                    break;
                                case "#87CEFA" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.pastbookingcard));
                                    break;
                                case "#A3A3A3" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.invite_gray));
                                    break;
                                case "#000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.black));
                                    break;
                                case "#FFFFFF" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.white));
                                    break;

                                // You can have any number of case statements.

                            //}

                        }
//                               if (((EditText) view).getHint().toString().equals(CL.getResources().getString(resId))) {
//                                   tt = fields[i].getName();
//                                   break;
//                               }
                        // et.setText(NC.nfields_byName.get(text));
                    }else if(view instanceof TextView){

                            TextView tv=(TextView)(view);
                            text=(tv).getText().toString();
                            if(NC.fields_value.indexOf(text)!=-1){
                                String keyValue=NC.fields.get(NC.fields_value.indexOf(text));
                                tv.setText(NC.nfields_byName.get(keyValue));


                            }else {
                               // System.out.println("value__"+NC.fields_value.size()+"___"+text);
                            }
                            String hexColor = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));
                          //  System.out.println("color"+hexColor);

                            switch(hexColor.toUpperCase()) {
                                case "#F6F6F6" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.appbg));
                                    break;
                                case "#F5F5F5" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.header_bgcolor));
                                    break;
                                case "#6F6F6F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.appbgdark));
                                    break;
                                case "#404041" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.header_text));
                                    break;
                                case "#47BF28" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.shiftoncolor));
                                    break;
                                case "#9C9C9C" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.shiftoffcolor));
                                    break;
                                case "#FF0000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.highlightcolor));
                                    break;
                                case "#666666" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.textviewcolor_dark));
                                    break;
                                case "#A2A2A2" :
                                  //  System.out.println("color_"+CL.getResources().getColor(R.color.textviewcolor_light));
                                    tv.setTextColor(CL.getResources().getColor(R.color.textviewcolor_light));
                                    break;
                                case "#646464" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.linebottom_dark));
                                    break;
                                case "#979797" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.bordercolor));
                                    break;
                                case "#A6000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.notificationbg));
                                    break;
                                case "#EE3324" :
                               //     System.out.println("nsnfsnnfsnf__"+CL.getResources().getColor(R.color.button_accept)+"____"+cc.getResources().getColor(R.color.button_accept));

                                    tv.setTextColor(CL.getResources().getColor(R.color.button_accept));

                                    break;
                                case "#8E1F16" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.button_reject));
                                    break;
                                case "#00000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.transparent));
                                    break;
                                case "#88676767" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.semitransparent));
                                    break;
                                case "#87DC1F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.btnaccept));
                                    break;
                                case "#5F5F5F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.trip_history_text_grey));
                                    break;
                                case "#BDBDBD" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.light_white));
                                    break;
                                case "#3F51B5" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorPrimary));
                                    break;
                                case "#303F9F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorPrimaryDark));
                                    break;
                                case "#FF4081" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorAccent));
                                    break;
                                case "#87CEFA" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.pastbookingcard));
                                    break;
                                case "#A3A3A3" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.invite_gray));
                                    break;
                                case "#000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.black));
                                    break;
                                case "#FFFFFF" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.white));
                                    break;

                                // You can have any number of case statements.

                            }

                        String hexColort = String.format("#%06X", (0xFFFFFF & tv.getCurrentHintTextColor()));
                        //  System.out.println("color"+hexColor);

                        switch(hexColort.toUpperCase()) {
                            case "#F6F6F6" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.appbg));
                                break;
                            case "#F5F5F5" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.header_bgcolor));
                                break;
                            case "#6F6F6F" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.appbgdark));
                                break;
                            case "#404041" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.header_text));
                                break;
                            case "#47BF28" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.shiftoncolor));
                                break;
                            case "#9C9C9C" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.shiftoffcolor));
                                break;
                            case "#FF0000" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.highlightcolor));
                                break;
                            case "#666666" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.textviewcolor_dark));
                                break;
                            case "#A2A2A2" :
                                //  System.out.println("color_"+CL.getResources().getColor(R.color.textviewcolor_light));
                                tv.setHintTextColor(CL.getResources().getColor(R.color.textviewcolor_light));
                                break;
                            case "#646464" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.linebottom_dark));
                                break;
                            case "#979797" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.bordercolor));
                                break;
                            case "#A6000000" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.notificationbg));
                                break;
                            case "#EE3324" :
                                //     System.out.println("nsnfsnnfsnf__"+CL.getResources().getColor(R.color.button_accept)+"____"+cc.getResources().getColor(R.color.button_accept));

                                tv.setHintTextColor(CL.getResources().getColor(R.color.button_accept));

                                break;
                            case "#8E1F16" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.button_reject));
                                break;
                            case "#00000000" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.transparent));
                                break;
                            case "#88676767" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.semitransparent));
                                break;
                            case "#87DC1F" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.btnaccept));
                                break;
                            case "#5F5F5F" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.trip_history_text_grey));
                                break;
                            case "#BDBDBD" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.light_white));
                                break;
                            case "#3F51B5" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.colorPrimary));
                                break;
                            case "#303F9F" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.colorPrimaryDark));
                                break;
                            case "#FF4081" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.colorAccent));
                                break;
                            case "#87CEFA" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.pastbookingcard));
                                break;
                            case "#A3A3A3" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.invite_gray));
                                break;
                            case "#000000" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.black));
                                break;
                            case "#FFFFFF" :
                                tv.setHintTextColor(CL.getResources().getColor(R.color.white));
                                break;

                            // You can have any number of case statements.

                        }

                        }
                        else{
                            Button tv=(Button)(view);
                            text=(tv).getText().toString();
                            if(NC.fields_value.indexOf(text)!=-1){
                                String keyValue=NC.fields.get(NC.fields_value.indexOf(text));
                                tv.setText(NC.nfields_byName.get(keyValue));}

                            String hexColor = String.format("#%06X", (0xFFFFFF & tv.getCurrentTextColor()));


                            switch(hexColor.toUpperCase()) {
                                case "#F6F6F6" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.appbg));
                                    break;
                                case "#F5F5F5" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.header_bgcolor));
                                    break;
                                case "#6F6F6F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.appbgdark));
                                    break;
                                case "#404041" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.header_text));
                                    break;
                                case "#47BF28" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.shiftoncolor));
                                    break;
                                case "#9C9C9C" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.shiftoffcolor));
                                    break;
                                case "#FF0000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.highlightcolor));
                                    break;
                                case "#666666" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.textviewcolor_dark));
                                    break;
                                case "#A2A2A2" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.textviewcolor_light));
                                    break;
                                case "#646464" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.linebottom_dark));
                                    break;
                                case "#979797" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.bordercolor));
                                    break;
                                case "#A6000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.notificationbg));
                                    break;
                                case "#EE3324" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.button_accept));
                                    break;
                                case "#8E1F16" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.button_reject));
                                    break;
                                case "#00000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.transparent));
                                    break;
                                case "#88676767" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.semitransparent));
                                    break;
                                case "#87DC1F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.btnaccept));
                                    break;
                                case "#5F5F5F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.trip_history_text_grey));
                                    break;
                                case "#BDBDBD" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.light_white));
                                    break;
                                case "#3F51B5" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorPrimary));
                                    break;
                                case "#303F9F" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorPrimaryDark));
                                    break;
                                case "#FF4081" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.colorAccent));
                                    break;
                                case "#87CEFA" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.pastbookingcard));
                                    break;
                                case "#A3A3A3" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.invite_gray));
                                    break;
                                case "#000000" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.black));
                                    break;
                                case "#FFFFFF" :
                                    tv.setTextColor(CL.getResources().getColor(R.color.white));
                                    break;

                                // You can have any number of case statements.

                            }

                            //tv.setText(NC.nfields_byName.get(text));
                        }
//                               if(((TextView) view).getText().toString().equals(CL.getResources().getString(resId))){
//                                   tt = fields[i].getName();
//                                   break;
//                               }


                    if(NC.fields_value.contains(text)){
                        //  System.out.println("nagaaaaaaa___"+NC.fields_value.indexOf(text));
                      //  System.out.println("value_"+NC.fields.get(NC.fields_value.indexOf(text)));

                    }


                    // }
                    //  str += "\n";
                    //  }

                    //  Log.d("tt", "tt" + tt+"=="+fields.length);
                }catch (Exception e){
                    e.printStackTrace();
                }

            } else if (view instanceof ViewGroup) {

                ChangeColor((ViewGroup) view,cc);
            }





        }
    }


    private static String getStringResourceByName(String aString,Context cc) {
       // Log.d("tt","tt"+aString);
//        String packageName = cc.getPackageName();
//        int resId = CL.getResources().getIdentifier(aString, "string", packageName);
        String resId = cc.getResources().getString(cc.getResources().getIdentifier(aString, "string", cc.getPackageName()));

        return resId;
    }






}