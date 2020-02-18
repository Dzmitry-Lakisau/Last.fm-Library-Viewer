package by.d1makrat.library_fm

import android.util.Base64
import java.security.MessageDigest
import java.util.*

fun ByteArray.toBase64String(): String = Base64.encodeToString(this, Base64.NO_WRAP)

fun String.toBytes(): ByteArray = Base64.decode(this, Base64.NO_WRAP)

fun ByteArray.md5(): ByteArray = MessageDigest.getInstance("MD5").digest(this)

fun String.md5(): ByteArray = this.toByteArray().md5()

fun ByteArray.toHexString(): String = this.joinToString("") { String.format("%02X", it) }.toLowerCase(Locale.getDefault())