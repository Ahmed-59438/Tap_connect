package com.tapconnect.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen() {
    var fullName     by remember { mutableStateOf("Alex Dev") }
    var role         by remember { mutableStateOf("Product Designer") }
    var organization by remember { mutableStateOf("TechCorp") }
    var bio          by remember { mutableStateOf("Building the future of mobile networking.") }
    var interests    by remember { mutableStateOf("AI, Design, Startups, Coffee") }
    var nfcEnabled   by remember { mutableStateOf(true) }
    var bleEnabled   by remember { mutableStateOf(true) }
    var linkedin     by remember { mutableStateOf("linkedin.com/in/alexrivera") }
    var instagram    by remember { mutableStateOf("@alex.design") }
    var whatsapp     by remember { mutableStateOf("+1 555 0192") }
    var twitter      by remember { mutableStateOf("@alexrivera") }
    var website      by remember { mutableStateOf("alexrivera.io") }

    Column(modifier = Modifier.fillMaxSize().background(AppBg)) {

        // ── Top bar ──────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxWidth().background(CardBg).padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text("My Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextH)
        }

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Avatar ───────────────────────────────────────────
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(88.dp).clip(CircleShape).background(Color(0xFF1C1C2E)), contentAlignment = Alignment.Center) {
                    Text(fullName.take(1), fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Spacer(Modifier.height(8.dp))
                Text("Change Photo", fontSize = 14.sp, color = AccentIndigo, fontWeight = FontWeight.Medium)
            }

            // ── Basic Info ───────────────────────────────────────
            PCard {
                PField("Full Name", fullName) { fullName = it }
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(Modifier.weight(1f)) { PField("Role", role) { role = it } }
                    Column(Modifier.weight(1f)) { PField("Organization", organization) { organization = it } }
                }
                Spacer(Modifier.height(12.dp))
                Text("Bio", fontSize = 12.sp, color = TextSub, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = bio, onValueChange = { bio = it },
                    modifier = Modifier.fillMaxWidth().height(88.dp),
                    shape = RoundedCornerShape(10.dp), maxLines = 3,
                    colors = fieldColors()
                )
                Spacer(Modifier.height(12.dp))
                PField("Interests (comma separated)", interests) { interests = it }
            }

            // ── Social Links ─────────────────────────────────────
            PCard {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("↔", fontSize = 16.sp, color = AccentIndigo)
                    Text("Social Links", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextH)
                }
                Spacer(Modifier.height(16.dp))
                SField("LINKEDIN",    "↔", linkedin,   { linkedin = it })
                Spacer(Modifier.height(12.dp))
                SField("INSTAGRAM",   "📸", instagram,  { instagram = it })
                Spacer(Modifier.height(12.dp))
                SField("WHATSAPP",    "📞", whatsapp,   { whatsapp = it }, KeyboardType.Phone)
                Spacer(Modifier.height(12.dp))
                SField("TWITTER / X", "⚡", twitter,    { twitter = it })
                Spacer(Modifier.height(12.dp))
                SField("WEBSITE",     "🌐", website,    { website = it },  KeyboardType.Uri)
            }

            // ── Discovery toggles ────────────────────────────────
            PCard {
                TRow("Discoverable via NFC", "Tap-to-connect with nearby users", nfcEnabled) { nfcEnabled = it }
                HorizontalDivider(color = Divider, modifier = Modifier.padding(vertical = 8.dp))
                TRow("Discoverable via BLE", "Background radar scanning", bleEnabled)         { bleEnabled = it }
            }

            // ── Save ─────────────────────────────────────────────
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentIndigo)
            ) {
                Text("Save Changes", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
            }

            Spacer(Modifier.height(90.dp))
        }
    }
}

// ── Helpers ──────────────────────────────────────────────────────

@Composable
fun PCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().shadow(1.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)).background(CardBg).padding(16.dp),
        content = content
    )
}

@Composable
fun PField(label: String, value: String, kbType: KeyboardType = KeyboardType.Text, onChange: (String) -> Unit) {
    Text(label, fontSize = 12.sp, color = TextSub, fontWeight = FontWeight.Medium)
    Spacer(Modifier.height(4.dp))
    OutlinedTextField(
        value = value, onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = kbType, imeAction = ImeAction.Next),
        colors = fieldColors()
    )
}

@Composable
fun SField(label: String, icon: String, value: String, onChange: (String) -> Unit, kbType: KeyboardType = KeyboardType.Text) {
    Text(label, fontSize = 11.sp, color = TextSub, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
    Spacer(Modifier.height(4.dp))
    OutlinedTextField(
        value = value, onValueChange = onChange,
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), singleLine = true,
        leadingIcon = { Text(icon, fontSize = 15.sp, modifier = Modifier.padding(start = 4.dp)) },
        keyboardOptions = KeyboardOptions(keyboardType = kbType, imeAction = ImeAction.Next),
        colors = fieldColors()
    )
}

@Composable
fun TRow(label: String, sub: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        Column(Modifier.weight(1f)) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextH)
            Text(sub, fontSize = 12.sp, color = TextSub)
        }
        Switch(checked = checked, onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = AccentIndigo))
    }
}

@Composable
fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = AccentIndigo, unfocusedBorderColor = Divider,
    focusedTextColor = TextH, unfocusedTextColor = TextH,
    cursorColor = AccentIndigo,
    unfocusedContainerColor = Color(0xFFFAFAFA), focusedContainerColor = Color.White
)
