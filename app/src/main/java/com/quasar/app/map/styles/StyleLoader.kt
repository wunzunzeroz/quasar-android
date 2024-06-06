package com.quasar.app.map.styles

import android.content.Context
import com.mapbox.maps.Style

class StyleLoader {
    companion object {
        fun getStyle(style: MapStyle, context: Context): String {
            return when (style) {
                MapStyle.Outdoors -> Style.OUTDOORS
                MapStyle.Satellite -> Style.SATELLITE_STREETS
                MapStyle.Topographic -> loadStyle("topographicStyle.json", context)
                MapStyle.Nautical -> loadStyle("nauticalStyle.json", context)
                MapStyle.Aeronautical -> loadStyle("aeroStyle.json", context)
            }
        }

        private fun loadStyle(filename: String, context: Context): String {
            val json = loadStyleJson(filename, context)
            return replaceApiKey(json)
        }

        private fun replaceApiKey(json: String): String {
            val linzkey = "1334e918fd854116b092b245f2a883ee"

            return json.replace("[api_token]", linzkey)
        }

        private fun loadStyleJson(filename: String, context: Context): String {
            return context.assets.open(filename).bufferedReader().use { it.readText() }
        }
    }
}