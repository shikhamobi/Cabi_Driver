package com.cabi.driver;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.cabi.driver.data.CommonData;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.utils.AppCacheImage;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.FontHelper;
import com.cabi.driver.utils.ImageDownloader;
import com.cabi.driver.utils.ImageUtils;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.SessionSave;
import com.yalantis.ucrop.UCrop;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This class is used to show driver profile info
 */
public class MeAct extends MainActivity implements OnClickListener, ImageDownloader {
    private static final int FROM_GALLERY = 108;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 113;
    private static final int ACTIVITY_VIEW_ATTACHMENT = 115;
    private static final int ACTION_IMAGE_CAPTURE = 113;
    // Class members declarations.
    private TextView DoneBtn, hidePwd, hideconPwd;
    private TextView HeadTitle, btn_back,voucher;
    private TextView forgotpswdTxt;
    private EditText emailEdt;
    private EditText mobileEdt;
    private EditText passwordEdt;
    private EditText confirmpswdEdt;
    //private EditText bankEdt;
    //private EditText bankaccnoEdt;
    private EditText firstTxt;
    private EditText lastTxt;
    private String phone;
    private String newpwd;
    private String confirmpwd;
    private String bankname;
    private String bankaccNo;
    private String taxi_model = "";
    private String taxi_no = "";
    private String taxi_map_from = "";
    private String taxi_map_to = "";
    private int driver_rating;
    public static Activity mFlagger;
    private ImageView profileImage;
    private String base64 = "", file_base64 = "";
    private TextView slider;
    public static MeAct profileAct;
    Dialog fileDialog;
    private TextView btnLogout, btnupload;
    public ImageView fileimg;
    int imageSelect = 0, imageSelected = 0;
    //private ImageLoader imageLoader;
    private Bitmap mBitmap;
    private Uri imageUri;
    private Bitmap downImage;
    private TextView btntaxidetail;
    public Dialog tDialog;
    private RelativeLayout me_layout;
    private ImageView driverRat;
    private TextView withdraw;
    private TextView inviteFriends;
    private TextView walletamount;
    private int walletamountr = 0;
    private Dialog mcancelDialog;
    private Dialog mDialog;
    private ScrollView profile_lay_s;

    private Dialog mlangDialog;
    private int types = 1;
    String timeStamp;
    private String encodedImage = "";
    private String destinationFileName = "profileImage";
    private TextView version_code;
    PackageManager manager;

    /**
     * Set the layout to activity.
     */
    @Override
    public int setLayout() {

        setLocale();
        return R.layout.me_lay;
    }


    /**
     * Initialize the views on layout
     */
    @Override
    public void Initialize() {

        try {
            CommonData.mActivitylist.add(this);
            CommonData.sContext = this;
            CommonData.current_act = "MeAct";

            Colorchange.ChangeColor((ViewGroup) (((ViewGroup) MeAct.this
                    .findViewById(android.R.id.content)).getChildAt(0)), MeAct.this);

            FontHelper.applyFont(this, findViewById(R.id.me_layout));
            profileAct = this;
            me_layout = (RelativeLayout) findViewById(R.id.me_layout);
            profile_lay_s = (ScrollView) findViewById(R.id.profile_lay_s);
            slider = (TextView) findViewById(R.id.slideImg);
            DoneBtn = (TextView) findViewById(R.id.donebtn);
            HeadTitle = (TextView) findViewById(R.id.headerTxt);
            firstTxt = (EditText) findViewById(R.id.firstTxt);
            firstTxt.setEnabled(false);
            lastTxt = (EditText) findViewById(R.id.lastTxt);
            lastTxt.setEnabled(false);
            emailEdt = (EditText) findViewById(R.id.emailEdt);
            emailEdt.setEnabled(false);
            mobileEdt = (EditText) findViewById(R.id.mobileEdt);
            mobileEdt.setEnabled(false);
            btnupload = (TextView) findViewById(R.id.btnupload);
            passwordEdt = (EditText) findViewById(R.id.passwordEdt);
            confirmpswdEdt = (EditText) findViewById(R.id.confirmpswdEdt);
            btn_back = (TextView) findViewById(R.id.slideImg);
            voucher=(TextView)findViewById(R.id.voucher);
            btn_back.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.language_setting)).setText(Html.fromHtml("<p><u>" + (NC.getString(R.string.select_language).trim()) + "<p><u>"));
            //bankEdt = (EditText) findViewById(R.id.bankEdt);
            // bankaccnoEdt = (EditText) findViewById(R.id.bankaccnoEdt);
            profileImage = (ImageView) findViewById(R.id.profileimage);
            btntaxidetail = (TextView) findViewById(R.id.btntaxidetail);
            version_code = (TextView) findViewById(R.id.version_code);
            btntaxidetail.setText(Html.fromHtml("<p><u>" + (NC.getString(R.string.taxi_detailsu).trim()) + "<p><u>"));
            driverRat = (ImageView) findViewById(R.id.driverRat);
            HeadTitle.setText(NC.getResources().getString(R.string.m_me));
            DoneBtn.setVisibility(View.VISIBLE);
            DoneBtn.setText(NC.getResources().getString(R.string.save));

            btnLogout = (TextView) findViewById(R.id.btnlogout);
            btnLogout.setOnClickListener(this);
            final View view = View.inflate(MeAct.this, R.layout.fileupload_popup, null);
            fileDialog = new Dialog(MeAct.this, R.style.dialogwinddow);
            fileDialog.setContentView(view);
            hidePwd = (TextView) findViewById(R.id.hidePwd);
            hideconPwd = (TextView) findViewById(R.id.hideconPwd);
            withdraw = (TextView) findViewById(R.id.withdraw);
            inviteFriends = (TextView) findViewById(R.id.invitefriends);
            walletamount = (TextView) findViewById(R.id.walletAmt);
            //Getting Driver Profile
            manager = getPackageManager();

             timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());

            JSONObject j = new JSONObject();
            try {
                int curVersion = manager.getPackageInfo(getPackageName(), 0).versionCode;
                version_code.setText("Version" + " " + ":" + " " + curVersion);
                j.put("userid", SessionSave.getSession("Id", MeAct.this));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String pro_url = "type=driver_profile";
            new GetProfileData(pro_url, j);
            //Amount withdraw onclick
            withdraw.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // startActivity(new Intent(MeAct.this, WithdrawHistoryAct.class));
                    Intent in = new Intent(MeAct.this, WithDrawMenuAct.class);
                    in.putExtra("walletamountr", walletamountr);
                    startActivity(in);
                    //  withDraw();

//                    if (walletamountr > 0)
//                        withDraw();
//                    else
//                        alert_view(MeAct.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.enough_wallet), "" + getResources().getString(R.string.ok), "");

                }
            });

            //Invite Friend onclick
            inviteFriends.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MeAct.this, InviteFriendAct.class));
                }
            });
            voucher.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    final View view = View.inflate(MeAct.this, R.layout.forgot_popup, null);
                    mDialog = new Dialog(MeAct.this, R.style.NewDialog);
                    mDialog.setContentView(view);
                    Colorchange.ChangeColor((ViewGroup) mDialog.findViewById(R.id.inner_content), MeAct.this);
                    FontHelper.applyFont(MeAct.this, mDialog.findViewById(R.id.inner_content));
                    mDialog.setCancelable(true);
                    mDialog.show();
                    final EditText vouchers = (EditText) mDialog.findViewById(R.id.forgotmail);
                     vouchers.setHint(getString(R.string.enter_voucher));
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(8);
                    vouchers.setFilters(filterArray);
                    final Button OK = (Button) mDialog.findViewById(R.id.okbtn);
                    final Button Cancel = (Button) mDialog.findViewById(R.id.cancelbtn);
                    OK.setOnClickListener(new OnClickListener() {
                        private String voucher;

                        @Override
                        public void onClick(final View v) {
                            try {
                                voucher = vouchers.getText().toString();
                                if (validations(ValidateAction.isVoucherNULL, MeAct.this, voucher)) {
                                    JSONObject j = new JSONObject();
                                    j.put("voucher", voucher);
                                    j.put("phone",SessionSave.getSession("phone_number",MeAct.this));
                                    j.put("driver_id", SessionSave.getSession("Id", MeAct.this));
                                    j.put("used_date",timeStamp);
                                    final String url = "type=voucher_recharge";
                                    new VoucherRecharge(url, j);
                                    vouchers.setText("");
                                    mDialog.dismiss();
                                }
                            } catch (Exception e) {
                                // TODO: handle exception
                                e.printStackTrace();
                            }
                        }
                    });
                    Cancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            mDialog.dismiss();
                        }
                    });
                }
            });
            //
            lastTxt.setOnEditorActionListener(new OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                        passwordEdt.requestFocus();
                    }
                    return true;
                }
            });

            FontHelper.applyFont(MeAct.this, fileDialog.findViewById(R.id.topid_fileup));
            fileDialog.setCancelable(true);


            setOnclicklistener();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

          /* newly add for password hide and show*/

        hidePwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    FontHelper.applyFont(MeAct.this, passwordEdt);
                    hidePwd.setText("" + NC.getResources().getString(R.string.hide));

                } else {
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hidePwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(MeAct.this, passwordEdt);

                }
            }
        });
        passwordEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    hidePwd.setVisibility(View.VISIBLE);


                } else {
                    hidePwd.setVisibility(View.GONE);
                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(MeAct.this, passwordEdt);
                    if (hidePwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        FontHelper.applyFont(MeAct.this, passwordEdt);
                        hidePwd.setText("" + NC.getResources().getString(R.string.show));
                    }
                }
            }
        });
