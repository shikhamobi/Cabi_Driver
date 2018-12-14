/*
package com.cabi.driver.location;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cabi.driver.interfaces.LocalDistanceInterface;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.text.DateFormat;
import java.util.Date;

*/
/**
 * Created by developer on 14/2/18.
 *//*


public class LocalDistanceCalculation extends Activity {

    private double slabDistance = 100;
    private static boolean ROUTE_EXPIRED_TODAY = false;
    public static final String CUSTOM_ACTION = "YOUR_CUSTOM_ACTION";
    Intent bintent = new Intent(CUSTOM_ACTION);
    public static LocalDistanceInterface Localdistanceinterface;
    public static Context localcontext;

    public static void registerDistanceInterface(LocalDistanceInterface distanceInterface) {
        Localdistanceinterface = distanceInterface;
    }

    public static LocalDistanceCalculation newInstance(Context context) {
        localcontext = context;
        LocalDistanceCalculation localdistance = new LocalDistanceCalculation();
        return localdistance;
    }

    */
/**
     * This Function is used for calculate the distance travelled
     *//*

    public synchronized void haversine(final double lat1, final double lon1, final double lat2, final double lon2) {
        // TODO Auto-generated method stub
        //Getting both the coordinates
        LatLng from = new LatLng(lat1, lon1);
        LatLng to = new LatLng(lat2, lon2);


//        Message message = mHandler.obtainMessage(1, "");
//        message.sendToTarget();

        //Calculating the distance in meters

        double distance = (float) SphericalUtil.computeDistanceBetween(from, to) / 1000;
        if (SessionSave.getSession("Metric", localcontext).trim().equalsIgnoreCase("miles"))
            distance = distance / 1.60934;
        System.out.println("Haversine Distance" + (distance));
        if ((distance * 1000) > slabDistance) {
            // new FindApproxDistance(from, to);
        } else {
            // distance += SessionSave.getDistance(localcontext);
            //  SessionSave.setDistance(distance, localcontext);
            Localdistanceinterface.haversineResult(true);
            Handler mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    // This is where you do your work in the UI thread.
                    // Your worker tells you in the message what to do.
                    //  Toast.makeText(LocationUpdate.this, "" + SessionSave.getDistance(LocationUpdate.this) + "Accuracy " + mLastLocation.getAccuracy() + " Speed  " + speed + "  Google speed" + SessionSave.getGoogleDistance(LocationUpdate.this), Toast.LENGTH_SHORT).show();
                    String savingTripDetail = "";
                    savingTripDetail += SessionSave.getSession(SessionSave.getSession("trip_id", localcontext) + "data", localcontext) + "\n\n\n<br><br>" + "Distance**85#&nbsp;" + SessionSave.getDistance(localcontext) + "&nbsp;&nbsp;Trip&nbsp;" + SessionSave.getSession("trip_id", localcontext) + "&nbsp;&nbsp;Speed&nbsp;" + "&nbsp;&nbsp;Time&nbsp;" + DateFormat.getTimeInstance().format(new Date()) +
                            "&nbsp;&nbsp;old&nbsp;&nbsp;" + lat1 + "&nbsp;" + lon1 + "&nbsp;&nbsp;New&nbsp;&nbsp;" + lat2
                            + "&nbsp;" + lon2
                            + "&nbsp;&nbsp;Read way&nbsp;&nbsp;" + SessionSave.ReadGoogleWaypoints(localcontext);

                    SessionSave.saveSession(SessionSave.getSession("trip_id", localcontext) + "data", savingTripDetail, localcontext);
                    // System.out.println("trip save"+SessionSave.getSession(SessionSave.getSession("trip_id", LocationUpdate.this) + "data", LocationUpdate.this) + "\n" + "Distance " + SessionSave.getDistance(LocationUpdate.this) + "Trip " + SessionSave.getSession("trip_id", LocationUpdate.this));

                }
            };
            mHandler.sendEmptyMessage(0);
        }

    }

    */
/**
     * For Calculating approx distance for eta and trip if user has buisness key
     *//*


    */
