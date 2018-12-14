/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cabi.driver.utils;

import android.content.Context;
import android.location.Location;

/**
 * Defines app-wide constants and utilities
 */
public final class LocationUtils {

	public static final String APPTAG = "LocationSample";

	public static final String SHARED_PREFERENCES = "com.example.android.location.SHARED_PREFERENCES";

	public static final String KEY_UPDATES_REQUESTED = "com.example.android.location.KEY_UPDATES_REQUESTED";
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
	public static final int FAST_CEILING_IN_SECONDS = 1;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;
	public static final String EMPTY_STRING = new String();

	/**
	 * Get the latitude and longitude from the Location object returned by
	 * Location Services.
	 * 
	 * @param currentLocation
	 *            A Location object containing the current location
	 * @return The latitude and longitude of the current location, or null if no
	 *         location is available.
	 */
	public static String getLatLng(Context context, Location currentLocation) {
		// If the location is valid
		if (currentLocation != null) {
			return EMPTY_STRING;
			// Return the latitude and longitude as strings
//			return context.getString("",
//					currentLocation.getLatitude(),
//					currentLocation.getLongitude());
		} else {

			// Otherwise, return the empty string
			return EMPTY_STRING;
		}

	}
}
