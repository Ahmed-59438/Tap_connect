package com.tapconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DiscoverScreen() {
    val mockUsers = listOf(
        Triple("Sarah Ali", "Android Engineer", "Google"),
        Triple("John Chen", "Product Designer", "Meta"),
        Triple("Ayesha K.", "Startup Founder", "Self"),
        Triple("Marco R.", "ML Engineer", "OpenAI"),
        Triple("Hina B.", "Backend Dev", "Amazon"),
    )

    Column(modifier = Modifier.fillMaxSize().background(AppBg)) {
        Row(
            modifier = Modifier.fillMaxWidth().background(CardBg)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Discover", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextH)
            Box(
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    .background(AppBg).padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("5 nearby", fontSize = 13.sp, color = AccentIndigo, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SectionHeader("People Nearby")
            mockUsers.forEach { (name, role, org) ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .shadow(1.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp)).background(CardBg)
                        .clickable { }.padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(AccentLight),
                            contentAlignment = Alignment.Center) {
                            Text(name.take(1), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AccentIndigo)
                        }
                        Column {
                            Text(name, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextH)
                            Text(role, fontSize = 13.sp, color = TextSub)
                            Text(org, fontSize = 12.sp, color = AccentIndigo.copy(alpha = 0.7f))
                        }
                    }
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        .background(AccentIndigo).padding(horizontal = 12.dp, vertical = 6.dp)) {
                        Text("Tap", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}
