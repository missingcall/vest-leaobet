package com.example.vest.sdk.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import code.sdk.core.VestInspectCallback
import code.sdk.core.VestSDK
import code.sdk.shf.VestSHF
import java.util.concurrent.TimeUnit

class AppTestSDKActivity : Activity() {

    private val TAG = AppTestSDKActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_custom_splash)
        /**
         * setup the date of apk build
         * don't need to invoke this method if using vest-plugin, vest-plugin will setup release time automatically
         * if not, you need to invoke this method to setup release time
         * this method has the first priority when using both ways.
         * time format: yyyy-MM-dd HH:mm:ss
         */
        VestSHF.getInstance().setReleaseTime("2023-11-29 10:23:20")
        /**
         * setup duration of silent period for requesting A/B switching starting from the date of apk build
         */
        VestSHF.getInstance().setInspectDelayTime(5, TimeUnit.DAYS)
        /**
         * set true to check the remote and local url, this could make effect on A/B switching
         */
        VestSHF.getInstance().setCheckUrl(true)
        /**
         * trying to request A/B switching, depends on setReleaseTime & setInspectDelayTime & backend config
         */
        VestSHF.getInstance().inspect(this, object : VestInspectCallback {
            /**
             * showing A side
             */
            override fun onShowVestGame(reason: Int) {
                Log.d(TAG, "show vest game")
                val intent = Intent(baseContext, VestGameActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }

            /**
             * showing B side
             */
            override fun onShowOfficialGame(url: String) {
                Log.d(TAG, "show official game: $url")
                VestSDK.gotoGameActivity(baseContext, url)
                finish()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        VestSDK.onPause()
    }

    override fun onResume() {
        super.onResume()
        VestSDK.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        VestSDK.onDestroy()
    }

}