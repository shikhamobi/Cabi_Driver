package com.cabi.driver.data;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cabi.driver.MeAct;
import com.cabi.driver.MyStatus;
import com.cabi.driver.OngoingAct;
import com.cabi.driver.R;
import com.cabi.driver.slidemenu.SlidingMenu;
import com.cabi.driver.utils.CL;

//Class for menu slider setup and implementation.
public class MenuSlider
{
	public Context mContext;
	private SlidingMenu menu;
	private final String fontbold = "DroidSans-Bold.ttf";
	private final String fontnormal = "DroidSans.ttf";
	public Typeface tf;
	private LinearLayout homelayout, profilelayout, managesaleslayout, targetlayout, statisticslayout, assignedlayout, telllayout, upcominglayout, ongoinglayout, logoutlayout, helplayout, otherdriverlayout, mystatuslayout;
	private TextView homeTextView, profileTextView, managesalesTextView, targetTextView, statisticsTextView, assignedTextView, tellTextView, upcomingTextView, ongoingTextView, logoutTextView, helpTextView, mystatusTextview, otherdriverTextview, menu_txtacc, taxihead;
	private ImageView homeImageView, profileImageView, managesalesImageView, targetImageView, statisticsImageView, assignedImageView, tellImageView, upcomingImageView, ongoingImageView, logoutImageView, helpImageView, mystatusImageView, otherdriverImageView;

	public MenuSlider(Context _context, SlidingMenu _menu)
	{
		mContext = _context;
		menu = _menu;
		slidemenu(mContext);
	}

	public Typeface fontbold()
	{
		tf = Typeface.createFromAsset(mContext.getAssets(), fontbold);
		return tf;
	}

	public Typeface fontnormal()
	{
		tf = Typeface.createFromAsset(mContext.getAssets(), fontnormal);
		return tf;
	}

	public SlidingMenu slidemenu(Context _context)
	{
		// TODO Auto-generated method stub
		menu = new SlidingMenu(mContext);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setBehindOffset(50);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity((Activity) _context, SlidingMenu.SLIDING_CONTENT);
		menu.setMenu(R.layout.slide_lay1);
		homelayout = (LinearLayout) menu.findViewById(R.id.menu_home);
		profilelayout = (LinearLayout) menu.findViewById(R.id.menu_me);
		managesaleslayout = (LinearLayout) menu.findViewById(R.id.manage_sales);
		targetlayout = (LinearLayout) menu.findViewById(R.id.menu_target);
		helplayout = (LinearLayout) menu.findViewById(R.id.menu_help);
		statisticslayout = (LinearLayout) menu.findViewById(R.id.menu_statistics);
		assignedlayout = (LinearLayout) menu.findViewById(R.id.menu_assignedtaxi);
		telllayout = (LinearLayout) menu.findViewById(R.id.menu_tellFrd);
		upcominglayout = (LinearLayout) menu.findViewById(R.id.menu_upgoing);
		ongoinglayout = (LinearLayout) menu.findViewById(R.id.menu_ongoing);
		logoutlayout = (LinearLayout) menu.findViewById(R.id.menu_logout);
		logoutlayout = (LinearLayout) menu.findViewById(R.id.menu_logout);
		mystatuslayout = (LinearLayout) menu.findViewById(R.id.menu_mystatus);
		otherdriverlayout = (LinearLayout) menu.findViewById(R.id.menu_otherdiver);
		homeTextView = (TextView) menu.findViewById(R.id.menu_txthome);
		profileTextView = (TextView) menu.findViewById(R.id.menu_txtme);
		managesalesTextView = (TextView) menu.findViewById(R.id.menu_txtmanage_sales);
		targetTextView = (TextView) menu.findViewById(R.id.menu_txttarget);
		statisticsTextView = (TextView) menu.findViewById(R.id.menu_txtstatistics);
		assignedTextView = (TextView) menu.findViewById(R.id.menu_txtassign);
		tellTextView = (TextView) menu.findViewById(R.id.menu_txttellfrd);
		upcomingTextView = (TextView) menu.findViewById(R.id.menu_txtupcoming);
		logoutTextView = (TextView) menu.findViewById(R.id.menu_txtlogout);
		helpTextView = (TextView) menu.findViewById(R.id.menu_txthelp);
		ongoingTextView = (TextView) menu.findViewById(R.id.menu_txtongoing);
		mystatusTextview = (TextView) menu.findViewById(R.id.menu_txtstatus);
		otherdriverTextview = (TextView) menu.findViewById(R.id.menu_txtotherdriver);
		menu_txtacc = (TextView) menu.findViewById(R.id.menu_txtacc);
		taxihead = (TextView) menu.findViewById(R.id.taxihead);
		homeImageView = (ImageView) menu.findViewById(R.id.menu_imghome);
		profileImageView = (ImageView) menu.findViewById(R.id.menu_imgme);
		targetImageView = (ImageView) menu.findViewById(R.id.menu_imgtarget);
		statisticsImageView = (ImageView) menu.findViewById(R.id.menu_imgstatistics);
		assignedImageView = (ImageView) menu.findViewById(R.id.menu_imgassign);
		tellImageView = (ImageView) menu.findViewById(R.id.menu_imgtellfrnd);
		upcomingImageView = (ImageView) menu.findViewById(R.id.menu_imgupcomin);
		logoutImageView = (ImageView) menu.findViewById(R.id.menu_imglogout);
		helpImageView = (ImageView) menu.findViewById(R.id.menu_imghelp);
		ongoingImageView = (ImageView) menu.findViewById(R.id.menu_imgongoing);
		managesalesImageView = (ImageView) menu.findViewById(R.id.menu_imgmanagesales);
		mystatusImageView = (ImageView) menu.findViewById(R.id.menu_imgstatus);
		otherdriverImageView = (ImageView) menu.findViewById(R.id.menu_imgotherdriver);
		homeTextView.setTypeface(fontnormal());
		profileTextView.setTypeface(fontnormal());
		managesalesTextView.setTypeface(fontnormal());
		targetTextView.setTypeface(fontnormal());
		statisticsTextView.setTypeface(fontnormal());
		assignedTextView.setTypeface(fontnormal());
		tellTextView.setTypeface(fontnormal());
		upcomingTextView.setTypeface(fontnormal());
		ongoingTextView.setTypeface(fontnormal());
		logoutTextView.setTypeface(fontnormal());
		helpTextView.setTypeface(fontnormal());
		mystatusTextview.setTypeface(fontnormal());
		otherdriverTextview.setTypeface(fontnormal());
		menu_txtacc.setTypeface(fontnormal());
		taxihead.setTypeface(fontnormal());
		if (mContext == MyStatus.mystatus)
		{
			mystatuslayout.setBackgroundResource(R.drawable.o_slide_navigationunfocus);
			mystatusTextview.setTextColor(CL.getResources().getColor(R.color.white));
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		if (mContext == MeAct.profileAct)
		{
			profilelayout.setBackgroundResource(R.drawable.o_slide_navigationunfocus);
			profileTextView.setTextColor(CL.getResources().getColor(R.color.white));
		}
		if (mContext == OngoingAct.activity)
		{
			menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}
		return menu;
	}
}