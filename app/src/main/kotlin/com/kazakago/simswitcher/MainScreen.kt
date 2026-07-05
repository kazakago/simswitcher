package com.kazakago.simswitcher

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kazakago.simswitcher.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    name: String,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Main") },
            )
        },
    ) { paddingValues ->
        Text(
            text = "Hello $name!",
            modifier = modifier.padding(paddingValues),
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    AppTheme {
        MainScreen("Android")
    }
}
