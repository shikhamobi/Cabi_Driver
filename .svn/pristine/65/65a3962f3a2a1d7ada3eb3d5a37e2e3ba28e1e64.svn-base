package com.cabi.driver;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.cabi.driver.utils.AppCacheImage;
import com.cabi.driver.utils.Colorchange;
import com.cabi.driver.utils.NC;
import com.cabi.driver.utils.RoundedImageView;
import com.cabi.driver.utils.SessionSave;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

/**
 * This class is used to invite friends in social media using referal code
 */
public class InviteFriendAct extends MainActivity implements OnClickListener {
    private LinearLayout SlideImg;
    private LinearLayout Donelay;
    private TextView BackBtn;
    private TextView leftIcon;
    private TextView HeadTitle;
    private TextView ismsTxt;
    private TextView iemailTxt;
    private TextView ifacebookTxt;
    private TextView iwhatsappTxt;
    private TextView itwitterTxt;
    private TextView referalamtTxt;
    private TextView referalcdeTxt;
    public static InviteFriendAct mInviteAct;
    private String PENDING_ACTION_BUNDLE_KEY = "";
    private boolean canPresentShareDialog;
    private boolean canPresentShareDialogWithPhotos;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;
    private PendingAction pendingAction = PendingAction.NONE;
    private RoundedImageView profileImg;
    private String invitesubject = "";
    private String invitemsg = "";
    private double refamount = 0.0;
    private RelativeLayout invite_main;
    ImageView invite_loading;
    private String[] packarr;
    private TextView wallet_history;

    /**
     * Setting layout in activity
     */
    @Override
    public int setLayout() {
        // TODO Auto-generated method stub
        return R.layout.invitefriend_lay;

    }

    /**
     * Loading UI Component Resources
     */
    public void priorChanges() {
        // FontHelper.applyFont(this, findViewById(R.id.rootContain));
        PENDING_ACTION_BUNDLE_KEY = getPackageName() + "PendingAction";
        profileImg = (RoundedImageView) findViewById(R.id.profileImg);
        Donelay = (LinearLayout) findViewById(R.id.rightlay);
        Donelay.setVisibility(View.INVISIBLE);
        leftIcon = (TextView) findViewById(R.id.leftIcon);
        leftIcon.setVisibility(View.VISIBLE);
        leftIcon.setBackgroundResource(R.drawable.back);
        BackBtn = (TextView) findViewById(R.id.back_text);
        BackBtn.setVisibility(View.GONE);
        HeadTitle = (TextView) findViewById(R.id.header_titleTxt);
        HeadTitle.setText(NC.getResources().getString(R.string.invite_friend));
    }

