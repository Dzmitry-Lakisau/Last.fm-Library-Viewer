package by.d1makrat.library_fm.utils

import by.d1makrat.library_fm.APIException
import com.crashlytics.android.Crashlytics
import com.google.gson.JsonSyntaxException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ExceptionHandler {
    fun sendExceptionAndGetReadableMessage(exception: Throwable): String {
        return if (exception.cause is APIException) {
            Crashlytics.logException(exception.cause)
            (exception.cause as APIException).message!!
        } else if (exception is HttpException && exception.code() == 403) {
            Crashlytics.logException(exception)
            "You do not have permission to access"
        } else {
            Crashlytics.logException(exception)
            when (exception) {
                is JsonSyntaxException -> "Bad formatted data in response"
                is SocketTimeoutException -> "Bad connection"
                is UnknownHostException -> "Server is down or unreachable"
                is ConnectException -> "Error at the server side"
                is APIException -> exception.message!!
                else -> "Error occurred"
            }
        }
    }
}
