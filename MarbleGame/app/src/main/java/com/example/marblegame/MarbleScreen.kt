package com.example.marblegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun MarbleScreen(vm: MarbleViewModel) {
    val density = LocalDensity.current
    val ballSizeDp = 40.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val maxWpx = with(density) { maxWidth.toPx() }
        val maxHpx = with(density) { maxHeight.toPx() }
        val ballPx = with(density) { ballSizeDp.toPx() }

        LaunchedEffect(maxWpx, maxHpx) {
            vm.setBounds(maxWpx, maxHpx, ballPx)
        }

        // Marble
        Box(
            modifier = Modifier
                .offset { IntOffset(vm.ui.xPx.roundToInt(), vm.ui.yPx.roundToInt()) }
                .size(ballSizeDp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )

        // HUD with high-contrast text
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val (rx, ry) = vm.positionRounded()
            Text(
                text = "x=$rx  y=$ry",
                color = MaterialTheme.colorScheme.onBackground // light on dark
            )
        }

        // Reset FAB
        ExtendedFloatingActionButton(
            onClick = { vm.recenter() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Reset")
        }
    }
}