//        passwordEdt.setOnFocusChangeListener(new OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                // TODO Auto-generated method stub
//                if (hasFocus)
//                    showpwdImg.setVisibility(View.VISIBLE);
//                else {
//                    showpwdImg.setVisibility(View.GONE);
//                    passwordEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                }
//            }
//        });

        hideconPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (hideconPwd.getText().toString().equals(NC.getResources().getString(R.string.show))) {
                    confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    hideconPwd.setText("" + NC.getResources().getString(R.string.hide));
                    FontHelper.applyFont(MeAct.this, confirmpswdEdt);
                } else {
                    confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    hideconPwd.setText("" + NC.getResources().getString(R.string.show));
                    FontHelper.applyFont(MeAct.this, confirmpswdEdt);

                }
            }
        });
        confirmpswdEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    hideconPwd.setVisibility(View.VISIBLE);


                } else {
                    hideconPwd.setVisibility(View.GONE);
                    confirmpswdEdt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    FontHelper.applyFont(MeAct.this, confirmpswdEdt);
                    if (hideconPwd.getText().toString().equals(NC.getResources().getString(R.string.hide))) {
                        hideconPwd.setText("" + NC.getResources().getString(R.string.show));
                    }
                }
            }
        });

        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(MeAct.this, MyStatus.class);
                startActivity(in);
                finish();


            }
        });
        if (SessionSave.getSession("referal", MeAct.this).equalsIgnoreCase("0")) {
            inviteFriends.setVisibility(View.GONE);
        } else {
            inviteFriends.setVisibility(View.VISIBLE);

        }
    }


    /**
     * Click method used to change the language and update the UI based on selected language.
     */
    public void language_settings(View v) {

        final View view = View.inflate(MeAct.this, R.layout.lang_list, null);
        mlangDialog = new Dialog(MeAct.this, R.style.dialogwinddow);
        mlangDialog.setContentView(view);
        FontHelper.applyFont(MeAct.this, mlangDialog.findViewById(R.id.id_lang));
        Colorchange.ChangeColor((ViewGroup) mlangDialog.findViewById(R.id.id_lang), MeAct.this);
        mlangDialog.setCancelable(true);
        mlangDialog.show();
        String totalLang[] = (SessionSave.getSession("lang_json", MeAct.this)).trim().split("____");

        final LinearLayout lay_fav_res1 = (LinearLayout) mlangDialog.findViewById(R.id.language_list);
        for (int i = 0; i < totalLang.length; i++) {
            // lay_fav_res1.
            TextView tv = new TextView(MeAct.this);
            tv.setText(SessionSave.getSession("LANG" + String.valueOf(i), MeAct.this).replaceAll(".xml", ""));
            tv.setTag(i);
            tv.setPadding(15, 15, 15, 15);
            if (SessionSave.getSession("Lang", MeAct.this).equals("ar") || SessionSave.getSession("Lang", MeAct.this).equals("fa"))
                tv.setGravity(Gravity.RIGHT);
            tv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = (int) v.getTag();
                    types = pos;
                    String url = SessionSave.getSession(SessionSave.getSession("LANG" + String.valueOf(pos), MeAct.this), MeAct.this);
                    System.out.println("current_url" + url);
                    SessionSave.saveSession("currentStringUrl", url, MeAct.this);
                    if (!SessionSave.getSession("Lang", MeAct.this).equalsIgnoreCase(SessionSave.getSession("LANGCode" + String.valueOf(pos), MeAct.this)))
                        new callString("strings.xml");

                }
            });
            lay_fav_res1.addView(tv);
        }


    }


    /**
     * Call string method used to call string file from back end
     */
    private class callString implements APIResult {
        public callString(final String url) {
            // TODO Auto-generated constructor stub
            // System.out.println("");
            String urls = SessionSave.getSession("currentStringUrl", MeAct.this);
            if (urls.equals(""))
                urls = SessionSave.getSession("Lang_English", MeAct.this);
            new APIService_Retrofit_JSON(MeAct.this, this, null, true, urls, true).execute();


            //new APIService_Retrofit_JSON(MeAct.this, this, null, true,"http://192.168.1.169:1009/public/uploads/android/language/passenger/" + url).execute();
        }

        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {

                NC.nfields_byID.clear();
                NC.nfields_byName.clear();
                NC.fields.clear();
                NC.fields_value.clear();
                NC.fields_id.clear();
                setLocale();
                // System.out.println("nagaaaa"+result);
                SessionSave.saveSession("wholekey", result, MeAct.this);
                getAndStoreStringValues(result);

                RefreshAct();

            }

        }
    }

    /**
     * Storing string files in local hash map
     */
    private synchronized void getAndStoreStringValues(String result) {
        try {

//            switch (types) {
//                case 1:
//                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_English", MeAct.this), MeAct.this);
//                    break;
//                case 2:
//                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Turkish", MeAct.this), MeAct.this);
//                    break;
//                case 3:
//                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Arabic", MeAct.this), MeAct.this);
//                    break;
//                case 4:
//                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Russian", MeAct.this), MeAct.this);
//                    break;
//                case 5:
//                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_German", MeAct.this), MeAct.this);
//                    break;
//                case 6:
//                    SessionSave.saveSession("currentStringUrl", SessionSave.getSession("Lang_Spanish", MeAct.this), MeAct.this);
//                    break;
//            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(result);
            InputStream is = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document doc = dBuilder.parse(is);
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("*");


            int chhh = 0;

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    chhh++;

                    Element element2 = (Element) node;
//                    if (element2.getAttribute("name").equals("pressBack"))
//                        System.out.println("size" + chhh + "___" + element2.getTextContent());

                    //  NC.nfields_value.add(element2.getTextContent());
                    NC.nfields_byName.put(element2.getAttribute("name"), element2.getTextContent());

//                System.out.println("nagaSsss___"+element2.getAttribute("name"));
//                System.out.println("nagaSsss___***"+element2.getTextContent()+"___"+element2.getTagName()+"___"+element2.getNodeValue()
//                +"___"+element2.getNodeName()+"___"+element2.getLocalName());
                }
            }
            getValueDetail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Getting string files in local hash map
     */
    synchronized void getValueDetail() {
        Field[] fieldss = R.string.class.getDeclaredFields();
        // fields =new int[fieldss.length];
        for (int i = 0; i < fieldss.length; i++) {
            int id = getResources().getIdentifier(fieldss[i].getName(), "string", MeAct.this.getPackageName());
            if (NC.nfields_byName.containsKey(fieldss[i].getName())) {
                NC.fields.add(fieldss[i].getName());
                NC.fields_value.add(NC.getResources().getString(id));
                NC.fields_id.put(fieldss[i].getName(), id);

            } else {
                System.out.println("Imissedthepunchrefree" + fieldss[i].getName());
            }
//        for(int h=0;h<NC.nfields_byName.size();h++){
//            System.out.println("NagarrrHIIIIIII&&&"+fields_id.get(NC.nfields_byName.get(h))+"____"+NC.nfields_byName.get(h));
//        NC.nfields_byID.put(fields_id.get(NC.nfields_byName.get(h)),NC.nfields_byName.get(h));}
        }

        for (Map.Entry<String, String> entry : NC.nfields_byName.entrySet()) {
            String h = entry.getKey();
            String value = entry.getValue();

            NC.nfields_byID.put(NC.fields_id.get(h), NC.nfields_byName.get(h));
            // do stuff
        }


    }

    /**
     * This method used to refresh UI after language selection
     */
    private void RefreshAct() {

      /*  Intent intent = getIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);*/
//        for(int i=0;i<6;i++){
//            System.out.println( "typesssss"+types+"__"+SessionSave.getSession("LANGTemp"+String.valueOf(i), MeAct.this));
//        }
        String temptype = SessionSave.getSession("LANGTemp" + String.valueOf(types), MeAct.this);
        SessionSave.saveSession("Lang", SessionSave.getSession("LANGCode" + types, MeAct.this), MeAct.this);

        if (temptype.equals("LTR")) {
            //SessionSave.saveSession("Lang", "en", MeAct.this);
            SessionSave.saveSession("Lang_Country", "en_US", MeAct.this);
        } else {
            // SessionSave.saveSession("Lang", "ar", MeAct.this);
            SessionSave.saveSession("Lang_Country", "ar_EG", MeAct.this);

        }
        System.out.println("LLLLTTTTT" + temptype);
        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", MeAct.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(SessionSave.getSession("Lang", MeAct.this), arry[1]);
        Locale.setDefault(new Locale(SessionSave.getSession("Lang", MeAct.this), arry[1]));
        MeAct.this.getBaseContext().getResources().updateConfiguration(config, MeAct.this.getResources().getDisplayMetrics());

        Intent intent = new Intent(MeAct.this, MyStatus.class);
        showLoading(MeAct.this);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * Adding click listener to required views.
     */
    private void setOnclicklistener() {
        slider.setOnClickListener(this);
        DoneBtn.setOnClickListener(this);
       // profileImage.setOnClickListener(this);
        btnupload.setOnClickListener(this);
        btntaxidetail.setOnClickListener(this);
        me_layout.setOnClickListener(this);
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.cabi.driver", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                //  Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

        // super.onBackPressed();

        Intent intent = new Intent(MeAct.this, MyStatus.class);
        startActivity(intent);
        finish();
    }


    /**
     * Actions to be performed Onclick.
     */
    @Override
    public void onClick(View v) {

        try {
            // If logout view clicked the following process runs.
            if (v == btnLogout) {
                logout(MeAct.this);
            }
            // If back view clicked the following process runs.
            if (v == slider) {
                if (profileImage != null) {
                    finish();
                }
            }
            // If profile image view clicked the following process run.
            else if (v == profileImage) {

                try {
                    if (ActivityCompat.checkSelfPermission(MeAct.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(MeAct.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        ActivityCompat.requestPermissions(MeAct.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_CAMERA);
                        return;
                    } else
                        getCamera();
                } catch (Exception e) {

                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
            // If done view to update the driver basic details.
            else if (v == DoneBtn) {
                phone = mobileEdt.getText().toString().trim();
                newpwd = passwordEdt.getText().toString().trim();
                confirmpwd = confirmpswdEdt.getText().toString().trim();
                bankname = "";//bankEdt.getText().toString().trim();
                bankaccNo = "";// bankaccnoEdt.getText().toString().trim();
                Drawable drawable = profileImage.getDrawable();
                Bitmap bitmap = ImageUtils.drawableToBitmap(drawable);
                if (phone.equalsIgnoreCase(SessionSave.getSession("phone_number", MeAct.this)) && confirmpwd.equalsIgnoreCase(SessionSave.getSession("Org_Password", MeAct.this)) && bankname.equalsIgnoreCase(SessionSave.getSession("Bankname", MeAct.this)) && bankaccNo.equalsIgnoreCase(SessionSave.getSession("Bankaccount_No", MeAct.this)) && firstTxt.getText().toString().equalsIgnoreCase(SessionSave.getSession("Name", MeAct.this)) && lastTxt.getText().toString().equalsIgnoreCase(SessionSave.getSession("Lastname", MeAct.this)) && emailEdt.getText().toString().equalsIgnoreCase(SessionSave.getSession("Email", MeAct.this)) && bitmap == downImage) {
                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.no_changes), "" + NC.getResources().getString(R.string.ok), "");
                } else if (newpwd.length() > 0) {
                    if (validations(ValidateAction.isValidPassword, MeAct.this, newpwd)) {
                        if (!newpwd.equals(confirmpwd)) {
                            alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.pwd_same), "" + NC.getResources().getString(R.string.ok), "");
                        } else {
                            if (validations(ValidateAction.isValueNULL, MeAct.this, phone)) {
                                Drawable d = profileImage.getDrawable();
                                Bitmap bit = ImageUtils.drawableToBitmap(d);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bit.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                String url = "type=edit_driver_profile";
                                new EditProfile(url);
                            }
                        }
                    }
                } else {
                    if (validations(ValidateAction.isValueNULL, MeAct.this, phone)) {
                        Drawable d = profileImage.getDrawable();
                        Bitmap bit = ImageUtils.drawableToBitmap(d);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bit.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] byteArray = byteArrayOutputStream.toByteArray();
                        base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        String url = "type=edit_driver_profile";
                        //  Log.i("profile picture", base64);
                        new EditProfile(url);
                    }
                }
            } else if (v == forgotpswdTxt) {
                Intent intent = new Intent(MeAct.this, ChangepassAct.class);
                startActivity(intent);
            }
            // If file upload view to update the driver document details.
            else if (v == btnupload) {
                final TextView btnchoose = (TextView) fileDialog.findViewById(R.id.btnchoose);
                final TextView btnsubmit = (TextView) fileDialog.findViewById(R.id.btnsubmit);
                fileimg = (ImageView) fileDialog.findViewById(R.id.fileimg);
                fileimg.setImageResource(R.drawable.no_file);
                fileDialog.show();
                btnchoose.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        imageSelect = 0;
                        getCamera();
                    }
                });
                btnsubmit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        try {
                            if (imageSelected == 1) {
                                Drawable d = fileimg.getDrawable();
                                Bitmap bit = ImageUtils.drawableToBitmap(d);
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bit.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                file_base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                String url = "type=driver_document_upload";
                                new FileUpload(url);
                            } else {
                                alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.please_choose), "" + NC.getResources().getString(R.string.ok), "");
                            }
                        } catch (NullPointerException e) {
                            // TODO: handle exception
                            e.printStackTrace();
                            alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.image_failed), "" + NC.getResources().getString(R.string.ok), "");
                        }
                    }
                });
                fileDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        fileimg.setImageResource(R.drawable.no_file);
                    }
                });
            } else if (v == btntaxidetail) {
                showtaxiDetails();
            } else if (v == me_layout) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(me_layout.getWindowToken(), 0);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//new Handler().postDelayed(new Runnable() {
