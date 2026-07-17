package com.chujunjie.scamwisecampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.chujunjie.scamwisecampus.ui.navigation.AppNavigation
import com.chujunjie.scamwisecampus.ui.theme.ScamWiseCampusTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScamWiseCampusTheme {
                Surface {
                    AppNavigation(
                        modifier = Modifier
                    )
                }
            }
        }
    }
}