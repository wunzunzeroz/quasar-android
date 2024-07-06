package com.quasar.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.ktx.Firebase
import com.quasar.app.map.services.LocationForegroundService
import com.quasar.app.ui.theme.QUASARTheme


class MainActivity : ComponentActivity() {
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAnalytics = Firebase.analytics

        val intent = Intent(this, LocationForegroundService::class.java)
        startForegroundService(intent)


//        val signInLauncher = registerForActivityResult(
//            FirebaseAuthUIActivityResultContract()
//        ) { result ->
//            onSignInResult(result)
//        }

        enableEdgeToEdge()
        setContent {
            QUASARTheme(dynamicColor = true) {
                QuasarApp()
            }
        }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == RESULT_OK) {
            val email = response?.email
            val providerType = response?.providerType

            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.ITEM_NAME, email ?: "Unknown email")
                param(FirebaseAnalytics.Param.METHOD, providerType ?: "Unknown provider")
            }
            // Successfully signed in, navigate to MapActivity

        } else {
            // Handle sign-in failure
            throw Exception("Error logging in")
        }
    }
}