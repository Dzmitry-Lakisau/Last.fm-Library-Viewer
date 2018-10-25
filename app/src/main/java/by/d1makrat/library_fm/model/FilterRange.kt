package by.d1makrat.library_fm.model

import android.os.Parcel
import android.os.Parcelable

class FilterRange(var startOfPeriod: Long? = null, var endOfPeriod: Long? = null) : Parcelable {

    constructor(input: Parcel): this(startOfPeriod = input.readLong(), endOfPeriod = input.readLong())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(startOfPeriod ?: DATE_LONG_DEFAULT_VALUE)
        dest.writeLong(endOfPeriod ?: DATE_LONG_DEFAULT_VALUE)
    }

    companion object CREATOR : Parcelable.Creator<FilterRange> {

        const val DATE_LONG_DEFAULT_VALUE = -1L

        override fun createFromParcel(parcel: Parcel): FilterRange {
            var startOfPeriod: Long? = parcel.readLong()
            startOfPeriod = if (startOfPeriod == DATE_LONG_DEFAULT_VALUE) null else startOfPeriod
            var endOfPeriod: Long? = parcel.readLong()
            endOfPeriod = if (endOfPeriod == DATE_LONG_DEFAULT_VALUE) null else endOfPeriod

            return FilterRange(startOfPeriod, endOfPeriod)
        }
        override fun newArray(size: Int) = arrayOfNulls<FilterRange>(size)
    }
}
