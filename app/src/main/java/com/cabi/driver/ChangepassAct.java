package com.cabi.driver;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cabi.driver.data.CommonData;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is used to change the driver password
 */
public class ChangepassAct extends MainActivity implements OnClickListener
{
	private TextView CancelBtn;
	private TextView DoneBtn;
	private EditText editoldpwd;
	private EditText editnewpwd;
	private EditText editconfirmpwd;
	private Button passchange_btn;
	private String oldpwd;
	private String newpwd;
	private String confirmpwd;
	private String md5oldpwd;
	private String md5newpwd;
	private String md5confirmpwd;

	@Override
	public int setLayout()
	{
		return R.layout.changepass_lay;
	}

	// Initialize the views on layout and variable declarations
	@Override
	public void Initialize()
	{
		// TODO Auto-generated method stub
		CommonData.sContext=this;
		CommonData.mActivitylist.add(this);
		CancelBtn = (TextView) findViewById(R.id.cancelBtn);
		DoneBtn = (TextView) findViewById(R.id.doneBtn);
		editoldpwd = (EditText) findViewById(R.id.editoldpwd);
		editnewpwd = (EditText) findViewById(R.id.editnewpwd);
		editconfirmpwd = (EditText) findViewById(R.id.editconfirmpwd);
		passchange_btn = (Button) findViewById(R.id.pro_change_btn);
		DoneBtn.setVisibility(View.GONE);

		Colorchange.ChangeColor((ViewGroup) (((ViewGroup) ChangepassAct.this
				.findViewById(android.R.id.content)).getChildAt(0)), ChangepassAct.this);

		setonclickListener();
	}


	/**
	 * On click listener
	 */
	private void setonclickListener()
	{
		passchange_btn.setOnClickListener(this);
		CancelBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		try
		{
			if (v == passchange_btn)
			{
				oldpwd = editoldpwd.getText().toString().trim();
				newpwd = editnewpwd.getText().toString().trim();
				confirmpwd = editconfirmpwd.getText().toString().trim();
				if (oldpwd.trim().length() == 0)
				{
					ShowToast(ChangepassAct.this, "" + NC.getResources().getString(R.string.enter_the_old_password));
				}
				else if (newpwd.trim().length() == 0)
				{
					ShowToast(ChangepassAct.this, "" + NC.getResources().getString(R.string.enter_the_new_password));
				}
				else if (newpwd.trim().length() < 6)
				{
					ShowToast(ChangepassAct.this, "" + NC.getResources().getString(R.string.password_min_character));
				}
				else if (confirmpwd.trim().length() == 0)
				{
					ShowToast(ChangepassAct.this, "" + NC.getResources().getString(R.string.enter_the_confirmation_password));
				}
				else if (!confirmpwd.equals(newpwd))
				{
					ShowToast(ChangepassAct.this, "" + NC.getResources().getString(R.string.confirmation_password_mismatch_with_password));
				}
				else if (oldpwd.equals(confirmpwd))
				{
					ShowToast(ChangepassAct.this, "" + NC.getResources().getString(R.string.old_password_and_newpassword_are_same));
				}
				else
				{
					md5oldpwd = convertPassMd5(oldpwd);
					md5newpwd = convertPassMd5(newpwd);
					md5confirmpwd = convertPassMd5(confirmpwd);
					JSONObject j = new JSONObject();
					j.put("id", SessionSave.getSession("Id", ChangepassAct.this));
					j.put("old_password", md5oldpwd);
					j.put("new_password", md5newpwd);
					j.put("confirm_password", md5confirmpwd);
					j.put("org_new_password", newpwd);
					String url = "type=chg_password_driver";
					new ChangePassword(url,j);
				}
			}
			else if (v == CancelBtn)
			{
				finish();
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	/**
	 * ChangePassword API response parsing.
	 */
	private class ChangePassword implements APIResult
	{
		String msg = "";

		public ChangePassword(String url,JSONObject data)
		{
			// TODO Auto-generated constructor stub
			new APIService_Retrofit_JSON(ChangepassAct.this, this, data, false).execute(url);
		}

		@Override
		public void getResult(boolean isSuccess, final String result)
		{
			// TODO Auto-generated method stub

			if (isSuccess)
			{
				try
				{
					JSONObject json = new JSONObject(result);
					if (json.getInt("status") == 1)
					{
						msg = json.getString("result");
						SessionSave.saveSession("Org_Password", newpwd, ChangepassAct.this);
						ShowToast(ChangepassAct.this, msg);
						startActivity(new Intent(ChangepassAct.this, MeAct.class));
						finish();
					}
					else
					{
						msg = json.getString("result");
						ShowToast(ChangepassAct.this, msg);
					}
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				runOnUiThread(new Runnable() {
					public void run()
					{
						ShowToast(ChangepassAct.this, getString(R.string.server_error));
					}
				});

			}
		}
	}
}
