package com.quasar.app.auth.ui

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

    Box(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                "QUASAR",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                "Navigate. Locate. Communicate.",
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(64.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            signInLauncher.launch(intent)
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.login_button))
                }
                OutlinedButton(
                    onClick = {
                        scope.launch {
                            signInLauncher.launch(intent)
                        }
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign up")

                }
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

@Composable
@Preview(showSystemUi = true)
fun LoginScreenPreview() {
    LandingScreen(navController = NavHostController(LocalContext.current))
}