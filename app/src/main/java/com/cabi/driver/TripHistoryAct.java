package com.cabi.driver;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cabi.driver.fragments.TripHistory;
import com.cabi.driver.utils.Colorchange;

/**
 * Created by developer on 15/11/16.
 */


/**
 * This is class is used to show driver trip history
 */
public class TripHistoryAct extends AppCompatActivity {

    private TextView backtext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_history);

        backtext = (TextView) findViewById(R.id.slideImg);
        backtext.setVisibility(View.VISIBLE);

        backtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) TripHistoryAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), TripHistoryAct.this);

        //Pointing to trip history page
        getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, new TripHistory()).commit();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentById(R.id.mainFrag) instanceof TripHistory){
            Intent intent = new Intent(TripHistoryAct.this, MyStatus.class);
            startActivity(intent);
            finish();
        }else
        super.onBackPressed();

    }
}
