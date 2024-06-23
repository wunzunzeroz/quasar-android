package com.quasar.app.map.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.quasar.app.QuasarScreen

@Composable
fun BottomNav(navHostController: NavHostController) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
        val selectedItem = remember {
            mutableIntStateOf(0)
        }

        val items = listOf(
            "Map", "Tasks", "Chat", "Channels", "Profile"
        )
        val screens = listOf(
            QuasarScreen.MapScreen,
            QuasarScreen.MapScreen,
            QuasarScreen.MapScreen,
            QuasarScreen.ChannelsScreen,
            QuasarScreen.MapScreen,
        )
        val icons = listOf(
            Icons.Default.Map,
            Icons.Default.List,
            Icons.Default.Chat,
            Icons.Default.Groups,
            Icons.Default.Person
        )
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        icons[index],
                        contentDescription = item,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = { Text(item) },
                selected = selectedItem.intValue == index,
                onClick = {
                    selectedItem.intValue = index
                    navHostController.navigate(screens[index].name)
                },
            )
        }
    }
}