package com.quasar.app.auth.ui

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.quasar.app.QuasarScreen
import com.quasar.app.R
import kotlinx.coroutines.launch

@Composable
fun LandingScreen(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val userIsLoggedIn = FirebaseAuth.getInstance().currentUser != null
    if (userIsLoggedIn) {
        navController.navigate(QuasarScreen.MapScreen.name)
    }

    val signInLauncher =
        rememberLauncherForActivityResult(contract = FirebaseAuthUIActivityResultContract()) { result ->
            onSignInResult(
                result, navController
            )
        }
    val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
    val intent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()

    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(stringResource(R.string.welcome_message))
            Button(onClick = {
                scope.launch {
                    signInLauncher.launch(intent)
                }

            }) {
                Text(text = stringResource(R.string.login_button))

            }
        }
    }
}

private fun onSignInResult(
    result: FirebaseAuthUIAuthenticationResult, navController: NavHostController
) {
    val response = result.idpResponse
    if (result.resultCode == Activity.RESULT_OK) {
        val email = response?.email
        val providerType = response?.providerType

//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
//            param(FirebaseAnalytics.Param.ITEM_NAME, email ?: "Unknown email")
//            param(FirebaseAnalytics.Param.METHOD, providerType ?: "Unknown provider")
//        }
        navController.navigate(QuasarScreen.MapScreen.name)
    } else {
        // Handle sign-in failure
        throw Exception("Error logging in")
    }
}