    @Override
    public void Initialize() {
        // TODO Auto-generated method stub
        priorChanges();
        if (!SessionSave.getSession("facebook_key", InviteFriendAct.this).equals(""))
            FacebookSdk.setApplicationId(SessionSave.getSession("facebook_key", InviteFriendAct.this));
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        AppEventsLogger.deactivateApp(this);
        invite_main = (RelativeLayout) findViewById(R.id.invite_main);
        mInviteAct = this;

        invite_loading = (ImageView) findViewById(R.id.invite_loading);
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(invite_loading);
        Glide.with(InviteFriendAct.this)
                .load(R.raw.loading_anim)
                .into(imageViewTarget);

        Colorchange.ChangeColor((ViewGroup) (((ViewGroup) InviteFriendAct.this
                .findViewById(android.R.id.content)).getChildAt(0)), InviteFriendAct.this);

        SlideImg = (LinearLayout) findViewById(R.id.leftIconTxt);

        ismsTxt = (TextView) findViewById(R.id.ismsTxt);
        iemailTxt = (TextView) findViewById(R.id.iemailTxt);
        ifacebookTxt = (TextView) findViewById(R.id.ifacebookTxt);
        iwhatsappTxt = (TextView) findViewById(R.id.iwhatsappTxt);
        itwitterTxt = (TextView) findViewById(R.id.itwitterTxt);
        wallet_history = (TextView) findViewById(R.id.wallet_history);
        // profileImg = (RoundedImageView) findViewById(R.id.profileImg);
        referalcdeTxt = (TextView) findViewById(R.id.referalcdeTxt);
        referalamtTxt = (TextView) findViewById(R.id.referalamtTxt);
        // referalcdeTxt.setInputType(Typeface.BOLD);
        if (BsavedInstanceState != null) {
            String name = BsavedInstanceState.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, shareCallback);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                handlePendingAction();
            }
        };

        SlideImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Can we present the share dialog for regular links?
        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
        // Can we present the share dialog for photos?
        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);
        String Profileimgepath = SessionSave.getSession("ProfileImage", InviteFriendAct.this);
        if (!AppCacheImage.loadBitmap(Profileimgepath, profileImg)) {

            // new DownloadImageAndsavetoCache(profileImg).execute(Profileimgepath);

        }
        if (!SessionSave.getSession("RefCode", InviteFriendAct.this).equalsIgnoreCase("") && !SessionSave.getSession("RefCode", InviteFriendAct.this).equalsIgnoreCase(null))
            referalcdeTxt.setText("" + SessionSave.getSession("RefCode", InviteFriendAct.this));
        if (!SessionSave.getSession("RefAmount", InviteFriendAct.this).equalsIgnoreCase("") && !SessionSave.getSession("RefAmount", InviteFriendAct.this).equalsIgnoreCase(null))
            refamount = Double.parseDouble(SessionSave.getSession("RefAmount", InviteFriendAct.this));
        referalamtTxt.setText("" + SessionSave.getSession("site_currency", InviteFriendAct.this) + "" + String.format(Locale.UK, "%.2f", refamount));
        Package pack = InviteFriendAct.this.getClass().getPackage();
        String packtxt = pack.toString();
        packarr = packtxt.split(" ");
        invitesubject = "" + NC.getResources().getString(R.string.invite_friend);
        //  invitemsg = " Excuse my brevity,Sign up with the referral code " + SessionSave.getSession("RefCode", InviteFriendAct.this) + " and earn " + SessionSave.getSession("Currency", InviteFriendAct.this) + String.format("%.2f", refamount) + " .Download the app from:https://play.google.com/store/apps/details?id=" + packarr[1];
        // referalcdeTxt.setTypeface(setcontentTypeface(), Typeface.BOLD);

        //check referal amount
        CheckReferral();

        setOnclickListener();
    }


    /**
     * Checking Referal amount
     */
    private void CheckReferral() {

        try {
            JSONObject j = new JSONObject();
            j.put("driver_id", SessionSave.getSession("Id", InviteFriendAct.this));
            String url = "type=driver_invite_with_referral";
            new RefferalAmt(url, j);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * This class used to check RefferalAmt
     *
     * @author developer
     */
    private class RefferalAmt implements APIResult {
        private RefferalAmt(final String url, JSONObject data) {

            new APIService_Retrofit_JSON(InviteFriendAct.this, this, data, false).execute(url);
        }

        @Override
        public void getResult(final boolean isSuccess, final String result) {
            // TODO Auto-generated method stub

            if (isSuccess) {
                invite_main.setVisibility(View.VISIBLE);
                invite_loading.setVisibility(View.GONE);
                try {
                    final JSONObject json = new JSONObject(result);
                    if (json.getInt("status") == 1) {
                        SessionSave.saveSession("RefCode", json.getJSONObject("detail").getString("referral_code"), InviteFriendAct.this);
                        SessionSave.saveSession("RefAmount", json.getJSONObject("detail").getString("referral_amount"), InviteFriendAct.this);

                        if (!SessionSave.getSession("RefCode", InviteFriendAct.this).equalsIgnoreCase("") && !SessionSave.getSession("RefCode", InviteFriendAct.this).equalsIgnoreCase(null))
                            referalcdeTxt.setText("" + SessionSave.getSession("RefCode", InviteFriendAct.this));
                        if (!SessionSave.getSession("RefAmount", InviteFriendAct.this).equalsIgnoreCase("") && !SessionSave.getSession("RefAmount", InviteFriendAct.this).equalsIgnoreCase(null))
                            refamount = Double.parseDouble(SessionSave.getSession("RefAmount", InviteFriendAct.this));
                        // invitemsg = " Excuse my brevity,Sign up with the referral code " + SessionSave.getSession("RefCode", InviteFriendAct.this) + " and earn " + SessionSave.getSession("Currency", InviteFriendAct.this) + String.format("%.2f", refamount) + ".Download the app from:https://play.google.com/store/apps/details?id=" + packarr[1];

                        invitemsg = NC.getResources().getString(R.string.excuse_me_brevity) + SessionSave.getSession("RefCode", InviteFriendAct.this) + NC.getResources().getString(R.string.and_earn) + " " + SessionSave.getSession("Currency", InviteFriendAct.this) + String.format(Locale.UK, "%.2f", refamount) + " " + NC.getResources().getString(R.string.download_from) + "https://play.google.com/store/apps/details?id=" + getPackageName();
                        referalamtTxt.setText("" + SessionSave.getSession("site_currency", InviteFriendAct.this) + " " + String.format(Locale.UK, "%.2f", refamount));



                        Picasso.with(InviteFriendAct.this).load(json.getJSONObject("detail").getString("profile_image")).placeholder(R.drawable.loadingimage).into(profileImg);
                    } else if (json.getInt("status") == -1) {
                        startActivity(new Intent(InviteFriendAct.this, MeAct.class));
                        SessionSave.saveSession("referal", "0", getApplicationContext());
                        finish();

                    }

                } catch (final Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
//                alert_view(InviteFriendAct.this, "Message", "" + result, "" + getResources().getString(R.string.ok), "");
                runOnUiThread(new Runnable() {
                    public void run() {
                        ShowToast(InviteFriendAct.this, getString(R.string.server_error));
                    }
                });
            }
        }
    }


    /**
     * Invite friends via facebook
     */
    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {


        }

        @Override
        public void onError(FacebookException error) {

            String title = NC.getString(R.string.connect_error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {


            if (result.getPostId() != null) {
                NC.getString(R.string.t_success);
                result.getPostId();
            }
        }

        private void showResult(String title, String alertMessage) {

            new AlertDialog.Builder(InviteFriendAct.this).setTitle(title).setMessage(alertMessage).setPositiveButton(R.string.ok, null).show();
        }
    };

    /**
     * Enum class
     */
    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    /**
     * Onclick listener
     */
    private void setOnclickListener() {

        BackBtn.setOnClickListener(this);
        ismsTxt.setOnClickListener(this);
        iemailTxt.setOnClickListener(this);
        ifacebookTxt.setOnClickListener(this);
        itwitterTxt.setOnClickListener(this);
        wallet_history.setOnClickListener(this);
        iwhatsappTxt.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {

        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onDestroy() {

        if (mshowDialog != null && mshowDialog.isShowing()) {
            mshowDialog.dismiss();
            mshowDialog = null;

        }
        super.onDestroy();
        profileTracker.stopTracking();
    }

    /**
     * This method will update fb staus
     */
    private void onClickPostStatusUpdate() {

        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
    }


    /**
     * Posting fb status update
     */
    private void postStatusUpdate() {

        Profile profile = Profile.getCurrentProfile();
        //		 return new FacebookDialog.ShareDialogBuilder(this).setCaption("" + getResources().getString(R.string.successfully_completed) + "" + pickup_place).setName("" + getResources().getString(R.string.app_name)).setDescription(SessionSave.getSession("TellfrdMsg", ReceiptAct.this) + "\n" + getResources().getString(R.string.pass_app_link) + "\n" + getResources().getString(R.string.driver_app_link)).setLink(SessionSave.getSession("api_base", ReceiptAct.this));
        // String link = SessionSave.getSession("api_base", InviteFriendAct.this);



        //invitemsg ="test app";

        // ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle(invitesubject).setContentDescription(invitemsg).setContentUrl(Uri.parse(SessionSave.getSession("api_base", InviteFriendAct.this))).build();

        //ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle("test").setContentDescription("test1").setContentUrl(Uri.parse("www.google.com")).build();
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentTitle("Hello Facebook")
//
//                    .setContentDescription(
//                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl((Uri.parse(SessionSave.getSession("api_base", InviteFriendAct.this))))
                    .setQuote(invitemsg)
                    .build();


//            ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
//                    .putString("og:type", "books.book")
//                    .putString("og:title", "A Game of Thrones")
//                    .putString("og:description", "In the frozen wastes to the north of Winterfell, sinister and supernatural forces are mustering.")
//                    .putString("books:isbn", "0-553-57340-3")
//                    .build();
//            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
//                    .setActionType("books.reads")
//                    .putObject("book", object)
//
//                    .build();
//
//
//
//            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
//                    .setPreviewPropertyName("book")
//                    .setAction(action)
//                    .build();
//
//            ShareDialog.show(InviteFriendAct.this, content);

            shareDialog.show(linkContent);
        }
//        if (canPresentShareDialog) {
//            shareDialog.show(linkContent);
//        } else if (profile != null && hasPublishPermission()) {
//            ShareApi.share(linkContent, shareCallback);
//        } else {
//            pendingAction = PendingAction.POST_STATUS_UPDATE;
//        }
    }


    /**
     * Checking public permission availability
     */
    private boolean hasPublishPermission() {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    /**
     * Publish social media content
     */
    private void performPublish(PendingAction action, boolean allowNoToken) {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null || allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }

    /**
     * <p>
     * This method used to perform what action to be done is photo share or status update.
     * </p>
     */
    @SuppressWarnings("incomplete-switch")
    private void handlePendingAction() {

        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but
        // we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;
        switch (previouslyPendingAction) {
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }


    /**
     * This method is used to invite via sms
     */
    public void sendSms() {

        final Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + ""));
        intentsms.putExtra("sms_body", invitemsg);
        startActivity(intentsms);
    }


    /**
     * This method is used to invite via email
     */
    public void sendEmail() {

        final String[] TO = {""};
        final String[] CC = {""};
        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + NC.getResources().getString(R.string.app_name) + "-" + invitesubject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "\n" + invitemsg);
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(emailIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.google.android.gm")) {
                emailIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                break;
            }
        }
        try {
            startActivity(Intent.createChooser(emailIntent, NC.getResources().getString(R.string.sendmail)));
        } catch (final android.content.ActivityNotFoundException ex) {
            ShowToast(InviteFriendAct.this, "" + NC.getResources().getString(R.string.there_is_no_email_client_installed));
        }
    }


    /**
     * This method is used to invite via twitter
     */
    public void initShareIntentTwi(final String type) {

        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, invitemsg);
        tweetIntent.setType("text/plain");
        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Toast.makeText(this, "Twitter app isn't found", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android"));
            startActivity(i);
        }
    }


    /**
     * This method is used to invite via whatsapp
     */
    public void onClickWhatsApp() {

        PackageManager pm = getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "" + invitemsg;
            PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));
        } catch (NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"));
            startActivity(i);
        }
    }


    /**
     * To perform onclick listener
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.back_text:
                onBackPressed();
                break;
            case R.id.ismsTxt:
                sendSms();
                break;
            case R.id.iemailTxt:
                sendEmail();
                break;
            case R.id.ifacebookTxt:
                onClickPostStatusUpdate();
                break;
            case R.id.iwhatsappTxt:
                onClickWhatsApp();
                break;
            case R.id.itwitterTxt:
                initShareIntentTwi("com.twitter.android");
                break;
            case R.id.wallet_history:
                startActivity(new Intent(InviteFriendAct.this, WithdrawHistoryAct.class));
                break;
            default:
                break;
        }
    }

    // Slider menu used to move from one activity to another activity.

}
