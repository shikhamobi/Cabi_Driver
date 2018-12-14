/*
package com.cabi.driver.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cabi.driver.MainActivity;
import com.cabi.driver.R;
import com.cabi.driver.data.MapWrapperLayout;
import com.cabi.driver.interfaces.APIResult;
import com.cabi.driver.service.APIService_Retrofit_JSON;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

*/
/**
 * Created by developer on 14/2/18.
 *//*


public class SelectDropLocationAct extends MainActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private ImageView btn_back;
    private Button Complete_trip;
    private GoogleMap googleMap;
    private MapWrapperLayout mapWrapperLayout;
    private static TextView TappedLocation;
    public String DropAddress = "";
    public Double droplatitude = 0.0, droplongitude = 0.0;
    public static boolean GEOCODE_EXPIRY = false;
    public Address_s address;

    @Override
    public int setLayout() {
        return R.layout.select_droplocation_lay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void Initialize() {
        FontHelper.applyFont(this, findViewById(R.id.select_drop_location));
        Colorchange.ChangeColor((ViewGroup) findViewById(R.id.select_drop_location), this);
        btn_back = (ImageView) findViewById(R.id.back_icon);
        Complete_trip = (Button) findViewById(R.id.butt_onboard);
        Complete_trip.setVisibility(View.GONE);
        TappedLocation = (TextView) findViewById(R.id.tapped_location);
        findViewById(R.id.header_titleTxt).setVisibility(View.GONE);
        Glide.with(this).load(SessionSave.getSession("image_path", this) + "headerLogo.png").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into((ImageView) findViewById(R.id.header_titleTxt));
        btn_back.setOnClickListener(this);
        Complete_trip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_icon:
                finish();
                break;
            case R.id.butt_onboard:
                UpdateLocationData();
                break;
            default:
                break;
        }
    }

    private void UpdateLocationData() {
        if (!TappedLocation.getText().toString().equals("") && droplatitude != 0.0 && droplongitude != 0.0) {
            Bundle conData = new Bundle();
            conData.putString("param_result", TappedLocation.getText().toString());
            conData.putDouble("lat", droplatitude);
            conData.putDouble("lng", droplongitude);
            Intent intent = new Intent();
            intent.putExtras(conData);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.getUiSettings().setZoomControlsEnabled(false);
        MapsInitializer.initialize(SelectDropLocationAct.this);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        mapWrapperLayout.init(googleMap, getPixelsFromDp(SelectDropLocationAct.this, 39 + 20));
        mapWrapperLayout.setVisibility(View.VISIBLE);
        if (!SessionSave.getSession("lastknowlats", SelectDropLocationAct.this).equals("")) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(SessionSave.getSession("lastknowlats", SelectDropLocationAct.this)), Double.parseDouble(SessionSave.getSession("lastknowlngs", SelectDropLocationAct.this))), 16f));
        }
        googleMap.setOnMapClickListener(this);
    }

    */
/**
     * Get the google map pixels from xml density independent pixel.
     *//*

    public static int getPixelsFromDp(final Context context, final float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (googleMap != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            Complete_trip.setVisibility(View.VISIBLE);
            if (address != null)
                address.cancel(true);
            address = new Address_s(SelectDropLocationAct.this, new LatLng(latLng.latitude, latLng.longitude));
            address.execute();
        }
    }

    */
