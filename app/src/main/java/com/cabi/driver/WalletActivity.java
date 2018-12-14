package com.cabi.driver;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.service.ServiceGenerator;
import com.cabi.driver.utils.CL;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by developer on 15/11/17.
 */

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {
//}
//public class WalletFrag extends Fragment implements View.OnClickListener,FragPopFront {


    private LinearLayout SlideImg;

    private LinearLayout Donelay;
    private TextView BackBtn;
    private TextView leftIcon;
    private TextView HeadTitle;
    private TextView walletbalTxt;
    private Button addmoneyBut;
    private EditText addmoneyEdt;
    private TextView monoption1;
    private TextView monoption2;
    private TextView monoption3;
    private TextView procodeTxt;
    private String promoCode = "";
    private Dialog mDialog;
    private int addMoney = 0;
    private Double walletAmount;
    private String wallet_amount_range = "";
    private String wallet_amount1 = "";
    private String wallet_amount2;
    private String wallet_amount3;
    private int range1;
    private int range2;
    private Dialog alertmDialog;
    private Dialog mshowDialog;
    private LinearLayout loading;


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.walletlay, container, false);
//        findViewById(v);
//        return v;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walletlay);
    }

    public void findViewById(View v) {
        FontHelper.applyFont(WalletActivity.this, v.findViewById(R.id.rootContain));
        HeadTitle = (TextView) v.findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.wallet));
        leftIcon = (TextView) v.findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.GONE);
        BackBtn = (TextView) v.findViewById(R.id.back_text);
        BackBtn.setVisibility(View.VISIBLE);

        Glide.with(this).load(SessionSave.getSession("image_path",WalletActivity.this)+"walletPageIcon.png").into((ImageView) v.findViewById(R.id.walletPageIcon));
        Initialize(v);
    }

    public void Initialize(View v) {
        // TODO Auto-generated method stub

        Colorchange.ChangeColor((ViewGroup) v,WalletActivity.this);

        SlideImg = (LinearLayout) v.findViewById(R.id.leftIconTxt);

        Donelay = (LinearLayout) v.findViewById(R.id.rightlay);
        Donelay.setVisibility(View.INVISIBLE);
        ((TextView) v.findViewById(R.id.cur_sym)).setText(SessionSave.getSession("Currency", WalletActivity.this));

        walletbalTxt = (TextView) v.findViewById(R.id.walletbalTxt);
        loading = (LinearLayout) v.findViewById(R.id.loading);
        ImageView iv= (ImageView)v.findViewById(R.id.giff);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
        Glide.with(WalletActivity.this)
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        addmoneyBut = (Button) v.findViewById(R.id.addmoneyBut);
        addmoneyEdt = (EditText) v.findViewById(R.id.addmoneyEdt);
        monoption1 = (TextView) v.findViewById(R.id.monoption1);
        monoption2 = (TextView) v.findViewById(R.id.monoption2);
        monoption3 = (TextView) v.findViewById(R.id.monoption3);
        procodeTxt = (TextView) v.findViewById(R.id.procodeTxt);
//        addmoneyEdt.setHint("" + getResources().getString(R.string.amount_between) + " " + SessionSave.getSession("Currency", WalletActivity.this) + "100" + "-" + SessionSave.getSession("Currency", WalletActivity.this) + "2000");
//        monoption1.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + "100");
//        monoption2.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + "200");
//        monoption3.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + "500");

        setOnclickListener();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (addmoneyEdt!= null)
//            addmoneyEdt.setText("");

//        ((MainHomeFragmentActivity) WalletActivity.this).left_icon.setTag("backarrow");
//        ((MainHomeFragmentActivity) WalletActivity.this).left_icon.setImageResource(R.drawable.back);
        CheckWallet();
    }

