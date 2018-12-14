package com.cabi.driver

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import com.cabi.driver.utils.Colorchange
import com.cabi.driver.utils.FontHelper
import kotlinx.android.synthetic.main.canceltrip_lay.*

/**
 * This is cancel the trip
 */
class ShowAlertAct : MainActivity() {


    var messages: String = ""
    /**
     * setting the layout
     */
    override fun setLayout(): Int {
        // TODO Auto-generated method stub
        return R.layout.canceltrip_lay
    }

    /**
     * Initializing the component variables
     */
    override fun Initialize() {
        // TODO Auto-generated method stub

        var move_to_playstore = false;
        val bun = intent.extras
        if (bun != null) {
            // nonactiityobj.stopServicefromNonActivity(CanceltripAct.this);
            messages = bun.getString("message")
            move_to_playstore = bun.getBoolean("move_to_playstore")
        }

        FontHelper.applyFont(this@ShowAlertAct, findViewById<View>(R.id.canceltrip))

        Colorchange.ChangeColor((this@ShowAlertAct
                .findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup, this@ShowAlertAct)

        message.setText(messages)
        button1.setText(resources.getString(R.string.ok))
        button1.setOnClickListener {
            if (move_to_playstore) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName()))
                startActivity(intent)
            } else
                finish()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
    }


}