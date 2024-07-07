package com.quasar.app.map.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.quasar.app.MainActivity
import com.quasar.app.R
import com.quasar.app.domain.services.UserLocationService
import com.quasar.app.map.models.Position
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LocationForegroundService : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val userLocationService: Lazy<UserLocationService> = inject<UserLocationService>()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    updateLocation(location, userLocationService.value)
                }
            }
        }

        startLocationUpdates()
        startForeground(
            1, createNotification()
        ) // TODO - Error handling, Work out why notification doesn't show
    }

    private fun startLocationUpdates() {
        val locationRequest =
            LocationRequest.Builder(1000).setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateDistanceMeters(30f).build()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d("LocationForegroundService", "Permission not granted")
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createNotification(): Notification {
        val channelId = "location_channel"
        val channelName = "Location Updates"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, channelId).setContentTitle("Location Service")
            .setContentText("Tracking location in the background")
            .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent).build()
    }

    private fun updateLocation(location: Location, lService: UserLocationService) {
        Log.d(
            "LocationForegroundService",
            "Location updated in foreground. Lat/Lng: ${location.latitude}, ${location.longitude}"
        )

        serviceScope.launch {
            lService.broadCastUserLocation(Position(location.latitude, location.longitude))
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}