/*public class FindApproxDistance implements APIResult {
        int type;
        LatLng pick = null;
        LatLng drop = null;

        public FindApproxDistance(LatLng from, LatLng to) {
            //Toast.makeText(localcontext, "Calling Google", Toast.LENGTH_SHORT).show();
            ArrayList<LatLng> points = new ArrayList<>();
            pick = from;
            drop = to;
            points.add(pick);
            points.add(drop);
            String url = new Route().GetDistanceTime(localcontext, points, "en", false);
            if (url != null && !url.equals("")) {
                if (ROUTE_EXPIRED_TODAY) {
                    url="google_geocoding";
                    JSONObject j = new JSONObject();
                    try {
                        j.put("origin", from.latitude + "," + from.longitude);
                        j.put("destination", to.latitude + "," + to.longitude);
                        j.put("type", "3");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new APIService_Retrofit_JSON(localcontext, this, j, false, url, false).execute();
                } else

                    //https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY&key=YOUR_API_KEY
                    new APIService_Retrofit_JSON(localcontext, this, null, true, url, true).execute();

                System.out.println("carmodel" + url);
            }

        }


        @Override
        public void getResult(boolean isSuccess, String result) {

            if (isSuccess) {
                try {
                    System.out.println("carmodel" + result.replaceAll("\\s", ""));
                    JSONObject data= new JSONObject(result);
                    if (!data.getString("status").equalsIgnoreCase("OK")) {
                        if (result != null && data.getString("status").equalsIgnoreCase("OVER_QUERY_LIMIT") && !ROUTE_EXPIRED_TODAY) {
                            ROUTE_EXPIRED_TODAY = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new FindApproxDistance(pick, drop);
                                }
                            }, 2000);

                        }else{
                            double distance = (float) SphericalUtil.computeDistanceBetween(pick, drop) / 1000;
                            if (SessionSave.getSession("Metric", localcontext).trim().equalsIgnoreCase("miles"))
                                distance = distance / 1.60934;
                            System.out.println("Haversine Distance" + (distance));


                            distance += SessionSave.getGoogleDistance(localcontext);
                            SessionSave.setGoogleDistance(distance, localcontext);
                            System.out.println("googledistanceee "+"5_"+pick.latitude+"__"+pick.longitude+"_____"+drop.latitude+"__"+drop.longitude);
                            SessionSave.saveGoogleWaypoints(pick , drop, "haversine", distance, "UNKNOWN"+result, localcontext);
                            //LocalBroadcastManager.getInstance(localcontext).sendBroadcast(bintent);

                            String savingTripDetail = "";
                            savingTripDetail += SessionSave.getSession(SessionSave.getSession("trip_id", localcontext) + "data", localcontext) + "\n\n\n<br><br>" + "Distance**168#&nbsp;" + SessionSave.getDistance(localcontext) + "&nbsp;&nbsp;Trip&nbsp;" + SessionSave.getSession("trip_id", localcontext) + "&nbsp;&nbsp;Speed&nbsp;"+ "&nbsp;&nbsp;Time&nbsp;" + DateFormat.getTimeInstance().format(new Date()) +
                                    "&nbsp;&nbsp;old&nbsp;&nbsp;" + pick.latitude + "&nbsp;" + pick.longitude + "&nbsp;&nbsp;New&nbsp;&nbsp;" + drop.latitude
                                    + "&nbsp;" + drop.longitude
                                    + "&nbsp;&nbsp;Read way&nbsp;&nbsp;" + SessionSave.ReadGoogleWaypoints(localcontext);

                            SessionSave.saveSession(SessionSave.getSession("trip_id", localcontext) + "data", savingTripDetail, localcontext);



                            Localdistanceinterface.haversineResult(true);
                        }
                    } else {
                        if (new JSONObject(result).getJSONArray("rows").length() != 0) {
                            JSONObject obj = new JSONObject(result).getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
                            JSONObject ds = obj.getJSONObject("distance");
                            String dis = ds.getString("value");
//                        JSONObject timee = obj.getJSONObject("duration");
//                        String time = timee.getString("value");
//                        double times = Double.parseDouble(time) / 60;
                            double dist = Double.parseDouble(dis) / 1000;
                            if (SessionSave.getSession("Metric", localcontext).trim().equalsIgnoreCase("miles"))
                                dist = dist / 1.60934;
                            SessionSave.setGoogleDistance(SessionSave.getGoogleDistance(localcontext) + dist, localcontext);
                            System.out.println("googledistanceee "+"6_"+pick.latitude+"__"+pick.longitude+"_____"+drop.latitude+"__"+drop.longitude);
                            SessionSave.saveGoogleWaypoints(pick , drop, "google", dist, "", localcontext);
                            //LocalBroadcastManager.getInstance(localcontext).sendBroadcast(bintent);
                            System.out.println("broad__________" + dist + "__" + "__" + SessionSave.getGoogleDistance(localcontext));


                            String savingTripDetail = "";
                            savingTripDetail += SessionSave.getSession(SessionSave.getSession("trip_id", localcontext) + "data", localcontext) + "\n\n\n<br><br>" + "Distance**195#&nbsp;" + SessionSave.getDistance(localcontext) + "&nbsp;&nbsp;Trip&nbsp;" + SessionSave.getSession("trip_id", localcontext) + "&nbsp;&nbsp;Speed&nbsp;"+ "&nbsp;&nbsp;Time&nbsp;" + DateFormat.getTimeInstance().format(new Date()) +
                                    "&nbsp;&nbsp;old&nbsp;&nbsp;" + pick.latitude + "&nbsp;" + pick.longitude + "&nbsp;&nbsp;New&nbsp;&nbsp;" + drop.latitude
                                    + "&nbsp;" + drop.longitude
                                    + "&nbsp;&nbsp;Read way&nbsp;&nbsp;" + SessionSave.ReadGoogleWaypoints(localcontext);

                            SessionSave.saveSession(SessionSave.getSession("trip_id", localcontext) + "data", savingTripDetail, localcontext);


                            Localdistanceinterface.haversineResult(true);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    if (NetworkStatus.isOnline(localcontext)) {
                        if (!ROUTE_EXPIRED_TODAY) {
                            ROUTE_EXPIRED_TODAY = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new FindApproxDistance(pick, drop);
                                }
                            }, 2000);
                        } else {
                            double distance = (float) SphericalUtil.computeDistanceBetween(pick, drop) / 1000;
                            if (SessionSave.getSession("Metric", localcontext).trim().equalsIgnoreCase("miles"))
                                distance = distance / 1.60934;
                            System.out.println("Haversine Distance" + (distance));
                            distance += SessionSave.getGoogleDistance(localcontext);
                            SessionSave.setGoogleDistance(distance, localcontext);
                            System.out.println("googledistanceee "+"7_"+pick.latitude+"__"+pick.longitude+"_____"+drop.latitude+"__"+drop.longitude);
                            SessionSave.saveGoogleWaypoints(pick , drop, "haversine", distance, e.getLocalizedMessage(), localcontext);
                            //LocalBroadcastManager.getInstance(localcontext).sendBroadcast(bintent);

                            String savingTripDetail = "";
                            savingTripDetail += SessionSave.getSession(SessionSave.getSession("trip_id", localcontext) + "data", localcontext) + "\n\n\n<br><br>" + "Distance**230#&nbsp;" + SessionSave.getDistance(localcontext) + "&nbsp;&nbsp;Trip&nbsp;" + SessionSave.getSession("trip_id", localcontext) + "&nbsp;&nbsp;Speed&nbsp;"+ "&nbsp;&nbsp;Time&nbsp;" + DateFormat.getTimeInstance().format(new Date()) +
                                    "&nbsp;&nbsp;old&nbsp;&nbsp;" + pick.latitude + "&nbsp;" + pick.longitude + "&nbsp;&nbsp;New&nbsp;&nbsp;" + drop.latitude
                                    + "&nbsp;" + drop.longitude
                                    + "&nbsp;&nbsp;Read way&nbsp;&nbsp;" + SessionSave.ReadGoogleWaypoints(localcontext);

                            SessionSave.saveSession(SessionSave.getSession("trip_id", localcontext) + "data", savingTripDetail, localcontext);

                            Localdistanceinterface.haversineResult(true);
                        }

                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new FindApproxDistance(pick, drop);
                            }
                        }, 2000);


                    }


                }


            }else {
                double distance = (float) SphericalUtil.computeDistanceBetween(pick, drop) / 1000;
                if (SessionSave.getSession("Metric", localcontext).trim().equalsIgnoreCase("miles"))
                    distance = distance / 1.60934;
                System.out.println("Haversine Distance" + (distance));
                distance += SessionSave.getGoogleDistance(localcontext);
                SessionSave.setGoogleDistance(distance, localcontext);
                System.out.println("googledistanceee "+"8_"+pick.latitude+"__"+pick.longitude+"_____"+drop.latitude+"__"+drop.longitude);
                SessionSave.saveGoogleWaypoints(pick , drop, "haversine", distance, "error"+result, localcontext);
                //LocalBroadcastManager.getInstance(localcontext).sendBroadcast(bintent);

                String savingTripDetail = "";
                savingTripDetail += SessionSave.getSession(SessionSave.getSession("trip_id", localcontext) + "data", localcontext) + "\n\n\n<br><br>" + "Distance**266#&nbsp;" + SessionSave.getDistance(localcontext) + "&nbsp;&nbsp;Trip&nbsp;" + SessionSave.getSession("trip_id", localcontext) + "&nbsp;&nbsp;Speed&nbsp;"+ "&nbsp;&nbsp;Time&nbsp;" + DateFormat.getTimeInstance().format(new Date()) +
                        "&nbsp;&nbsp;old&nbsp;&nbsp;" + pick.latitude + "&nbsp;" + pick.longitude + "&nbsp;&nbsp;New&nbsp;&nbsp;" + drop.latitude
                        + "&nbsp;" + drop.longitude
                        + "&nbsp;&nbsp;Read way&nbsp;&nbsp;" + SessionSave.ReadGoogleWaypoints(localcontext);

                SessionSave.saveSession(SessionSave.getSession("trip_id", localcontext) + "data", savingTripDetail, localcontext);

                Localdistanceinterface.haversineResult(true);
            }


        }
    }*//*

}
*/
