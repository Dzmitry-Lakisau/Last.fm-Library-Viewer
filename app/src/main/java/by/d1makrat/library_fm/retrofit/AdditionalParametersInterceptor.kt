package by.d1makrat.library_fm.retrofit

import by.d1makrat.library_fm.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils

class AdditionalParametersInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        var url = request.url()

        /*
        Adding call signature:
        api signature = md5("api_keyxxxxxxxxmethodauth.getMobileSession
        passwordxxxxxxxusernamexxxxxxxxmysecret")
        Query parameters must be in alphabetical order
        */
        if (request.method().equals("POST", true)){
            val string = StringBuilder()

            val names = url.queryParameterNames().sorted()

            for (name in names){
                string.append(name).append(url.queryParameterValues(name)[0])
            }

            string.append(BuildConfig.SECRET)

            url = url.newBuilder().addQueryParameter("api_sig", String(Hex.encodeHex(DigestUtils.md5(string.toString())))).build()
        }

        url = url.newBuilder().addQueryParameter("format", "json").build()

        return chain.proceed(request.newBuilder().url(url).build())
    }
}
