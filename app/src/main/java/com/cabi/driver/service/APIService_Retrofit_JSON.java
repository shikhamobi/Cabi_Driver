package com.cabi.driver.service;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cabi.driver.R;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.NetworkStatus;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * @author developer This AsyncTask used to communicate the application with server through Volley framework. Here the response completely in JSON format. Constructor get the input details List<NameValuePair>,POST or GET then url. In pre execute,Show the progress dialog. In Background,Connect and get the response. In Post execute, Return the result with interface. This class call the API without any progress on UI.
 */
public class APIService_Retrofit_JSON {
    private boolean dont_encode;
    HashMap<String, String> map;
    final String check_internet = "Check your Internet Connection";
    final String please_try_again = "Please try again later!";
    public Dialog mProgressdialog;
    public Context mContext;
    private boolean isSuccess = true;
    private boolean GetMethod;
    private Dialog mDialog;
    private JSONObject data;
    public APIResult response;
    public boolean wholeURL;
    String result = "";
    private String url_type;

    public APIService_Retrofit_JSON(Context ctx, APIResult res, JSONObject j, boolean getmethod) {
        mContext = ctx;
        response = res;
        this.data = j;
        GetMethod = getmethod;
    }

    public APIService_Retrofit_JSON(Context ctx, APIResult res, String j, boolean getmethod) {
        mContext = ctx;
        response = res;
        JSONObject jobj = null;
        try {
            if (!getmethod)
                jobj = new JSONObject(j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.data = jobj;
        GetMethod = getmethod;
    }


    public APIService_Retrofit_JSON(Context ctx, APIResult res, JSONObject j, boolean getmethod, String url, boolean dont_encode) {
        mContext = ctx;
        response = res;
        this.data = j;
        this.dont_encode = dont_encode;
        GetMethod = getmethod;
        String type[] = url.split("type=");
        if (type.length > 1)
            url_type = type[1];
        else {
            wholeURL = true;
            url_type = url;
        }
    }


    public APIService_Retrofit_JSON(Context ctx, APIResult res, boolean getmethod) {
        mContext = ctx;
        response = res;
        GetMethod = getmethod;
    }

    //@Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
//		super.onPreExecute();
        showDialog();
        doInBackground();
    }

    public void showDialog() {
        try {
            if (NetworkStatus.isOnline(mContext)) {
                if (mDialog != null && mContext!=null)
                    mDialog.dismiss();
                View view = View.inflate(mContext, R.layout.progress_bar, null);
                mDialog = new Dialog(mContext, R.style.dialogwinddow);
                Colorchange.ChangeColor((ViewGroup) view, mContext);
                mDialog.setContentView(view);
                mDialog.setCancelable(false);
                try {
                    if(mContext!=null)
                    mDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
                Glide.with(mContext)
                        .load(R.raw.loading_anim)
                        .into(imageViewTarget);
            } else {

            }
        } catch (Exception e) {

        }

    }

    public void closeDialog() {
        try {
            if (mDialog != null)
                if (mDialog.isShowing())
                    mDialog.dismiss();
        } catch (Exception e) {

        }
    }

    //@Override
    protected void doInBackground() {
        // TODO Auto-generated method stub
        if (!NetworkStatus.isOnline(mContext)) {
            isSuccess = false;
            response.getResult(false, NC.getResources().getString(R.string.please_check_internet));
            result = mContext.getString(R.string.please_check_internet);
            //return result;
        } else {
            if (GetMethod) {
                CoreClient client = null;
                //System.out.println("rrc_____get" + url_type + "___" + wholeURL + "___" + data);

                client = new ServiceGenerator(mContext, dont_encode).createService(CoreClient.class);
                Call<ResponseBody> coreResponse = null;
                if (!wholeURL)
                    coreResponse = client.coreDetailsN(ServiceGenerator.COMPANY_KEY, ServiceGenerator.DYNAMIC_AUTH_KEY);
                else
                    coreResponse = client.getWhole("no-cache", url_type);
                coreResponse.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        String data = null;
                        closeDialog();
                        try {
                            data = response.body().string();
                            // APIService_Retrofit_JSON.this.response.getResult(true, data);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //System.out.println("rrc_____get" + data);
                        if (data != null) {
                            if (APIService_Retrofit_JSON.this.response != null)
                                APIService_Retrofit_JSON.this.response.getResult(true, data);

                            //System.out.println("rrc_____get" + data);
                        } else {
                            if (APIService_Retrofit_JSON.this.response != null)
                                APIService_Retrofit_JSON.this.response.getResult(false, null);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (APIService_Retrofit_JSON.this.response != null)
                            APIService_Retrofit_JSON.this.response.getResult(false, null);
                        else
                            Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        closeDialog();
                    }
                });

            } else {
                CoreClient client = new ServiceGenerator(mContext, dont_encode).createService(CoreClient.class);
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (data).toString());

                Call<ResponseBody> coreResponse = client.updateUser(ServiceGenerator.COMPANY_KEY, body, url_type, ServiceGenerator.DYNAMIC_AUTH_KEY, SessionSave.getSession("Lang", mContext));
                coreResponse.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                        String data = null;
                        closeDialog();
                        //System.out.println("ssssss____________" + response.body());
                        try {

                            if (response.body() != null) {
                                data = response.body().string();
                                if (APIService_Retrofit_JSON.this.response != null)
                                    APIService_Retrofit_JSON.this.response.getResult(true, data);
                            } else {
                                if (APIService_Retrofit_JSON.this.response != null)
                                    APIService_Retrofit_JSON.this.response.getResult(false, null);
                                Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_SHORT);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (data != null) {
                            //System.out.println("rrc_____post" + data);
                        } else {
                            if (APIService_Retrofit_JSON.this.response != null)
                                APIService_Retrofit_JSON.this.response.getResult(false, null);
                            else
                                Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (APIService_Retrofit_JSON.this.response != null)
                            APIService_Retrofit_JSON.this.response.getResult(false, null);
                        else
                            Toast.makeText(mContext, mContext.getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        closeDialog();
                    }
                });
            }

        }

    }


    public void execute(String url) {
        String[] type = url.split("=");
        this.url_type = type[1];
        onPreExecute();

    }

    public void execute() {

        onPreExecute();

    }
}