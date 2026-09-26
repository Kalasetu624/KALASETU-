package com.example.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.PastelStepHeaderCard
import com.example.ui.theme.InkDark
import com.example.ui.theme.InkMedium
import com.example.ui.theme.OrderDoneGreen
import com.example.ui.theme.OrderDoneGreenBorder
import com.example.ui.theme.OrderDoneGreenContainer
import com.example.ui.theme.OrderDoneGreenText
import com.example.ui.theme.PastelBlueBorder
import com.example.ui.theme.PastelBlueContainer
import com.example.ui.theme.PastelBlueDark
import com.example.ui.theme.PastelBlueLight
import com.example.ui.theme.PastelBluePrimary
import com.example.ui.theme.PureWhite
import com.example.ui.viewmodel.KalaSetuUiState
import com.example.ui.viewmodel.KalaSetuViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step1ProgressiveLoginScreen(
    state: KalaSetuUiState,
    viewModel: KalaSetuViewModel
) {
    val languages = listOf("English + हिंदी", "हिंदी (Hindi)", "मराठी (Marathi)", "தமிழ் (Tamil)")

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Hero Banner Image
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, PastelBlueBorder),
            colors = CardDefaults.cardColors(containerColor = PureWhite)
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.img_artisan_hero_1790431556527),
                    contentDescription = "Rural Indian Artisans Digital Marketplace",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(155.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PastelBlueLight)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Welcome, Karigar! (नमस्ते कारीगर)",
                        style = MaterialTheme.typography.titleLarge,
                        color = PastelBlueDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Step 1: Simple Phone & OTP Login. Zero documents needed upfront!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMedium
                    )
                }
            }
        }

        // Language Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = BorderStroke(1.dp, PastelBlueBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "1. Choose Your Language (अपनी भाषा चुनें)",
                    style = MaterialTheme.typography.titleMedium,
                    color = InkDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    languages.forEach { lang ->
                        val selected = state.preferredLanguage == lang
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.updatePreferredLanguage(lang) },
                            label = {
                                Text(
                                    text = lang,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PastelBlueContainer,
                                selectedLabelColor = PastelBlueDark
                            )
                        )
                    }
                }
            }
        }

        // Phone + OTP Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = BorderStroke(1.5.dp, PastelBlueBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "2. Mobile Number Login (मोबाइल नंबर लॉगिन)",
                    style = MaterialTheme.typography.titleMedium,
                    color = InkDark
                )

                OutlinedTextField(
                    value = state.artisanName,
                    onValueChange = { viewModel.updateArtisanName(it) },
                    label = { Text("Artisan Name (कारीगर का नाम)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("artisan_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PastelBluePrimary,
                        unfocusedBorderColor = PastelBlueBorder,
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite
                    )
                )

                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.updatePhoneNumber(it) },
                    label = { Text("10-Digit Mobile Number (मोबाइल नंबर)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = "Phone Icon",
                            tint = PastelBluePrimary
                        )
                    },
                    prefix = { Text("+91 ", fontWeight = FontWeight.Bold, color = InkDark) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phone_number_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PastelBluePrimary,
                        unfocusedBorderColor = PastelBlueBorder,
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { viewModel.sendOtp() },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("send_otp_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PastelBlueContainer,
                            contentColor = PastelBlueDark
                        )
                    ) {
                        Text(
                            text = if (state.isOtpSent) "Resend OTP (4829)" else "Get OTP (ओटीपी पाएं)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (state.isOtpSent) {
                    Surface(
                        color = OrderDoneGreenContainer,
                        border = BorderStroke(1.dp, OrderDoneGreenBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "OTP Sent",
                                tint = OrderDoneGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SMS OTP Verified: ${state.demoOtpCode} (Auto-filled for easy access)",
                                color = OrderDoneGreenText,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Button(
                    onClick = { viewModel.verifyOtpAndProceed() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("login_continue_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PastelBluePrimary,
                        contentColor = PureWhite
                    )
                ) {
                    Text(
                        text = "Login & Go to Step 2: Photo Capture",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Proceed to Photo Capture"
                    )
                }
            }
        }
    }
}

@Composable
fun Step2PhotoCaptureScreen(
    state: KalaSetuUiState,
    viewModel: KalaSetuViewModel
) {
    val context = LocalContext.current

    // System Camera Launcher (TakePicturePreview returns a Bitmap directly)
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            viewModel.onBitmapCapturedOrPicked(bitmap, "Camera Photo (कैमरा फोटो)")
        } else {
            // If user cancels emulator camera or no camera hardware, load selected craft sample
            viewModel.selectCraftPreset(context, state.selectedPresetKey)
        }
    }

    // Runtime Permission Launcher for CAMERA
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        viewModel.onPermissionResult("CAMERA", isGranted)
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            viewModel.selectCraftPreset(context, state.selectedPresetKey)
        }
    }

    // Zero-Permission Android Photo Picker Launcher for Gallery Upload
    val pickMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                val bmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                        decoder.isMutableRequired = true
                    }
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                if (bmp != null) {
                    viewModel.onBitmapCapturedOrPicked(bmp, "Gallery Upload (गैलरी फोटो)")
                }
            } catch (_: Exception) {
                viewModel.selectCraftPreset(context, state.selectedPresetKey)
            }
        }
    }

    // Explicit Permission Explanation Dialog before launching Camera or Gallery
    if (state.showPermissionPromptDialog) {
        val isCamera = state.pendingMediaSource == "CAMERA"
        AlertDialog(
            onDismissRequest = { viewModel.dismissPermissionDialog() },
            containerColor = PureWhite,
            icon = {
                Icon(
                    imageVector = if (isCamera) Icons.Default.CameraAlt else Icons.Default.PhotoLibrary,
                    contentDescription = "Permission Icon",
                    tint = PastelBluePrimary,
                    modifier = Modifier.size(36.dp)
                )
            },
            title = {
                Text(
                    text = if (isCamera) {
                        "Allow Camera Access? (कैमरा अनुमति)"
                    } else {
                        "Allow Gallery Photo Access? (गैलरी अनुमति)"
                    },
                    fontWeight = FontWeight.Bold,
                    color = InkDark
                )
            },
            text = {
                Text(
                    text = if (isCamera) {
                        "KalaSetu needs your permission to open the Camera so you can click a clear photo of your handcrafted item."
                    } else {
                        "KalaSetu needs your permission to open your Photo Gallery so you can upload an existing picture of your craft."
                    },
                    color = InkMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissPermissionDialog()
                        if (isCamera) {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        } else {
                            viewModel.onPermissionResult("GALLERY", true)
                            pickMediaLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PastelBluePrimary),
                    modifier = Modifier.testTag("grant_permission_confirm_button")
                ) {
                    Text("Allow Access (अनुमति दें)", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.dismissPermissionDialog() }) {
                    Text("Cancel", color = InkMedium)
                }
            }
        )
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PastelStepHeaderCard(
            stepNumber = 2,
            titleEn = "Capture or Upload Craft Photo",
            titleHi = "उत्पाद की फोटो खींचें या अपलोड करें",
            subtitle = "Take a photo with your Camera or pick from Gallery. AI will clean the background in Step 3."
        )

        // Large Accessible Camera & Gallery Buttons (Asking Permission First)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.requestMediaWithPermissionDialog("CAMERA") }
                    .testTag("take_photo_camera_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PastelBlueLight),
                border = BorderStroke(2.dp, PastelBluePrimary)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PastelBluePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Take Photo with Camera",
                            tint = PureWhite,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Take Photo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PastelBlueDark
                    )
                    Text(
                        text = "कैमरा खोलें",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMedium
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.requestMediaWithPermissionDialog("GALLERY") }
                    .testTag("upload_gallery_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(2.dp, PastelBlueBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PastelBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Upload from Gallery",
                            tint = PastelBlueDark,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Upload Pic",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = InkDark
                    )
                    Text(
                        text = "गैलरी से चुनें",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkMedium
                    )
                }
            }
        }

        // Current Photo Preview Box on Crisp White Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = BorderStroke(1.5.dp, PastelBlueBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Selected Craft Photo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        color = OrderDoneGreenContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Ready for AI",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = OrderDoneGreenText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                val currentBmp = state.capturedBitmap
                if (currentBmp != null) {
                    Image(
                        bitmap = currentBmp.asImageBitmap(),
                        contentDescription = state.photoSourceLabel,
                        modifier = Modifier
                            .size(210.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, PastelBlueBorder, RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    val presetRes = when (state.selectedPresetKey) {
                        "pottery" -> R.drawable.img_craft_pottery_1790431500975
                        "bamboo" -> R.drawable.img_craft_bamboo_1790431524732
                        else -> R.drawable.img_craft_terracotta_1790431512586
                    }
                    Image(
                        painter = painterResource(id = presetRes),
                        contentDescription = state.photoSourceLabel,
                        modifier = Modifier
                            .size(210.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, PastelBlueBorder, RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = state.photoSourceLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = PastelBlueDark
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Or Quick-Select an Artisan Craft Sample:",
                    style = MaterialTheme.typography.labelMedium,
                    color = InkMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        "terracotta" to "Terracotta (₹120)",
                        "pottery" to "Blue Pottery (₹185)",
                        "bamboo" to "Bamboo (₹145)"
                    ).forEach { (key, label) ->
                        val isSelected = state.selectedPresetKey == key
                        OutlinedButton(
                            onClick = { viewModel.selectCraftPreset(context, key) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) PastelBluePrimary else PastelBlueBorder
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) PastelBlueLight else PureWhite
                            )
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = InkDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Step3AiEnhancementScreen(
    state: KalaSetuUiState,
    viewModel: KalaSetuViewModel
) {
    val context = LocalContext.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PastelStepHeaderCard(
            stepNumber = 3,
            titleEn = "AI Studio Background & Lighting",
            titleHi = "एआई सफ़ेद बैकग्राउंड और रोशनी सुधार",
            subtitle = "Automatically removes cluttered background (rembg) and equalizes lighting (OpenCV CLAHE) for e-commerce."
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = BorderStroke(1.5.dp, PastelBlueBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (state.showOriginalPreview) PastelBlueLight else OrderDoneGreenContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (state.showOriginalPreview) {
                                "BEFORE: Raw Village Photo"
                            } else {
                                "AFTER: Pure White Studio (#FFFFFF)"
                            },
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            color = if (state.showOriginalPreview) PastelBlueDark else OrderDoneGreenText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.toggleOriginalPreview(!state.showOriginalPreview) },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, PastelBluePrimary)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Compare,
                            contentDescription = "Compare Before and After",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (state.showOriginalPreview) "Show AI Fixed" else "Compare Raw",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .size(235.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(PureWhite)
                        .border(2.dp, PastelBlueBorder, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (state.isEnhancingImage) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = PastelBluePrimary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Running rembg + Histogram Fix...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = PastelBlueDark
                            )
                        }
                    } else {
                        val displayBitmap = if (state.showOriginalPreview) {
                            state.capturedBitmap
                        } else {
                            state.enhancedBitmap ?: state.capturedBitmap
                        }

                        if (displayBitmap != null) {
                            Image(
                                bitmap = displayBitmap.asImageBitmap(),
                                contentDescription = "AI Enhanced Product Photo",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_craft_terracotta_1790431512586),
                                contentDescription = "Sample Craft",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Enhancement Pipeline Toggles
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(PastelBlueLight)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Background Removal (rembg)",
                                fontWeight = FontWeight.Bold,
                                color = InkDark,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Replaces background with pure #FFFFFF white",
                                color = InkMedium,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = state.removeBackgroundEnabled,
                            onCheckedChange = { viewModel.toggleRemoveBackground(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = PastelBluePrimary)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "OpenCV Histogram Lighting Fix",
                                fontWeight = FontWeight.Bold,
                                color = InkDark,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Auto-corrects shadows & warm craft colors",
                                color = InkMedium,
                                fontSize = 12.sp
                            )
                        }
                        Switch(
                            checked = state.lightingEqualizationEnabled,
                            onCheckedChange = { viewModel.toggleLightingEqualization(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = PastelBluePrimary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (state.capturedBitmap == null) {
                            viewModel.selectCraftPreset(context, state.selectedPresetKey)
                        } else {
                            viewModel.runStudioImageEnhancement()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("reprocess_ai_image_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PastelBlueContainer,
                        contentColor = PastelBlueDark
                    )
                ) {
                    Icon(imageVector = Icons.Default.WbSunny, contentDescription = "Re-run AI")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Re-Apply Studio Enhancement", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Step4VoiceToCatalogScreen(
    state: KalaSetuUiState,
    viewModel: KalaSetuViewModel
) {
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.setListeningVoice(false)
        if (result.resultCode == Activity.RESULT_OK) {
            val spoken = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                viewModel.updateVoiceTranscript(spoken)
                viewModel.generateAiCatalogFromVoice()
            }
        }
    }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            try {
                viewModel.setListeningVoice(true)
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                    putExtra(
                        RecognizerIntent.EXTRA_PROMPT,
                        "बोलें: उत्पाद का नाम, बनने का समय और कच्चे माल का खर्च..."
                    )
                }
                speechLauncher.launch(intent)
            } catch (_: Exception) {
                viewModel.setListeningVoice(false)
                viewModel.generateAiCatalogFromVoice()
            }
        } else {
            viewModel.generateAiCatalogFromVoice()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PastelStepHeaderCard(
            stepNumber = 4,
            titleEn = "Voice-to-Catalog (Multilingual AI)",
            titleHi = "बोलकर कैटलॉग बनाएं (भाषिणी / Gemini AI)",
            subtitle = "Speak in your regional language about the product, hours taken, and material cost. AI translates & writes SEO titles."
        )

        // Voice Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = BorderStroke(1.5.dp, PastelBlueBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Select Regional Language (बोली चुनें):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("हिंदी (Hindi)", "मराठी (Marathi)", "தமிழ் (Tamil)", "বাংলা (Bengali)", "English").forEach { lang ->
                        val selected = state.selectedVoiceLang == lang
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.selectVoiceLanguage(lang) },
                            label = { Text(lang, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PastelBlueContainer,
                                selectedLabelColor = PastelBlueDark
                            )
                        )
                    }
                }

                // Big Accessible Voice Recording Button
                Button(
                    onClick = {
                        recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("record_voice_note_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PastelBluePrimary,
                        contentColor = PureWhite
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Speak Voice Note",
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (state.isListeningVoice) {
                            "Listening... Speak Now (बोलिए...)"
                        } else {
                            "Tap & Speak Voice Note (माइक दबाकर बोलें)"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                // Quick Regional Voice Presets for easy Emulator Testing
                Text(
                    text = "Or Tap a Sample Artisan Voice Note:",
                    style = MaterialTheme.typography.labelMedium,
                    color = InkMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.applyVoiceSamplePreset(
                                "यह हाथ से बना मिट्टी का दीया और कुल्हड़ सेट है, इसे बनाने में डेढ़ घंटे लगे और मिट्टी व रंग का खर्च 40 रुपये आया।",
                                "हिंदी (Hindi)"
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Hindi Diya (₹40)", fontSize = 11.sp, color = PastelBlueDark)
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.applyVoiceSamplePreset(
                                "जयपुर की ब्लू पॉटरी का छोटा फूलदान, 2 घंटे मेहनत और 65 रुपये कच्चा माल लगा है।",
                                "हिंदी (Hindi)"
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Pottery (₹65)", fontSize = 11.sp, color = PastelBlueDark)
                    }
                    OutlinedButton(
                        onClick = {
                            viewModel.applyVoiceSamplePreset(
                                "बांस से बुना हुआ 6 कोस्टर का सेट, डेढ़ घंटा लगा और बांस का खर्च 45 रुपये है।",
                                "हिंदी (Hindi)"
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Bamboo (₹45)", fontSize = 11.sp, color = PastelBlueDark)
                    }
                }

                OutlinedTextField(
                    value = state.voiceTranscript,
                    onValueChange = { viewModel.updateVoiceTranscript(it) },
                    label = { Text("Recorded Voice Note (आवाज़ का टेक्स्ट)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("voice_transcript_input"),
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PastelBluePrimary,
                        unfocusedBorderColor = PastelBlueBorder
                    )
                )

                Button(
                    onClick = { viewModel.generateAiCatalogFromVoice() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("generate_ai_catalog_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PastelBlueContainer,
                        contentColor = PastelBlueDark
                    )
                ) {
                    if (state.isGeneratingCatalog) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = PastelBlueDark,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Translating & Writing SEO Catalog...")
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "AI Translate")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Translate & Generate SEO Catalog",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Generated Bilingual SEO Catalog Output Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PastelBlueLight),
            border = BorderStroke(1.5.dp, PastelBlueBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Bilingual SEO Output",
                            tint = PastelBlueDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI Generated SEO Title & Description",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PastelBlueDark
                        )
                    }
                }

                OutlinedTextField(
                    value = state.catalogTitleEn,
                    onValueChange = { viewModel.updateCatalogTitleEn(it) },
                    label = { Text("SEO English Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite
                    )
                )

                OutlinedTextField(
                    value = state.catalogTitleHi,
                    onValueChange = { viewModel.updateCatalogTitleHi(it) },
                    label = { Text("SEO Hindi Title (हिंदी शीर्षक)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite
                    )
                )

                OutlinedTextField(
                    value = state.catalogDescEn,
                    onValueChange = { viewModel.updateCatalogDescEn(it) },
                    label = { Text("E-Commerce SEO Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        color = PureWhite,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, PastelBlueBorder)
                    ) {
                        Text(
                            text = "Extracted Material Cost: ₹${state.rawMaterialCost}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = InkDark
                        )
                    }
                    Surface(
                        color = PureWhite,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, PastelBlueBorder)
                    ) {
                        Text(
                            text = "Time Taken: ${state.hoursTaken} hrs",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = InkDark
                        )
                    }
                }

                // Explicit Next Step Button inside Voice-to-Catalog card as requested
                Button(
                    onClick = { viewModel.nextStep() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("voice_catalog_next_step_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PastelBluePrimary,
                        contentColor = PureWhite
                    )
                ) {
                    Text(
                        text = "Next Step: Dynamic Pricing (आगे बढ़ें)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next Step"
                    )
                }

                // Prototype Security Warning required by gemini-api skill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Security Notice",
                        tint = InkMedium,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Prototype Notice: API keys in BuildConfig are for prototype testing only; do not share APK publicly.",
                        fontSize = 10.sp,
                        color = InkMedium
                    )
                }
            }
        }
    }
}
