package com.example.todolistmobileapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─────────────────────────────────────────────
// HOME SCREEN
// ─────────────────────────────────────────────

@Composable
fun HomeScreen(
    selectedNavIdx: Int,
    onNavSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(GradientStart, GradientEnd)))
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = "Icon Kalender",
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.BottomStart)
                .offset(x = (150).dp, y = (-600).dp)
        )

        // Konten Tengah
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TODO",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = TextDark,
                textAlign = TextAlign.Center,
                letterSpacing = 4.sp
            )
            Text(
                text = "APP",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                textAlign = TextAlign.Center,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Klik Get Start
            Button(
                onClick = { onNavSelected(1) },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = "Get Start",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        // Bottom Navigation — highlight index 0 / Home
        BottomNavBar(
            selectedIndex = selectedNavIdx,
            onNavSelected = onNavSelected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}