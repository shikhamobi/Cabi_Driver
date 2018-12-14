package com.cabi.driver.earningchart;

import android.content.Context;
import android.widget.TextView;

import com.cabi.driver.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by developer on 12/11/16.
 */
public class XYMarkerView extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;
    String currencySymbol = "";

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter, String currencySymbol) {
        super(context, R.layout.custom_marker_view);

        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("###.00");
        this.currencySymbol = currencySymbol;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

//        tvContent.setText("x: " + xAxisValueFormatter.getFormattedValue(e.getX(), null) + ", y: " + format.format(e.getY()));
        NumberFormat numberFormat=DecimalFormat.getInstance(Locale.ENGLISH);
        DecimalFormat formatter = (DecimalFormat)numberFormat;
        formatter.applyPattern("###,###,###,##0.00");
        String fString = formatter.format((e.getY()));
//        System.out.println("__NP_____" + numberFormat.format(e.getY()));
        //String ss = format.format(numberFormat.format(e.getY()));
//        if (!ss.trim().isEmpty())
//             ss = numberFormat.format(format.format(e.getY()));
        tvContent.setText(currencySymbol + " " + fString);

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