//    @Override
//    public void run() {
//        DoneBtn.setBackgroundResource(R.drawable.draw_select_bgred);
//    }
//},1000);

    }

    /**
     * Getting image from camera Or Taking new image
     */
//    private void getCamera() {
//        new AlertDialog.Builder(MeAct.this).setMessage("" + NC.getResources().getString(R.string.choose_an_image)).setTitle("" + NC.getResources().getString(R.string.profile_image)).setCancelable(true).setNegativeButton("" + NC.getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialog, final int which) {
//                // TODO Auto-generated method stub
//                System.gc();
//                final Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_PICK);
//                startActivityForResult(intent, 108);
//                dialog.cancel();
//
//
//            }
//        }).setPositiveButton("" + NC.getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialog, final int which) {
//                // TODO Auto-generated method stub
//                dialog.cancel();
////
////                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////                    startActivityForResult(takePictureIntent, ACTION_IMAGE_CAPTURE);
////                }
//
////                MimeTypeMap mime = MimeTypeMap.getSingleton();
////                final File photo = new File("com.cabi.driver.fileProvider");
////                String ext = photo.getName().substring(photo.getName().lastIndexOf(".") + 1);
////                String type = mime.getMimeTypeFromExtension(ext);
////                try {
////                    Intent intent = new Intent();
////                    intent.setAction(Intent.ACTION_VIEW);
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
////                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
////                        Uri contentUri = FileProvider.getUriForFile(MeAct.this, "com.cabi.driver.fileProvider", photo);
////                        intent.setDataAndType(contentUri, type);
////                    } else {
////                        intent.setDataAndType(Uri.fromFile(photo), type);
////                    }
////                    startActivityForResult(intent, ACTIVITY_VIEW_ATTACHMENT);
////                } catch (ActivityNotFoundException anfe) {
////                    Toast.makeText(MeAct.this, "No activity found to open this attachment.", Toast.LENGTH_LONG).show();
////                }
//
//                dialog.cancel();
//                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                final File photo = new File(Environment.getExternalStorageDirectory() + "/TaxiMobility/img");
//                if (!photo.exists())
//                    photo.mkdirs();
//                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                final File mediaFile = new File(photo.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
//                imageUri = Uri.fromFile(mediaFile);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                startActivityForResult(intent, 1);
//
////                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                final File photo = new File(Environment.getExternalStorageDirectory() + "/TaxiMobility/img");
////                if (!photo.exists())
////                    photo.mkdirs();
////                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
////                final File mediaFile = new File(photo.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
////                imageUri = Uri.fromFile(mediaFile);
////                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////                startActivityForResult(intent, 1);
//            }
//        }).show();
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getCamera();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //  finish();
                }
                return;
            }
        }
    }

    /**
     * withdraw amount
     */
    public void withDraw() {
        try {
            final View view = View.inflate(MeAct.this, R.layout.netcon_lay, null);
            mcancelDialog = new Dialog(MeAct.this, R.style.dialogwinddow);
            mcancelDialog.setContentView(view);
            mcancelDialog.setCancelable(true);
            mcancelDialog.show();
            FontHelper.applyFont(MeAct.this, mcancelDialog.findViewById(R.id.alert_id));
            final TextView title_text = (TextView) mcancelDialog.findViewById(R.id.title_text);
            final TextView message_text = (TextView) mcancelDialog.findViewById(R.id.message_text);
            final Button button_success = (Button) mcancelDialog.findViewById(R.id.button_success);
            final Button button_failure = (Button) mcancelDialog.findViewById(R.id.button_failure);
            button_failure.setVisibility(View.VISIBLE);
            title_text.setText("" + NC.getResources().getString(R.string.message));
            message_text.setText("" + NC.getResources().getString(R.string.with_draw) + SessionSave.getSession("site_currency", MeAct.this) + " " + String.valueOf(walletamountr));
            button_success.setText("" + NC.getResources().getString(R.string.yes));
            button_failure.setText("" + NC.getResources().getString(R.string.no));
            button_success.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    try {
                        mcancelDialog.dismiss();
                        // TODO Auto-generated method stub
                        {
                            //http://192.168.1.116:1027/mobileapi116/index/dGF4aV9hbGw=/?type=driver_wallet_request
                            //{"driver_id":"1529","driver_wallet_amount":"150"}
                            // JSONObject j = new JSONObject();
                            //j.put("driver_id", SessionSave.getSession("Id", MeAct.this));
                            //j.put("driver_wallet_amount", walletamountr);

                            // final String withdrawUrl = "type=driver_wallet_request";
                            // new WithDraw(withdrawUrl, j);

                            Intent in = new Intent(MeAct.this, WithDrawMenuAct.class);
                            startActivity(in);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            });
            button_failure.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    // TODO
                    // Auto-generated
                    // method stub
                    mcancelDialog.dismiss();
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    /**
     * withdraw amount Api and response parsing
     */
    private class WithDraw implements APIResult {
        private String msg;

        public WithDraw(final String url, JSONObject data) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(MeAct.this, this, data, false).execute(url);
                } else {
                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        /**
         * Parse the response and update the UI.
         */
        @Override
        public void getResult(final boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    final JSONObject json = new JSONObject(result);
                    msg = json.getString("message");
                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, NC.getResources().getString(R.string.ok), "");

                }

                //alert_view(OngoingAct.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_net_connection), "" + getResources().getString(R.string.ok), "");
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Used to call the edit profile Api(post method) and parse the response
     */
    private class EditProfile implements APIResult {
        String msg = "";

        public EditProfile(String url) {

            try {
                JSONObject j = new JSONObject();
                j.put("driver_id", SessionSave.getSession("Id", MeAct.this));
                j.put("salutation", SessionSave.getSession("Salutation", MeAct.this));
                j.put("email", emailEdt.getText().toString());
                j.put("phone", phone);
                j.put("firstname", firstTxt.getText().toString());
                j.put("lastname", lastTxt.getText().toString());
                j.put("password", confirmpwd);
                j.put("bankname", bankname);
                j.put("bankaccount_no", bankaccNo);
                //    j.put("profile_picture",base64);
                j.put("profile_picture", encodedImage);
                if (isOnline()) {
                    new APIService_Retrofit_JSON(MeAct.this, this, j, false).execute(url);
                } else {
                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("Bankname", bankname, MeAct.this);
                        SessionSave.saveSession("Bankaccount_No", bankaccNo, MeAct.this);
                        SessionSave.saveSession("Org_Password", confirmpwd, MeAct.this);
                        SessionSave.saveSession("phone_number", phone, MeAct.this);
                        SessionSave.saveSession("Name", firstTxt.getText().toString(), MeAct.this);
                        SessionSave.saveSession("Lastname", lastTxt.getText().toString(), MeAct.this);
                        SessionSave.saveSession("Email", emailEdt.getText().toString(), MeAct.this);
                        System.out.println("___________" + json.getJSONObject("detail").getString("driver_wallet_amount"));
//                        "driver_wallet_amount": "150.00",
//                        "driver_wallet_pending_amount": "510.00",
//                        "trip_amount": "0",
//                        "trip_pending_amount": 0,
                        SessionSave.saveSession("driver_wallet_amount", json.getJSONObject("detail").getString("driver_wallet_amount"), MeAct.this);
                        SessionSave.saveSession("driver_wallet_pending_amount", json.getJSONObject("detail").getString("driver_wallet_pending_amount"), MeAct.this);
                        SessionSave.saveSession("trip_amount", json.getJSONObject("detail").getString("trip_amount"), MeAct.this);
                        SessionSave.saveSession("trip_pending_amount", json.getJSONObject("detail").getString("trip_pending_amount"), MeAct.this);
                        Drawable drawable = profileImage.getDrawable();
                        downImage = ImageUtils.drawableToBitmap(drawable);
                        msg = json.getString("message");
                        confirmpswdEdt.setText("");
                        passwordEdt.setText("");
                        alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    } else {
                        msg = json.getString("message");
                        alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
//                    alert_view(MeAct.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_net_connection), "" + getResources().getString(R.string.ok), "");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(MeAct.this, getString(R.string.server_error));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @API call(get method) to get the driver profile data and parsing the response
     */
    private class GetProfileData implements APIResult {
        public GetProfileData(String url, JSONObject data) {

            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(MeAct.this, this, data, false).execute(url);
                } else {
                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        // Log.e("_json_", json.toString());
                        JSONObject details = json.getJSONObject("detail");
                        firstTxt.setText(details.getString("name"));
                        lastTxt.setText(details.getString("lastname"));
                        emailEdt.setText(details.getString("email"));
                        mobileEdt.setText(details.getString("phone"));
                        //  bankaccnoEdt.setText(details.getString("bankaccount_no"));
                        SessionSave.saveSession("driver_wallet_amount", details.getString("driver_wallet_amount"), MeAct.this);
                        SessionSave.saveSession("driver_wallet_pending_amount", details.getString("driver_wallet_pending_amount"), MeAct.this);
                        SessionSave.saveSession("trip_amount", details.getString("trip_amount"), MeAct.this);
                        SessionSave.saveSession("trip_pending_amount", details.getString("trip_pending_amount"), MeAct.this);
                        SessionSave.saveSession("referal_amount", "300", MeAct.this);
                        SessionSave.saveSession("total_amount", details.getString("total_amount"), MeAct.this);


                        walletamountr = details.getInt("driver_wallet_amount");
                        //  walletamount.setText(SessionSave.getSession("site_currency", MeAct.this) + " " + details.getString("driver_wallet_amount"));
                        walletamount.setText(SessionSave.getSession("site_currency", MeAct.this) + " " + details.getString("driver_recharge_amount"));

                        String imgPath = details.getString("main_image_path").trim();
                        taxi_model = details.getString("taxi_model");
                        taxi_no = details.getString("taxi_no");
                        taxi_map_from = details.getString("taxi_map_from");
                        taxi_map_to = details.getString("taxi_map_to");
                        driver_rating = details.getInt("driver_rating");
//                        ratingBar.setRating(driver_rating);
                        System.out.println("driver_rating" + driver_rating);
                        driverRat.setImageResource(0);
                        if (driver_rating == 0)
                            driverRat.setImageResource(R.drawable.star6);
                        else if (driver_rating == 1)
                            driverRat.setImageResource(R.drawable.star1);
                        else if (driver_rating == 2)
                            driverRat.setImageResource(R.drawable.star2);
                        else if (driver_rating == 3)
                            driverRat.setImageResource(R.drawable.star3);
                        else if (driver_rating == 4)
                            driverRat.setImageResource(R.drawable.star4);
                        else if (driver_rating == 5)
                            driverRat.setImageResource(R.drawable.star5);
                        try {
                            if (imgPath != null && imgPath.length() > 0) {
                                // imageLoader.displayImage(imgPath, profileImage, roundedImageoptions);
                                if (!AppCacheImage.loadBitmap(imgPath, profileImage))
                                    new DownloadImageTask(profileImage).execute(imgPath);
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        SessionSave.saveSession("Bankname", details.getString("bankname"), MeAct.this);
                        SessionSave.saveSession("Bankaccount_No", details.getString("bankaccount_no"), MeAct.this);
                        SessionSave.saveSession("Org_Password", confirmpwd, MeAct.this);
                        SessionSave.saveSession("phone_number", details.getString("phone"), MeAct.this);
                        SessionSave.saveSession("Name", details.getString("name"), MeAct.this);
                        SessionSave.saveSession("Lastname", details.getString("lastname"), MeAct.this);
                        SessionSave.saveSession("Email", details.getString("email"), MeAct.this);
                    } else {
                        ShowToast(MeAct.this, json.getString("message"));
                    }
                    profile_lay_s.setVisibility(View.VISIBLE);
                    me_layout.setVisibility(View.VISIBLE);
                } else {
                    //alert_view(MeAct.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_net_connection), "" + getResources().getString(R.string.ok), "");
                    ShowToast(MeAct.this, getString(R.string.server_error));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onStop() {
        CommonData.closeDialog(fileDialog);
        CommonData.closeDialog(tDialog);
        CommonData.closeDialog(mcancelDialog);
        CommonData.closeDialog(mDialog);
        CommonData.closeDialog(mlangDialog);
        super.onStop();
    }


    /**
     * Download Image API response parsing.
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String result;

        public DownloadImageTask(final ImageView bmImage) {

            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(final String... urls) {

            final String urldisplay = urls[0];
            result = urldisplay;
            Bitmap mIcon11 = null;
            try {
                final InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            if (result != null)
                AppCacheImage.addBitmapToMemoryCache(this.result, result);
            try {
                bmImage.setImageBitmap(result);
                downImage = result;
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }


    private class ImageCompressionAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private Dialog mDialog;
        private String result;
        private int orientation;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            final View view = View.inflate(MeAct.this, R.layout.progress_bar, null);
            mDialog = new Dialog(MeAct.this, R.style.NewDialog);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();

            ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
            Glide.with(MeAct.this)
                    .load(R.raw.loading_anim)
                    .into(imageViewTarget);
        }

        @Override
        protected Bitmap doInBackground(final String... params) {
            try {
                result = getRealPathFromURI(params[0]);
                final File file = new File(result);
                System.out.println("Imageee....." + file.getTotalSpace() + "__" + file.getAbsolutePath());

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                mBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                //  mBitmap = decodeImageFile(file);
                System.out.println("Imageee.....b" + mBitmap.getByteCount() + "___" + mBitmap.getHeight());

//                if (mBitmap != null) {
//                    final Matrix matrix = new Matrix();
//                    try {
//                        final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
//                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                        if (orientation == 3) {
//                            matrix.postRotate(180);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        } else if (orientation == 6) {
//                            matrix.postRotate(90);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        } else if (orientation == 8) {
//                            matrix.postRotate(270);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        }
//                    } catch (final IOException e) {
//                    }
//                }
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                if (mBitmap.getByteCount() > 100000 && mBitmap.getByteCount() < 200000)
//                    mBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
//                else if (mBitmap.getByteCount() > 200000)
//                    mBitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
//                else
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                final byte[] image = stream.toByteArray();
                encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
                System.out.println("Imageee" + encodedImage.getBytes().length + "__" + mBitmap.getHeight());
                System.out.println("Imageeen" + encodedImage.length());
            } catch (final Exception e) {
                // TODO: handle exception
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MeAct.this, NC.getResources().getString(R.string.image_failed), Toast.LENGTH_SHORT).show();
                        //  ShowToast(getApplicationContext(), "" + NC.getResources().getString(R.string.please_select_image_from_valid_path));
                    }
                });
            }
            return mBitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (MeAct.this != null && mDialog.isShowing())
                    mDialog.dismiss();

                profileImage.setBackgroundResource(0);
                if (result != null)
                    profileImage.setImageBitmap(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Image Compression API response parsing.
     */
//    private class ImageCompressionAsyncTask extends AsyncTask<String, Void, Bitmap> {
//
//        private String result;
//        private int orientation;
//        private int requestcode;
//
//        public ImageCompressionAsyncTask(int reqcode) {
//            // TODO Auto-generated constructor stub
//            requestcode = reqcode;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//            final View view = View.inflate(MeAct.this, R.layout.progress_bar, null);
//            mDialog = new Dialog(MeAct.this, R.style.NewDialog);
//            mDialog.setContentView(view);
//            mDialog.setCancelable(false);
//            mDialog.show();
//
//            ImageView iv = (ImageView) mDialog.findViewById(R.id.giff);
//            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(iv);
//            Glide.with(MeAct.this)
//                    .load(R.raw.loading_anim)
//                    .into(imageViewTarget);
//        }
//
//        @Override
//        protected Bitmap doInBackground(final String... params) {
//
//            try {
//                result = getRealPathFromURI(params[0]);
//                //  result=params[0].getP
//                System.out.println("Image" + result);
//                final File file = new File(result);
//                mBitmap = decodeImageFile(file);
//                if (mBitmap != null) {
//                    final Matrix matrix = new Matrix();
//                    try {
//                        final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
//                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                        if (orientation == 3) {
//                            matrix.postRotate(180);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        } else if (orientation == 6) {
//                            matrix.postRotate(90);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        } else if (orientation == 8) {
//                            matrix.postRotate(270);
//                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
//                        }
//
//                        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                        if (mBitmap.getByteCount() > 100000 && mBitmap.getByteCount() < 200000)
//                            mBitmap.compress(Bitmap.CompressFormat.PNG, 80, stream);
//                        else if (mBitmap.getByteCount() > 200000)
//                            mBitmap.compress(Bitmap.CompressFormat.PNG, 60, stream);
//                        else
//                            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                    } catch (final IOException e) {
//                    }
//                } else {
//                    if (mDialog.isShowing())
//                        mDialog.dismiss();
//                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.image_failed), "" + NC.getResources().getString(R.string.ok), "");
//
//                }
//                //  mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            } catch (final Exception e) {
//                // TODO: handle exception
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if (mDialog.isShowing())
//                            mDialog.dismiss();
//                        alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.image_failed), "" + NC.getResources().getString(R.string.ok), "");
//                        if (requestcode == FROM_GALLERY || requestcode == 1) {
//                            profileImage.setBackgroundDrawable(getResources().getDrawable(R.drawable.profile_img));
//                        } else {
//                            imageSelected = 0;
//                            if (fileimg != null)
//                                fileimg.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_file));
//                        }
//                    }
//                });
//            }
//            return mBitmap;
//        }
//
//        @Override
//        protected void onPostExecute(final Bitmap result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            System.out.println("Image...+" + this.result + "_________" + result + "______" + requestcode);
//            System.out.println("_____________hicam" + imageUri.toString());
////            if (mDialog.isShowing())
////                mDialog.dismiss();
////            if (result != null)
////                AppCacheImage.addBitmapToMemoryCache(this.result, result);
////            if (requestcode == FROM_GALLERY || requestcode == 1) {
////                profileImage.setBackgroundResource(0);
////                if (result != null)
////                    profileImage.setImageBitmap(result);
////            } else {
////                imageSelected = 1;
////                if (result != null)
////                    fileimg.setImageBitmap(result);
////            }
//
//
//            if (mDialog.isShowing())
//                mDialog.dismiss();
//
//            profileImage.setBackgroundResource(0);
//            if (result != null) {
////                final File file = new File(this.result);
////                try {
////                    Uri rui= Uri.parse(file.getPath());
////                    Bitmap bitmap = getThumbnail(rui);
////                    profileImage.setImageBitmap(bitmap);
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//                profileImage.setImageBitmap(result);
//                profileImage.invalidate();
//                System.out.println("____________________iiii" + result.getByteCount());
//            }
//
//        }
//    }


    /**
     * Decoding image file
     * f - fileobject
     */
    public static Bitmap decodeImageFile(final File f) {

        try {
            final BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            final int REQUIRED_SIZE = 400;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (final FileNotFoundException e) {
        }
        return null;
    }


    /**
     * Getting path
     */
    private String getRealPathFromURI(final String contentURI) {

        final Uri contentUri = Uri.parse(contentURI);
        final Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        else {
            cursor.moveToFirst();
            final int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    private void getCamera() {
        new AlertDialog.Builder(this).setMessage("" + NC.getResources().getString(R.string.choose_an_image)).setTitle("" + NC.getResources().getString(R.string.profile_image)).setCancelable(true).setNegativeButton("" + NC.getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                System.gc();
                final Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 0);
                dialog.cancel();
            }
        }).setPositiveButton("" + NC.getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                final File photo = new File(Environment.getExternalStorageDirectory() + "/TaxiMobility/img");
                if (!photo.exists())
                    photo.mkdirs();
                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                final File mediaFile = new File(photo.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
                imageUri = Uri.fromFile(mediaFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
            }
        }).show();
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // ResultActivity.startWithUri(SampleActivity.this, resultUri);
            System.out.println("Hellow" + resultUri);
            new ImageCompressionAsyncTask().execute(resultUri.toString());
        } else {
            // Toast.makeText(SampleActivity.this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(final int requestcode, final int resultcode, final Intent data) {
        try {
            //    System.out.println("cameeeeeeev" + requestcode + "__" + CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);
//            if (requestcode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultcode == AppCompatActivity.RESULT_OK) {
//
//                Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
//                System.out.println("cameeeeeee"+imageUri.toString());
//                // For API >= 23 we need to check specifically that we have permissions to read external storage,
//                // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
//                boolean requirePermissions = false;
//                if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
//                    System.out.println("cameeeeeeedd"+imageUri.toString());
//                    // request permissions and handle the result in onRequestPermissionsResult()
//                    requirePermissions = true;
//                    new ImageCompressionAsyncTask().execute(imageUri.toString());
//                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
//                } else {
//                    System.out.println("cameeeeeeeuu"+imageUri.toString());
//                    new ImageCompressionAsyncTask().execute(imageUri.toString());
//                   // mCurrentFragment.setImageUri(imageUri);
//                }
//            }
            if (requestcode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (resultcode == RESULT_OK) {
                System.gc();
                switch (requestcode) {
                    case 0:
                        try {
                            UCrop uCrop = UCrop.of(Uri.fromFile(new File(getRealPathFromURI(data.getDataString()))), Uri.fromFile(new File(MeAct.this.getCacheDir(), destinationFileName)))
                                    .useSourceImageAspectRatio().withAspectRatio(1, 1)
                                    .withMaxResultSize(400, 400);
                            UCrop.Options options = new UCrop.Options();
                            options.setToolbarColor(ContextCompat.getColor(MeAct.this, R.color.appbg));
                            options.setStatusBarColor(ContextCompat.getColor(MeAct.this, R.color.header_text));
                            options.setToolbarWidgetColor(ContextCompat.getColor(MeAct.this, R.color.header_text));
                            options.setMaxBitmapSize(1000000000);
                            uCrop.withOptions(options);
                            uCrop.start(MeAct.this);
                            //new ImageCompressionAsyncTask().execute(data.getDataString());
//                            CropImage.activity( Uri.parse(data.getDataString()))
//                                    .start(getContext(),this);
                        } catch (final Exception e) {
                        }
                        break;
                    case 1:
                        try {
                            new ImageCompressionAsyncTask().execute(imageUri.toString()).get();
//                            CropImage.activity(imageUri)
//                                    .start(getContext(),this);

                            UCrop.of(imageUri, Uri.fromFile(new File(MeAct.this.getCacheDir(), destinationFileName)))
                                    .withAspectRatio(1, 1)
                                    // .withMaxResultSize(2000, 2000)
                                    .start(MeAct.this);
                        } catch (final Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        } catch (final Exception e) {
        }
    }
    /**
     * Function for getting image from sd card or camera
     */
//    @Override
//    public void onActivityResult(final int requestcode, final int resultcode, final Intent data) {
//        try {
//            if (resultcode == RESULT_OK) {
//                System.gc();
//                switch (requestcode) {
//                    case 0:
//                        try {
//                            new ImageCompressionAsyncTask().execute(data.getDataString());
//                        } catch (final Exception e) {
//                        }
//                        break;
//                    case 1:
//                        try {
//                            new ImageCompressionAsyncTask().execute(imageUri.toString()).get();
//                        } catch (final Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                }
//            }
//        } catch (final Exception e) {
//        }
//    }
    /**
     * this method used to load the image from gallery or camera
     */
//    private void selectImage() {
//
//        try {
//            new AlertDialog.Builder(MeAct.this).setMessage("" + NC.getResources().getString(R.string.choose_an_image)).setTitle("" + NC.getResources().getString(R.string.upload_your_image)).setCancelable(true).setNegativeButton("" + NC.getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(final DialogInterface dialog, final int which) {
//                    // TODO Auto-generated method stub
//                    System.gc();
//                    final Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(intent, 2);
//                    dialog.cancel();
//                }
//            }).setPositiveButton("" + NC.getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(final DialogInterface dialog, final int which) {
//                    // TODO Auto-generated method stub
//                    dialog.cancel();
//                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    final File photo = new File(Environment.getExternalStorageDirectory() + "/TaxiMobility/img");
//                    if (!photo.exists())
//                        photo.mkdirs();
//                    final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                    final File mediaFile = new File(photo.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
//                    imageUri = Uri.fromFile(mediaFile);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                    startActivityForResult(intent, 3);
//                }
//            }).show();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
//
//        super.onActivityResult(requestcode, resultcode, data);
//        // System.out.println("nagaaaaaaa" + data.getDataString());
//        try {
//            if (resultcode == RESULT_OK) {
//                System.gc();
//                switch (requestcode) {
//                    case FROM_GALLERY:
//                        try {
//                            new ImageCompressionAsyncTask().execute(data.getDataString());
//                        } catch (final Exception e) {
//                        }
//                        break;
//                    case 1:
//                        try {
//                            System.out.println("_____________hicam" + imageUri.toString());
//                            // Glide.with(MeAct.this).load(new File(imageUri.toString())).into(profileImage);
//                            new ImageCompressionAsyncTask().execute(imageUri.toString()).get();
//
//                        } catch (final Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                    case 2:
//                        try {
//                            new ImageCompressionAsyncTask().execute(data.getDataString());
//                        } catch (final Exception e) {
//                        }
//                        break;
//                    case 3:
//                        try {
//                            new ImageCompressionAsyncTask().execute(imageUri.toString()).get();
//                        } catch (final Exception e) {
//                            e.printStackTrace();
//                        }
//                        break;
//                }
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//    }

    /**
     * @API call(method) to update the driver document file details and parsing the response
     */
    private class FileUpload implements APIResult {
        String msg = "";

        public FileUpload(String url) {

            try {
                if (isOnline()) {
                    JSONObject j = new JSONObject();
                    j.put("driver_id", SessionSave.getSession("Id", MeAct.this));
                    j.put("driver_document", file_base64);
                    j.put("device_type", "1");
                    new APIService_Retrofit_JSON(MeAct.this, this, j, false).execute(url);
                } else {
                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, final String result) {

            try {
                if (isSuccess) {
                    JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        msg = json.getString("message");
                        fileDialog.dismiss();
                        file_base64 = "";
                        alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    } else {
                        msg = json.getString("message");
                        fileDialog.dismiss();
                        file_base64 = "";
                        alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, "" + NC.getResources().getString(R.string.ok), "");
                    }
                } else {
                    fileDialog.dismiss();
//                    alert_view(MeAct.this, "" + getResources().getString(R.string.message), "" + getResources().getString(R.string.check_net_connection), "" + getResources().getString(R.string.ok), "");

                    runOnUiThread(new Runnable() {
                        public void run() {
                            ShowToast(MeAct.this, getString(R.string.server_error));
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
                fileDialog.dismiss();
            }
        }
    }

    /**
     * This method used to call logout API.
     */
    public void showtaxiDetails() {

        try {
            final View view = View.inflate(MeAct.this, R.layout.taxidetail_lay, null);
            if (tDialog != null && tDialog.isShowing())
                tDialog.cancel();
            tDialog = new Dialog(MeAct.this, R.style.NewDialog);

            tDialog.setContentView(view);
            tDialog.setCancelable(true);
            tDialog.show();
            Colorchange.ChangeColor((ViewGroup) tDialog.findViewById(R.id.alert_id), MeAct.this);
            FontHelper.applyFont(MeAct.this, tDialog.findViewById(R.id.alert_id));
            final TextView modelTxt = (TextView) tDialog.findViewById(R.id.modelTxt);
            final TextView taxinoTxt = (TextView) tDialog.findViewById(R.id.taxinoTxt);
            final TextView assignfromTxt = (TextView) tDialog.findViewById(R.id.assignfromTxt);
            final TextView assigntoTxt = (TextView) tDialog.findViewById(R.id.assigntoTxt);
            modelTxt.setText(taxi_model);
            taxinoTxt.setText(taxi_no);

//            assignfromTxt.setText("" + date_conversion(taxi_map_from));
//            assigntoTxt.setText("" + date_conversion(taxi_map_to));
            assignfromTxt.setText("" + (taxi_map_from));
            assigntoTxt.setText("" + (taxi_map_to));
            System.out.println("taxi map---------->" + (taxi_map_from));
            System.out.println("taxi map---------->" + (taxi_map_to));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > 500) ? (originalSize / 500) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    private class VoucherRecharge implements APIResult {
        public VoucherRecharge(String url, JSONObject data) {
            try {
                if (isOnline()) {
                    new APIService_Retrofit_JSON(MeAct.this, this, data, false).execute(url);
                } else {
                    alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + NC.getResources().getString(R.string.check_net_connection), "" + NC.getResources().getString(R.string.ok), "");
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        public void getResult(boolean isSuccess, String result) {
               String msg = "";
            if (isSuccess){
                try{
                    JSONObject json = new JSONObject(result);
                     msg=json.getString("message");
                    if (json.getInt("status") == 1) {

                        alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, NC.getResources().getString(R.string.ok), "");
                        walletamount.setText(SessionSave.getSession("site_currency", MeAct.this) + " " + json.getString("driver_wallet_amount"));

                    }else {
                        alert_view(MeAct.this, "" + NC.getResources().getString(R.string.message), "" + msg, NC.getResources().getString(R.string.ok), "");
                    }
                }catch (JSONException ee ){
                    ee.fillInStackTrace();
                }

            }

        }
    }
}