package com.quasar.app.map.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Duration
import java.time.Instant

class Utils {
    companion object {
        fun RoundNumberToDp(number: Double, dp: Int): Double {
            return BigDecimal(number).setScale(dp, RoundingMode.HALF_EVEN).toDouble()
        }

        @OptIn(ExperimentalStdlibApi::class)
        fun convertColorToHexString(color: Color): String {
            val hex = color.toArgb().toHexString().substring(2)

            return "#$hex"
        }

        fun getTimeSinceNow(timestamp: Instant): String {
            val now = Instant.now()

            val duration = Duration.between(timestamp, now)

            return if (duration.toMinutes() < 60)
                "${duration.toMinutes()} minutes"
            else
                "${duration.toHours()} hours"
        }
    }
}
