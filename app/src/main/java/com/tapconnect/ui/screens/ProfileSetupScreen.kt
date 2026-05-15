package com.tapconnect.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tapconnect.domain.model.UserProfile
import com.tapconnect.ui.components.PrimaryButton
import com.tapconnect.ui.components.TapConnectCard
import com.tapconnect.viewmodel.AppViewModelProvider
import com.tapconnect.viewmodel.ProfileViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    onSetupComplete: () -> Unit,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    // Local state for the form
    var name by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var organization by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var interests by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Setup Your Profile",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        TapConnectCard {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = role,
                onValueChange = { role = it },
                label = { Text("Role / Title (e.g., Software Engineer)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = organization,
                onValueChange = { organization = it },
                label = { Text("Organization / Company") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TapConnectCard {
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Short Bio") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 3,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = interests,
                onValueChange = { interests = it },
                label = { Text("Interests (comma separated)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "Save & Continue",
            enabled = name.isNotBlank() && role.isNotBlank(),
            onClick = {
                val newProfile = UserProfile(
                    userId = UUID.randomUUID().toString(), // Mock ID for now
                    name = name,
                    role = role,
                    organization = organization,
                    bio = bio,
                    interests = interests.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                    socialLinks = emptyMap(),
                    sharingPreferences = emptyMap()
                )
                viewModel.saveProfile(newProfile)
                onSetupComplete()
            }
        )
    }
}
