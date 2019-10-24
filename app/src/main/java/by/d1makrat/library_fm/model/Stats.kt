package by.d1makrat.library_fm.model

import com.google.gson.annotations.SerializedName

data class Stats(
        @SerializedName("userplaycount")
        val scrobblesCount: Int
)
