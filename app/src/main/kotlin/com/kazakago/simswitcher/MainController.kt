package com.kazakago.simswitcher

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainController(
    modifier: Modifier = Modifier,
) {
    MainScreen(
        name = "Android",
        modifier = modifier,
    )
}
