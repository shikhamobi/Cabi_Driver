package com.cabi.driver.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabi.driver.R;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.utils.Colorchange;


/**
 * Created by developer on 4/6/16.
 */

/**
 *This adapter class is used to show search location in listview
 */
public class Taxi_searchList_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
  //  ArrayList<SearchListData> data;

    public  interface LocationListItemClick{
        public void onClick(int pos);
    }
    public Taxi_searchList_adapter(Context c) {
        this.mContext = c;
      //  this.data = TaxiUtil.SEARCH_LIST_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.taxi__search_list_item, parent, false);
        Colorchange.ChangeColor((ViewGroup) view,mContext);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CustomViewHolder vHolder = (CustomViewHolder) holder;
        vHolder.title.setText(CommonData.SEARCH_LIST_ITEM.get(position).getAdd1());
        vHolder.sub_title.setText(CommonData.SEARCH_LIST_ITEM.get(position).getAdd2());
    }

    @Override
    public int getItemCount() {
        return CommonData.SEARCH_LIST_ITEM.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView title, sub_title;
        LinearLayout taxi__search_listitem;

        public CustomViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.taxi__search_listitem_textview_title);
            sub_title = (TextView) view.findViewById(R.id.taxi__search_listitem_textview_subtitle);
            taxi__search_listitem= (LinearLayout) view.findViewById(R.id.taxi__search_listitem);
            taxi__search_listitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(mContext, data.get(getAdapterPosition()).getAdd1()+"____"+String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                    ((LocationListItemClick)mContext).onClick(getAdapterPosition());

                }
            });
        }
    }

}
