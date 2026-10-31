package com.teds.warungku.pos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.teds.warungku.pos.presentation.navigation.AppNavigation
import com.teds.warungku.pos.ui.theme.WarungKuPOSTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            WarungKuPOSTheme {
                AppNavigation()
            }
        }
    }
}
