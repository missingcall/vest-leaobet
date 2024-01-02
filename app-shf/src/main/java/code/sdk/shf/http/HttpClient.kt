package code.sdk.shf.http

import android.text.TextUtils
import code.sdk.core.util.ConfigPreference
import code.sdk.shf.http.interceptor.ResponseInterceptor
import code.util.LogUtil.isDebug
import code.util.MySSLSocketClient.getHostnameVerifier
import code.util.MySSLSocketClient.getSSLSocketFactory
import code.util.MySSLSocketClient.getX509TrustManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrl

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class HttpClient private constructor() {
    val api: Api
        get() {
            if (mApi == null) {
                mApi = retrofit.create(
                    Api::class.java
                )
            }
            return mApi!!
        }
    private val retrofit: Retrofit
        get() {
            var url = ConfigPreference.readSHFBaseHost()
            if (TextUtils.isEmpty(url)) {
                url = "https://getRetrofit.com"
            }
            return Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
        }
    private val okHttpClient: OkHttpClient
        get() {
            val interceptor = HttpLoggingInterceptor()
            if (isDebug()) { //这行必须加 不然默认不打印
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            return OkHttpClient.Builder()
                .addInterceptor(ResponseInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_READ_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .sslSocketFactory(getSSLSocketFactory(), getX509TrustManager())
                .hostnameVerifier(getHostnameVerifier())
                .build()
        }

    fun buildUrl(host: String, api: String, query: Map<String, String>?): String {

        val builder = host.toHttpUrl().newBuilder()
        builder.addEncodedPathSegment(api)
        if (!query.isNullOrEmpty()) {
            for ((key, value) in query) {
                builder.addEncodedQueryParameter(key, value)
            }
        }
        return builder.build().toString()
    }

    fun <T : Any> ioSchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream: Observable<T> ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    companion object {
        private const val DEFAULT_READ_TIMEOUT = 8000 //读取超时，单位毫秒
        private const val DEFAULT_WRITE_TIMEOUT = 8000 //写入超时，单位毫秒
        private const val DEFAULT_CONNECT_TIMEOUT = 8000 //连接超时，单位毫秒
        private var mApi: Api? = null

        val mInstance by lazy { HttpClient() }

    }
}