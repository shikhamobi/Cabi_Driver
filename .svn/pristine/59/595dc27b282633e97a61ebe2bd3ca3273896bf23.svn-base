package com.cabi.driver.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabi.driver.R;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.SessionSave;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by developer on 14/11/16.
 */

/**
 *This adapter class is used to show withdraw referal history
 */
public class Withdraw_referalhistory_adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mList;
    private ArrayList<Integer> Bg=new ArrayList<>();

    // constructor
    public Withdraw_referalhistory_adapter(Context context, ArrayList<HashMap<String, String>> list) {
        this.mContext = context;
        this.mList = list;

    }
    // It returns the list item count.
    @Override
    public int getCount() {
        return mList.size();
    }


    // It returns the item detail with select position.
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    // It returns the item id with select position.
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // Get the view for each row in the list used view holder.
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.withdrawrefhistoryitem, parent,false);
            FontHelper.applyFont(mContext, convertView.findViewById(R.id.list_booking_root));
            holder = new ViewHolder();
            holder.request_amount = (TextView) convertView.findViewById(R.id.request_amount);
            holder.request_taxi = (TextView) convertView.findViewById(R.id.request_taxi);
            holder.status = (TextView) convertView.findViewById(R.id.status);

            holder.layout = (LinearLayout) convertView.findViewById(R.id.main);

            // holder.txtName.setText(""+mList.get(position).get("passenger_name"));
            convertView.setTag(holder);




        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.request_taxi.setText("" + mList.get(position).get("wallet_request_date"));
            holder.request_amount.setText("" + SessionSave.getSession("site_currency", mContext)+" "+ mList.get(position).get("wallet_request_amount"));
            holder.status.setText("" + mList.get(position).get("status"));
            holder.layout.setBackgroundColor(Bg.get(position));




        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return convertView;
    }


    /**
     *View holder class member this contains in every row in list.
     */
    private class ViewHolder {
        LinearLayout layout;
        TextView request_amount;
        TextView request_taxi;
        TextView status;

    }
}