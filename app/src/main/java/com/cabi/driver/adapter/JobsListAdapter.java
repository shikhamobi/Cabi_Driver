package com.cabi.driver.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabi.driver.R;
import com.cabi.driver.utils.FontHelper;

import java.util.ArrayList;
import java.util.HashMap;



/**
 *This adapter used to show the job list details into list view.
 */
public class JobsListAdapter extends BaseAdapter
{
	private Context mContext;
	private ArrayList<HashMap<String, String>> mList;

	// constructor
	public JobsListAdapter(Context context, ArrayList<HashMap<String, String>> list)
	{
		this.mContext = context;
		this.mList = list;
	}

	// It returns the list item count.
	@Override
	public int getCount()
	{
		return mList.size();
	}


	// It returns the item detail with select position.
	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}

	// It returns the item id with select position.
	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	// Get the view for each row in the list used view holder.
	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		if (convertView == null)
		{
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.list_booking_item, null);
			FontHelper.applyFont(mContext, convertView.findViewById(R.id.list_booking_root));
			holder = new ViewHolder();
			holder.txtTime = (TextView) convertView.findViewById(R.id.booking_timeTxt);
			holder.txtPick = (TextView) convertView.findViewById(R.id.booking_picTxt);
			holder.txtDrop = (TextView) convertView.findViewById(R.id.booking_dropsTxt);
			holder.txtName = (TextView) convertView.findViewById(R.id.booking_nameTxt);
			holder.txtLblTime = (TextView) convertView.findViewById(R.id.txttime);
			holder.txtLblPick = (TextView) convertView.findViewById(R.id.txtpick);
			holder.txtLblDrop = (TextView) convertView.findViewById(R.id.txtdrop);
			holder.txtColon1 = (TextView) convertView.findViewById(R.id.txtcolon);
			holder.txtColon2 = (TextView) convertView.findViewById(R.id.txtcolon1);
			holder.txtColon3 = (TextView) convertView.findViewById(R.id.txtcolon2);
			holder.layout = (LinearLayout) convertView.findViewById(R.id.tripchild);
			// holder.txtName.setText(""+mList.get(position).get("passenger_name"));
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		try
		{
			holder.txtName.setText(Character.toUpperCase(mList.get(position).get("passenger_name").charAt(0)) + mList.get(position).get("passenger_name").substring(1));
			holder.txtTime.setText((mList.get(position).get("pickup_time")));
			//holder.txtTime.setText(MainActivity.date_conversion(mList.get(position).get("pickup_time")));

			holder.txtPick.setText("" + mList.get(position).get("pickup_location"));
			holder.txtDrop.setText("" + mList.get(position).get("drop_location"));

			holder.txtPick.measure(0, 0);
			int width = holder.txtPick.getHeight();

			ViewGroup.LayoutParams params = holder.txtLblPick.getLayoutParams();
			params.height = width;
			holder.txtLblPick.setLayoutParams(params);

			//holder.txtLblPick.setHeight(width);

			holder.txtDrop.measure(0, 0);
			int txtDrop1 = holder.txtDrop.getMeasuredHeight();
			ViewGroup.LayoutParams params1 = holder.txtLblDrop.getLayoutParams();
			params1.height = txtDrop1;
			holder.txtLblDrop.setLayoutParams(params1);
			//Log.d("Jobslist", "getView: " + txtDrop1 + "==" + width);
		//	holder.txtLblDrop.setHeight(txtDrop1);

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		return convertView;
	}


	/**
	 *View holder class member this contains in every row in list.
	 */
	private class ViewHolder
	{
		LinearLayout layout;
		TextView txtTime;
		TextView txtPick;
		TextView txtDrop;
		TextView txtName;
		TextView txtLblTime;
		TextView txtLblPick;
		TextView txtLblDrop;
		TextView txtColon1;
		TextView txtColon2;
		TextView txtColon3;
	}
}
