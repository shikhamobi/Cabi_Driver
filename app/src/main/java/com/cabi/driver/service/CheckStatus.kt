package com.cabi.driver.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.cabi.driver.MainActivity
import com.cabi.driver.ShowAlertAct
import com.cabi.driver.SplashAct
import com.cabi.driver.UserLoginAct
import com.cabi.driver.data.CommonData
import com.cabi.driver.utils.CToast
import com.cabi.driver.utils.SessionSave
import org.json.JSONObject

//Nodejs Log Code
//408				=>	Header not contains domains					(Need Alert)
//408				=>  DB Not connected,Site Config not retrived 	(Need Alert)
//408				=>  Timezone issue								(Need Alert)
//408				=>  Invalid Auth 								(Need Alert)
//408				=>  MongoDB down.
//408				=>  TryCatch Error								(Need Alert)
//
//Return data
//811				=>  Driver Get Current Information
//
//Message without action service stop only
//
//409				=>  Force to Update Build
//412				=>  Request to uninstall current build.
//
//Action with message move to login screen
//
//
//410				=>  Driver App Destory like as new build
//411				=>  Driver Logout
//
//Only action
//601 =>  Web Domain Url Change Request
//602 =>  Node Url Change Request
//603 =>  Token Expired

class CheckStatus(val json: JSONObject, val context: Context) {

    fun isNormal(): Boolean {
        var normal = true

        if (json.has("token"))
            SessionSave.saveSession(CommonData.NODE_TOKEN, json.getString("token"), context)

        var statusCode = json.getInt("status");
        var message = json.getString("message");


        when (statusCode) {
            408 -> {
                normal = false
                Handler(Looper.getMainLooper()).post {
                    CToast.ShowToast(context, message)

                }

            }
            601 -> {
                normal = false
//                SessionSave.saveSession("base_url", message, context)
                CheckUrl().update(context, json.getString("domain"), message, "base_url")
            }
            602 -> {
                normal = false
//                SessionSave.saveSession(CommonData.NODE_URL, message, context)
                CheckUrl().update(context, json.getString("domain"), message, CommonData.NODE_URL)

            }
            603 -> {
                normal = false
                NodeAuth().getAuth(context)
            }

            409 -> {
                normal = false
                val cancelIntent = Intent()
                val bun = Bundle()
                bun.putString("message", message)
                bun.putBoolean("move_to_playstore", true)
                cancelIntent.putExtras(bun)
                cancelIntent.action = Intent.ACTION_MAIN
                cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
                val cn = ComponentName(context, ShowAlertAct::class.java)
                cancelIntent.component = cn
                context.stopService(Intent(context, LocationUpdate::class.java))
                context.startActivity(cancelIntent)
            }

            410 -> {
                normal = false
                SessionSave.clearAllSession(context)
                forceLogout("")
                val cancelIntent = Intent()
                val bun = Bundle()
                bun.putString("message", message)
                cancelIntent.putExtras(bun)
                cancelIntent.action = Intent.ACTION_MAIN
                cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
                val cn = ComponentName(context, SplashAct::class.java)
                cancelIntent.component = cn
                context.startActivity(cancelIntent)
            }

            412 -> {
                normal = false
                val cancelIntent = Intent()
                val bun = Bundle()
                bun.putBoolean("move_to_playstore", false)
                bun.putString("message", message)
                cancelIntent.putExtras(bun)
                cancelIntent.action = Intent.ACTION_MAIN
                cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
                val cn = ComponentName(context, ShowAlertAct::class.java)
                cancelIntent.component = cn
                context.stopService(Intent(context, LocationUpdate::class.java))
                context.startActivity(cancelIntent)
            }

            411 -> {
                normal = false
                MainActivity.clearsession(context)
                val cancelIntent = Intent()
                val bun = Bundle()
                bun.putString("message", message)
                bun.putString("alert_message", message)
                cancelIntent.putExtras(bun)
                cancelIntent.action = Intent.ACTION_MAIN
                cancelIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                cancelIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_SINGLE_TOP
                val cn = ComponentName(context, UserLoginAct::class.java)
                cancelIntent.component = cn
                context.startActivity(cancelIntent)
            }

            811 -> {
                normal = false
                SendDriverDeviceInfo().sendInfo(context, "-1")
            }

        }


        return normal
    }


    /**
     * Method to logout user if status -101 and redirect to login page
     * @param message - To intimate user by showing alert message
     */
    private fun forceLogout(message: String) {
//        CToast.ShowToast(context, message)
        ServiceGenerator.API_BASE_URL = ""
        SessionSave.saveSession("base_url", "", context)
        SessionSave.saveSession("Id", "", context)
        SessionSave.clearAllSession(context)
        context.stopService(Intent(context, LocationUpdate::class.java))
        context.stopService(Intent(context, WaitingTimerRun::class.java))
    }
}