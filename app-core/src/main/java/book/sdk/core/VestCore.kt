package book.sdk.core

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import book.sdk.core.event.SDKEvent
import book.sdk.core.manager.ActivityManager
import book.sdk.core.manager.AdjustManager
import book.sdk.core.manager.ConfigurationManager
import book.sdk.core.util.GoogleAdIdInitializer
import book.sdk.core.util.PreferenceUtil
import book.sdk.core.util.Tester
import book.util.AppGlobal
import book.util.LogUtil
import org.greenrobot.eventbus.EventBus

object VestCore {
    private val TAG = VestCore::class.java.simpleName
    private val WEBVIEW_ACTIVITY_CLASS_NAME = "book.sdk.ui.WebActivity"
    private var isTestIntentHandled = false
    var WEBVIEW_TYPE_INNER: String = "1"
    var WEBVIEW_TYPE_SYSTEM: String = "2"

    fun init(context: Context, configAssets: String?, loggable: Boolean?) {
        if (loggable != null) {
            Tester.setLoggable(loggable)
            LogUtil.setDebug(loggable)
        }
        LogUtil.setDebug(Tester.isLoggable())
        ConfigurationManager.init(context, configAssets)
        registerActivityLifecycleCallbacks()
        GoogleAdIdInitializer.init()
        initThirdSDK()
    }

    fun isTestIntentHandled(): Boolean {
        return isTestIntentHandled
    }

    private fun registerActivityLifecycleCallbacks() {
        AppGlobal.application?.registerActivityLifecycleCallbacks(object :
            SimpleLifecycleCallbacks() {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityManager.mInstance.push(activity)
                interceptLauncherActivity(activity)
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.mInstance.remove(activity)
                interceptDestroyedActivity(activity)
            }
        })
    }

    private fun interceptLauncherActivity(activity: Activity) {
        val intent = activity.intent ?: return
        val categories = intent.categories
        LogUtil.d(TAG, "[Vest-Core] onActivityCreated: intent=%s", intent)
        if (categories != null && categories.contains(Intent.CATEGORY_LAUNCHER)) {
            LogUtil.d(
                TAG, "[Vest-Core] onActivityCreated: %s is a launcher activity",
                if (intent.component != null) intent.component!!.flattenToString() else ""
            )
            isTestIntentHandled = Tester.handleIntent(activity)
        }
    }

    private fun interceptDestroyedActivity(activity: Activity) {
        val intent = activity.intent ?: return
        LogUtil.d(TAG, "[Vest-Core] onActivityDestroyed: intent=%s", intent)
        if (ActivityManager.mInstance.isActivityEmpty()) {
            isTestIntentHandled = false
            LogUtil.d(TAG, "[Vest-Core] onActivityDestroyed: activity stack is empty, reset")
        }
    }

    fun initThirdSDK() {
        AdjustManager.init(AppGlobal.application!!)
    }

    fun updateThirdSDK() {
        AdjustManager.initParams()
    }

    fun getTargetCountry(): String? {
        val targetCountry = PreferenceUtil.readTargetCountry()
        LogUtil.d(TAG, "[Vest-Core] read target country: %s", targetCountry)
        return targetCountry
    }

    fun onCreate() {
        EventBus.getDefault().post(SDKEvent("onCreate"))
    }

    fun onDestroy() {
        EventBus.getDefault().post(SDKEvent("onDestroy"))
    }

    fun onPause() {
        EventBus.getDefault().post(SDKEvent("onPause"))
    }

    fun onResume() {
        EventBus.getDefault().post(SDKEvent("onResume"))
    }


    fun toWebViewActivity(
        context: Context?,
        url: String?,
        flag: String? = WEBVIEW_TYPE_INNER
    ): Boolean {
        try {
            if (!URLUtil.isValidUrl(url)) {
                LogUtil.e(TAG, "[Vest-Core] Launch aborted for invalid url: $url")
                return false
            }
            if (flag == WEBVIEW_TYPE_SYSTEM) {
                AdjustManager.trackEventAccess(null)
                LogUtil.d(TAG, "[Vest-Core] Launch B-side with system WebView")
                val intent = Intent().apply {
                    action = Intent.ACTION_VIEW
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    data = Uri.parse(url)
                }
                context?.startActivity(intent)
            } else {
                LogUtil.d(TAG, "[Vest-Core] Launch B-side with inner WebView")
                val intent = Intent().apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    setClassName(context!!, WEBVIEW_ACTIVITY_CLASS_NAME)
                    putExtra("key_path_url_value", addRandomTimestamp(url))
                    putExtra("key_is_game_value", true)
                }
                context?.startActivity(intent)
            }
            return true
        } catch (e: ActivityNotFoundException) {
            LogUtil.e(TAG, e, "[Vest-Core] Activity not found, please import 'vest-sdk' library")
        } catch (e: Exception) {
            LogUtil.e(TAG, e, "[Vest-Core] Activity launched error")
        }
        return false
    }

    private fun addRandomTimestamp(url: String?): String? {
        val uri = try {
            Uri.parse(url)
        } catch (e: Exception) {
            LogUtil.e(TAG, "url parse error: %s", url)
            return url
        }
        val ts = System.currentTimeMillis().toString()
        val queryParameterNames = uri.queryParameterNames
        return if (queryParameterNames.size > 0) {
            "$url&t=$ts"
        } else {
            "$url?t=$ts"
        }
    }
}