/**
     * Address parsing from given lat and long
     *//*

    public class Address_s extends AsyncTask<String, String, String> {
        public Context mContext;
        LatLng mPosition;
        String Address = "";
        Geocoder geocoder;
        List<android.location.Address> addresses = null;
        private double latitude;
        private double longitude;

        public Address_s(Context context, LatLng position) {
            showLoading(SelectDropLocationAct.this);
            mContext = context;
            mPosition = position;
            latitude = mPosition.latitude;
            longitude = mPosition.longitude;
            geocoder = new Geocoder(context, Locale.getDefault());
        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            SessionSave.saveSession("notes", "", mContext);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                System.out.println("address size11ssss:" + latitude + "%$#" + longitude);

                if (Geocoder.isPresent()) {
                    System.out.println("address size:" + latitude + "%$#" + longitude);
                    addresses = geocoder.getFromLocation(latitude, longitude, 3);
                    System.out.println("address size:" + addresses.size());
                    if (addresses.size() == 0) {
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);
                    } else {
                        for (int i = 0; i < addresses.size(); i++) {
                            Address += addresses.get(0).getAddressLine(i) + ", ";
                        }
                        System.out.println("____________aaa" + Address);
                        if (Address.length() > 0) {
                            Address = Address.substring(0, Address.length() - 2);
                            try {
                                Address = Address.replaceAll("null", "").replaceAll(", ,", "").replaceAll(", ,", "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    if (NetworkStatus.isOnline(mContext))
                        new convertLatLngtoAddressApi(mContext, latitude, longitude);
                    else {
                        SelectDropLocationAct.setfetch_address();
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if (NetworkStatus.isOnline(mContext))
                    new convertLatLngtoAddressApi(mContext, latitude, longitude);
                else {
                    SelectDropLocationAct.setfetch_address();
                }
            }
            return Address;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // TaxiUtil.pAddress = "" + Address;
            if (Address.length() != 0 && SelectDropLocationAct.this != null) {
                try {
                    Address = Address.replaceAll("null", "").replaceAll(", ,", "").replaceAll(", ,", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cancelLoading();
                TappedLocation.setText(Address);
                DropAddress = Address;
                droplatitude = latitude;
                droplongitude = longitude;
            } else {
                new convertLatLngtoAddressApi(mContext, latitude, longitude);

            }
            result = null;

        }

        */
/**
         * Set location and response parsing
         *//*

        public void setLocation(String inputJson) {

            try {
                cancelLoading();
                JSONObject object = new JSONObject("" + inputJson);
                JSONArray array = object.getJSONArray("results");
                if (array.length() > 0) {
                    object = array.getJSONObject(0);
                    System.out.println("address size11:" + latitude + "%$#" + longitude + object.getString("formatted_address"));
                    //   SessionSave.saveSession("drop_location", object.getString("formatted_address"), SelectDropLocationAct.this);
                    TappedLocation.setText(Address);
                    DropAddress = Address;
                    droplatitude = latitude;
                    droplongitude = longitude;
                } else
                    DropAddress = "Tap your current Location on Map";
                droplongitude = 0.0;
                droplatitude = 0.0;
                GEOCODE_EXPIRY = true;
                CToast.ShowToast(mContext, NC.getString(R.string.c_tryagain));

            } catch (Exception ex) {
                ex.printStackTrace();
                cancelLoading();
                DropAddress = NC.getString(R.string.tap_drop);
                droplongitude = 0.0;
                droplatitude = 0.0;
                GEOCODE_EXPIRY = true;
                if (mContext != null)
                    if (!NetworkStatus.isOnline(mContext))
                        CToast.ShowToast(mContext, NC.getString(R.string.check_internet));
                    else
                        CToast.ShowToast(mContext, NC.getString(R.string.c_tryagain));
                SelectDropLocationAct.setfetch_address();
            }
        }

        */
/**
         * Convert lat and lon to address
         *//*

        public class convertLatLngtoAddressApi implements APIResult {
            public convertLatLngtoAddressApi(Context c, double lati, double longi) {

                if (GEOCODE_EXPIRY) {
                    // googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                    String googleMapUrl = SessionSave.getSession("base_url", SelectDropLocationAct.this) + "?type=google_geocoding";
                    try {
                        JSONObject j = new JSONObject();
                        j.put("origin", lati + "," + longi);
                        j.put("type", "2");
                        new APIService_Retrofit_JSON(c, this, j, false, googleMapUrl, false).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    String googleMapUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lati + "," + longi + "&sensor=false";
                    new APIService_Retrofit_JSON(c, this, null, true, googleMapUrl, true).execute();
                }
            }

            @Override
            public void getResult(boolean isSuccess, String result) {
                if (result != null)
                    setLocation(result.toString());
            }
        }

    }

    */
/**
     * Set fetching address
     *//*

    public static void setfetch_address() {
        TappedLocation.setText(R.string.fetching_address);
    }
}
*/
