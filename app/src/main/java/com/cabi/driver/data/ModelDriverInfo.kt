package com.cabi.driver.data

data class ModelDriverInfo(val driverId: String = "",
                           val driverTripId: String = "",
                           val driverLastlocation: String = "",
                           val driverShiftStatus: String = "",
                           val travelStatus: String = "",
                           val serviceStatus: Boolean,
                           val currentLocation: String = "")

/*{
    override fun toString(): String {
        val Jj = ""
        return ""
//        return "{driverInfo:{id :$driverId,driverTripId :$driverTripId,driverLastlocation :$driverLastlocation,driverLocationAccuracy :$driverLocationAccuracy,driverShiftStatus :$driverShiftStatus}}"
    }
}*/