//    @Override
//    public void onAttach(Context context) {
//
//        super.onAttach(context);
//    }

    @Override
    public void onStart() {

        super.onStart();
        //  Colorchange.ChangeColor((ViewGroup) getView(),WalletActivity.this);
    }

    private void setOnclickListener() {

        BackBtn.setOnClickListener(this);
        addmoneyBut.setOnClickListener(this);
        monoption1.setOnClickListener(this);
        monoption2.setOnClickListener(this);
        monoption3.setOnClickListener(this);
        procodeTxt.setOnClickListener(this);

        SpannableString content = new SpannableString(NC.getString(R.string.have_promocode).trim());
        content.setSpan(new UnderlineSpan(), 0, NC.getString(R.string.have_promocode).trim().length(), 0);
        procodeTxt.setText(Html.fromHtml("<p><u>"+(NC.getString(R.string.have_promocode).trim())+"<p><u>"));
        //procodeTxt.setPaintFlags(procodeTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * Calls api for wallet amount enqury
     */
    private void CheckWallet() {

        try {
            JSONObject j = new JSONObject();
            j.put("passenger_id", SessionSave.getSession("Id", WalletActivity.this));
            String url = "type=passenger_wallet";
            new WalletBal(url, j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void trigger_FragPopFront() {
        CheckWallet();
      //  ((MainHomeFragmentActivity) WalletActivity.this).cancel_b.setVisibility(View.GONE);
        if (addmoneyEdt!=null)
            addmoneyEdt.setText("");
        if(monoption1!=null){
            monoption1.setBackgroundResource(R.drawable.draw_edittext_bg);
            monoption2.setBackgroundResource(R.drawable.draw_edittext_bg);
            monoption3.setBackgroundResource(R.drawable.draw_edittext_bg);
            monoption1.setTextColor(CL.getColor(R.color.hintcolor));
            monoption2.setTextColor(CL.getColor(R.color.hintcolor));
            monoption3.setTextColor(CL.getColor(R.color.hintcolor));
        }
    }

    /**
     * This class used to check wallet balance
     * <p>
     * This class used to check wallet balance
     * <p>
     *
     * @author developer
     */
    private class WalletBal implements APIResult {
        private WalletBal(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(WalletActivity.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {

                    loading.setVisibility(View.GONE);
                    addmoneyBut.setVisibility(View.VISIBLE);
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        walletAmount = Double.parseDouble(json.getString("wallet_amount"));
                        wallet_amount_range = json.getJSONObject("amount_details").getString("wallet_amount_range");
                        wallet_amount1 = json.getJSONObject("amount_details").getString("wallet_amount1");
                        wallet_amount2 = json.getJSONObject("amount_details").getString("wallet_amount2");
                        wallet_amount3 = json.getJSONObject("amount_details").getString("wallet_amount3");
                        String[] rangeary = wallet_amount_range.split("-");

                        Log.e("range__", rangeary[0]);

                        if (rangeary.length > 1) {
                            range1 = Integer.parseInt(rangeary[0]);
                            range2 = Integer.parseInt(rangeary[1]);
                        }
                        addmoneyEdt.setHint("" + NC.getResources().getString(R.string.amount_between) + " " + SessionSave.getSession("Currency", WalletActivity.this) + range1 + "-" + SessionSave.getSession("Currency", WalletActivity.this) + range2);
                        monoption1.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + wallet_amount1);
                        monoption2.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + wallet_amount2);
                        monoption3.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + wallet_amount3);
                        walletbalTxt.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + String.format(Locale.UK, "%.2f", walletAmount));
                    } else {
                        walletbalTxt.setText("" + SessionSave.getSession("Currency", WalletActivity.this) + "0.00");
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                /*WalletActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        CToast.ShowToast(WalletActivity.this, result);
                    }
                });*/
                alert_view(WalletActivity.this, "Message", "" + result, "" +NC.getResources().getString(R.string.ok), "");
            }
        }
    }

    /**
     * This class used to check valid promo code
     * <p>
     * This class used to check valid promo code
     * <p>
     *
     * @author developer
     */
    private class CheckPromoCode implements APIResult {
        private CheckPromoCode(final String url, JSONObject data) {
            // new APIService_Volley_JSON(CardRegisterAct.this, this, data, false).execute(url);
            new APIService_Retrofit_JSON(WalletActivity.this, this, data, false, (ServiceGenerator.API_BASE_URL + ServiceGenerator.COMPANY_KEY+"/?" + "lang=" + SessionSave.getSession("Lang", WalletActivity.this) + "&" + url),false).execute();
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub
            if (isSuccess) {
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        alert_view(WalletActivity.this, "Message", "" + json.getString("message"), "" +NC.getResources().getString(R.string.ok), "");
                    } else {
                        //alert_view(WalletActivity.this, "Message", "" + json.getString("message"), "" + getResources().getString(R.string.ok), "");
                        WalletActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Toast.makeText(WalletActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                 //   ShowToast(WalletActivity.this, json.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        promoCode = "";
                    }
                } catch (final JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    promoCode = "";
                }
            } else {
                promoCode = "";
                WalletActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(WalletActivity.this,  getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                       // CToast.ShowToast(WalletActivity.this, getString(R.string.server_con_error));
                    }
                });
//                alert_view(WalletActivity.this, "Message", "" + result, "" + getResources().getString(R.string.ok), "");
            }
        }
    }

    public void alert_view(Context mContext, String title, String message, String success_txt, String failure_txt) {
        try {
            final View view = View.inflate(mContext, R.layout.alert_view, null);
            alertmDialog = new Dialog(mContext, R.style.dialogwinddow);
            alertmDialog.setContentView(view);
            alertmDialog.setCancelable(true);
            FontHelper.applyFont(mContext, alertmDialog.findViewById(R.id.alert_id));
            Colorchange.ChangeColor((ViewGroup) alertmDialog.findViewById(R.id.alert_id),WalletActivity.this);
            alertmDialog.show();
            final TextView title_text = (TextView) alertmDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) alertmDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) alertmDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) alertmDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.GONE);
            title_text.setText(title);
            message_text.setText(message);
            button_success.setText(success_txt);
            button_success.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
            button_failure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    alertmDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    /**
     * this method is used to show the promo code dialog and execute api
     */

    private void ShowPromoDilaog() {

        try {

            final View view = View.inflate(WalletActivity.this, R.layout.alert_view, null);
            mDialog = new Dialog(WalletActivity.this, R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(true);
            mDialog.show();
            Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.alert_id),WalletActivity.this);
            FontHelper.applyFont(WalletActivity.this, mDialog.findViewById(R.id.alert_id));
            final TextView titleTxt = (TextView) mDialog.findViewById(R.id.title_text);
            final TextView msgTxt = (TextView) mDialog.findViewById(R.id.message_text);
            msgTxt.setVisibility(View.GONE);
            final EditText promocodeEdt = (EditText) mDialog.findViewById(R.id.forgotmail);
            final Button OK = (Button) mDialog.findViewById(R.id.button_success);
            final Button Cancel = (Button) mDialog.findViewById(R.id.button_failure);
            Cancel.setVisibility(View.GONE);
            promocodeEdt.setVisibility(View.VISIBLE);
            OK.setText("" + NC.getResources().getString(R.string.ok));
            titleTxt.setText("" + NC.getResources().getString(R.string.reg_promocode));
            int maxLengthpromoCode = getResources().getInteger(R.integer.promoMaxLength);
            promocodeEdt.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            promocodeEdt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthpromoCode)});
            InputFilter[] editFilters =promocodeEdt.getFilters();
            InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
            System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
            newFilters[editFilters.length] =new InputFilter.AllCaps();
            promocodeEdt.setFilters(newFilters);
            promocodeEdt.setHint("" + NC.getResources().getString(R.string.reg_enterprcode));
            OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    try {
                        promoCode = promocodeEdt.getText().toString();
                        if (TextUtils.isEmpty(promoCode))
//                            message = "" + NC.getResources().getString(R.string.reg_enterprcode);
//                        else
//                            result = true;
                        if (!TextUtils.isEmpty(promoCode)) {
                            mDialog.dismiss();
                            JSONObject j = new JSONObject();
                            j.put("passenger_id", SessionSave.getSession("Id", WalletActivity.this));
                            j.put("promo_code", promoCode);
                            String url = "type=check_valid_promocode";
                            new CheckPromoCode(url, j);
                            WalletActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        }else{
                            Toast.makeText(WalletActivity.this, NC.getResources().getString(R.string.reg_enterprcode), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO Auto-generated method stub
                    mDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    //


    /**
     * This is method for check the mail is valid by the use of regex class.
     */
    public boolean validdmail(String string) {
        // TODO Auto-generated method stub
        boolean isValid = false;
        String expression = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(string);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Slider menu used to move from one activity to another activity.
     * @param v
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_text:
                // menu.toggle();
                break;
            case R.id.addmoneyBut:
                if (addmoneyEdt.getText().toString().trim().length() != 0) {
                    addMoney = Integer.parseInt(addmoneyEdt.getText().toString());
                  /*  System.out.println("range1" + range1);
                    System.out.println("range2" + range2);*/
                    if (addMoney < range1 || addMoney > range2)
                        alert_view(WalletActivity.this, "" + NC.getResources().getString(R.string.message), "" + getResources().getString(R.string.amount_between) + " " + SessionSave.getSession("Currency", WalletActivity.this) + range1 + "-" + SessionSave.getSession("Currency", WalletActivity.this) + range2, "" + getResources().getString(R.string.ok), "");
                    else {
                        //Intent monIntent = new Intent(WalletActivity.this, AddMoneyAct.class);
//                        AddMoneyFrag addMoneyFrag = new AddMoneyFrag();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("MONEY", "" + addMoney);
//                        bundle.putString("PROMOCODE", promoCode);
//                        addMoneyFrag.setArguments(bundle);
//                        WalletActivity.this.getSupportFragmentManager().beginTransaction().add(R.id.mainFrag, addMoneyFrag).addToBackStack(null).commit();
//                        WalletActivity.this.finish();
                    }
                } else {
                    alert_view(WalletActivity.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.enter_amount), "" + NC.getResources().getString(R.string.ok), "");
                }
                break;
            case R.id.monoption1:
                monoption1.setBackgroundResource(R.drawable.draw_select_bg);
                monoption2.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption3.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption1.setTextColor(CL.getColor(R.color.button_accept));
                monoption2.setTextColor(CL.getColor(R.color.hintcolor));
                monoption3.setTextColor(CL.getColor(R.color.hintcolor));
                addmoneyEdt.setText("" + wallet_amount1);
                break;
            case R.id.monoption2:
                monoption1.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption2.setBackgroundResource(R.drawable.draw_select_bg);
                monoption3.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption2.setTextColor(CL.getColor(R.color.button_accept));
                monoption1.setTextColor(CL.getColor(R.color.hintcolor));
                monoption3.setTextColor(CL.getColor(R.color.hintcolor));
                addmoneyEdt.setText("" + wallet_amount2);
                break;
            case R.id.monoption3:
                monoption1.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption2.setBackgroundResource(R.drawable.draw_edittext_bg);
                monoption3.setBackgroundResource(R.drawable.draw_select_bg);
                monoption3.setTextColor(CL.getColor(R.color.button_accept));
                monoption2.setTextColor(CL.getColor(R.color.hintcolor));
                monoption1.setTextColor(CL.getColor(R.color.hintcolor));
                addmoneyEdt.setText("" + wallet_amount3);
                break;
            case R.id.procodeTxt:
                ShowPromoDilaog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        //TaxiUtil.mActivitylist.remove(this);
        try {
            if (mshowDialog != null && mshowDialog.isShowing()) {
                mshowDialog.dismiss();
                mshowDialog = null;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}