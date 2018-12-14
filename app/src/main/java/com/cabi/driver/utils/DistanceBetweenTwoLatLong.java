package com.cabi.driver.utils;

import android.location.Location;
/**
 *Used to calculate distance between two lat and long
 */
public class DistanceBetweenTwoLatLong {

	public static float calculate(double latitude_from, double longtitue_from,
			double latitude_to, double longtitue_to) {
		Location locationA = new Location("point A");
		locationA.setLatitude(latitude_from);
		locationA.setLongitude(longtitue_from);
		Location locationB = new Location("point B");
		locationB.setLatitude(latitude_to);
		locationB.setLongitude(longtitue_to);
		float distance = locationA.distanceTo(locationB);
		return distance;
	}

	public static double calculatekms(double latitude_from,
			double longtitue_from, double latitude_to, double longtitue_to) {

		return calculate(latitude_from, longtitue_from, latitude_to,
				longtitue_to) / 1000;
	}
}
