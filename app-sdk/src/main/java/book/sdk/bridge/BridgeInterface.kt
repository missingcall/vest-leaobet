package book.sdk.bridge

interface BridgeInterface {
    fun copyText(text: String?)
    fun trackAdjustEvent(eventToken: String?, jsonData: String?)
    fun getDeviceID(): String?
    fun getChannel(): String?
    fun getBrand(): String?
    fun getAdjustDeviceID(): String?
    fun getGoogleADID(): String?
    fun setCocosData(key: String?, value: String?)
    fun getCocosData(key: String?): String?
    fun getCocosAllData(): String?
    fun getBridgeVersion(): Int
    fun getTDTargetCountry(): String?
    fun openUrlByBrowser(url: String?)
    fun openUrlByWebView(url: String?)
    fun onWebViewLoadChanged(json: String?)
}