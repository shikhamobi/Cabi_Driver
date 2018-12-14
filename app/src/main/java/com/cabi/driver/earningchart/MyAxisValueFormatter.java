package com.cabi.driver.earningchart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by developer on 12/11/16.
 */
public class MyAxisValueFormatter implements IAxisValueFormatter, IValueFormatter {

    private DecimalFormat mFormat;
    private String currency;

    public MyAxisValueFormatter(String currency) {

        mFormat = new DecimalFormat("###,###,###,##0.00");
        this.currency=currency;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        NumberFormat numberFormat=DecimalFormat.getInstance(Locale.ENGLISH);
        DecimalFormat formatter = (DecimalFormat)numberFormat;
        formatter.applyPattern("###,###,###,##0.00");
        String fString = formatter.format(value);
        //System.out.println("fjdhsjshcc"+mFormat.format(numberFormat.format(value)));
        return " "+fString;
    }

    @Override
    public int getDecimalDigits() {
        return 1;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
      //  NumberFormat numberFormat=DecimalFormat.getInstance(Locale.ENGLISH);
       // System.out.println("fjdhsjsh"+mFormat.format(numberFormat.format(value)));

        NumberFormat numberFormat=DecimalFormat.getInstance(Locale.ENGLISH);
        DecimalFormat formatter = (DecimalFormat)numberFormat;
        formatter.applyPattern("###,###,###,##0.00");
        String fString = formatter.format(value);
        //System.out.println("fjdhsjshcc"+mFormat.format(numberFormat.format(value)));
        return " "+fString;
       // return " "+currency+mFormat.format(numberFormat.format(value));
    }
}
