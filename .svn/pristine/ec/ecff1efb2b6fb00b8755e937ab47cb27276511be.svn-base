package com.cabi.driver.earningchart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by developer on 15/11/16.
 */
public class Weekaxis implements IAxisValueFormatter {



        public static Object[] mMonths1 = new Object[]{
                "Sun", "Mon", "Tue",
        };

        public Weekaxis() {


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
            return String.valueOf(mMonths1[(int)value]);
        }

        @Override
        public int getDecimalDigits() {
            return 0;
        }


}
