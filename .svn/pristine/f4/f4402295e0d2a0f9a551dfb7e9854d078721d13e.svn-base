package com.cabi.driver.earningchart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;


public class Weekaxisformatter implements IAxisValueFormatter
{

    public static List<String> mMonths;

    public Weekaxisformatter() {


    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        // float percent = value / axis.mAxisRange;
        //return mMonths[(int) (mMonths.length * percent)];
        try{

         //   System.out.println("____________hi"+value+"_");

        }catch (Exception e){
            e.printStackTrace();
        }
      //  Log.e("size", "" + mMonths.size() + "==" + ((int) value));
        if(mMonths.size()==((int)value)) {
            return "11";
        }else{
            if(mMonths.size()>((int)value))
                return mMonths.get(((int) value));
            else
                return "0";
        }

        // return String.valueOf(mMonths[(int)value]);
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}

