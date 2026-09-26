package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

data class StepMetadata(
    val number: Int,
    val shortTitle: String,
    val hindiHint: String,
    val voiceGuidanceEn: String,
    val voiceGuidanceHi: String
)

val ALL_EIGHT_STEPS = listOf(
    StepMetadata(
        1,
        "Login",
        "लॉगिन",
        "Step 1: Enter your 10 digit mobile number and OTP to start. Or tap Next Step.",
        "कदम एक: अपना मोबाइल नंबर और ओटीपी दर्ज करें या अगला कदम दबाएं।"
    ),
    StepMetadata(
        2,
        "Photo",
        "फोटो लें",
        "Step 2: Take a photo with Camera or upload from Gallery. Then tap Next Step.",
        "कदम दो: कैमरे से अपने उत्पाद की फोटो खींचें या गैलरी से चुनें।"
    ),
    StepMetadata(
        3,
        "AI Studio",
        "सफ़ेद बैकग्राउंड",
        "Step 3: AI automatically removes the background to pure white and fixes lighting.",
        "कदम तीन: एआई ने फोटो का बैकग्राउंड सफ़ेद कर दिया है और रोशनी ठीक कर दी है।"
    ),
    StepMetadata(
        4,
        "Voice NLP",
        "बोलकर विवरण",
        "Step 4: Speak in your language about the product, time taken, and material cost.",
        "कदम चार: अपनी भाषा में बोलकर सामान का नाम, समय और लागत बताएं।"
    ),
    StepMetadata(
        5,
        "Smart Price",
        "सही दाम",
        "Step 5: Review the affordable selling price and your profit margin.",
        "कदम पांच: कम और सही कीमत चुनें जिसमें आपका पूरा मुनाफा शामिल है।"
    ),
    StepMetadata(
        6,
        "Market & KYC",
        "बाज़ार व केवाईसी",
        "Step 6: Choose ONDC marketplaces and enter your Artisan ID or UPI.",
        "कदम छह: ओएनडीसी बाज़ार चुनें और अपनी यूपीआई आईडी भरें।"
    ),
    StepMetadata(
        7,
        "1-Tap ONDC",
        "एक-टैप लिस्टिंग",
        "Step 7: Tap One-Tap Publish to list your product on ONDC instantly.",
        "कदम सात: एक बटन दबाकर अपना सामान ओएनडीसी बाज़ार में लाइव करें।"
    ),
    StepMetadata(
        8,
        "Orders",
        "ऑर्डर व स्टॉक",
        "Step 8: Track your orders and tap the green Order Done button when completed.",
        "कदम आठ: अपने ऑर्डर देखें और पूरा होने पर हरे रंग का ऑर्डर डन बटन दबाएं।"
    )
)

@Composable
fun KalaSetuTopHeader(
    currentStep: Int,
    onSpeakGuidance: () -> Unit,
    onOpenArchitectureModal: () -> Unit,
    onSelectStep: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PastelBlueContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = "KalaSetu Logo",
                        tint = PastelBlueDark,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "KalaSetu • कलासेतु",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = InkDark
                    )
                    Text(
                        text = "Step $currentStep of 8 • Artisan AI",
                        style = MaterialTheme.typography.labelMedium,
                        color = InkMedium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // FastAPI / Expo Architecture Reference Button
                Surface(
                    modifier = Modifier
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onOpenArchitectureModal() }
                        .testTag("arch_reference_button"),
                    color = PastelBlueLight,
                    border = BorderStroke(1.dp, PastelBlueBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = "FastAPI & Expo Code Reference",
                            tint = PastelBlueDark,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Stack",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = PastelBlueDark
                        )
                    }
                }

                // Prominent Voice Guide Button for Low-Literacy Artisans
                Surface(
                    modifier = Modifier
                        .height(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSpeakGuidance() }
                        .testTag("voice_guide_button"),
                    color = PastelBlueContainer,
                    border = BorderStroke(1.dp, PastelBluePrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Listen to Voice Guidance",
                            tint = PastelBlueDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "सुनें / Listen",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = PastelBlueDark
                        )
                    }
                }
            }
        }

        // Interactive 8-Step Stepper Bar
        InteractiveEightStepBar(
            currentStep = currentStep,
            onStepSelected = onSelectStep
        )
    }
}

@Composable
fun InteractiveEightStepBar(
    currentStep: Int,
    onStepSelected: (Int) -> Unit
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PastelBlueLight.copy(alpha = 0.6f))
            .horizontalScroll(scrollState)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ALL_EIGHT_STEPS.forEach { step ->
            val isCompleted = step.number < currentStep
            val isActive = step.number == currentStep

            val bgColor = when {
                isActive -> PastelBluePrimary
                isCompleted -> OrderDoneGreenContainer
                else -> PureWhite
            }
            val borderColor = when {
                isActive -> PastelBlueDark
                isCompleted -> OrderDoneGreenBorder
                else -> PastelBlueBorder
            }
            val textColor = when {
                isActive -> PureWhite
                isCompleted -> OrderDoneGreenText
                else -> InkDark
            }

            Surface(
                modifier = Modifier
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onStepSelected(step.number) }
                    .testTag("stepper_step_${step.number}"),
                color = bgColor,
                border = BorderStroke(1.5.dp, borderColor),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isActive -> PureWhite
                                    isCompleted -> OrderDoneGreen
                                    else -> PastelBlueContainer
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Step ${step.number} Completed",
                                tint = PureWhite,
                                modifier = Modifier.size(15.dp)
                            )
                        } else {
                            Text(
                                text = "${step.number}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) PastelBluePrimary else PastelBlueDark
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = step.shortTitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = step.hindiHint,
                            fontSize = 10.sp,
                            color = if (isActive) PureWhite.copy(alpha = 0.9f) else InkMedium,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
    HorizontalDivider(color = PastelBlueBorder, thickness = 1.dp)
}

@Composable
fun StepBottomNavigationBar(
    currentStep: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onStartNewCraft: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(PureWhite)
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = PureWhite,
        shadowElevation = 8.dp
    ) {
        Column {
            HorizontalDivider(color = PastelBlueBorder, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = onPrevious,
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("previous_step_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, PastelBluePrimary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = PastelBlueLight,
                            contentColor = PastelBlueDark
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Step",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Back (पीछे)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // On Step 1 (Default Login), provide quick shortcut to Step 8 Orders
                    OutlinedButton(
                        onClick = { onStartNewCraft() },
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("dashboard_shortcut_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, PastelBlueBorder),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = PastelBlueLight,
                            contentColor = PastelBlueDark
                        )
                    ) {
                        Text(
                            text = "Orders (ऑर्डर)",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                if (currentStep < 8) {
                    val nextStepMeta = ALL_EIGHT_STEPS.getOrNull(currentStep)
                    Button(
                        onClick = onNext,
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("next_step_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PastelBluePrimary,
                            contentColor = PureWhite
                        )
                    ) {
                        Text(
                            text = if (nextStepMeta != null) {
                                "Next: ${nextStepMeta.shortTitle} (आगे)"
                            } else {
                                "Next Step (आगे)"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Step",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onStartNewCraft,
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("add_new_craft_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = OrderDoneGreen,
                            contentColor = PureWhite
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Add New Craft",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Add New Product (नया सामान)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PastelStepHeaderCard(
    stepNumber: Int,
    titleEn: String,
    titleHi: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = PastelBlueLight),
        border = BorderStroke(1.5.dp, PastelBlueBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = PastelBluePrimary,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "STEP $stepNumber OF 8",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = PureWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = titleHi,
                    style = MaterialTheme.typography.labelLarge,
                    color = PastelBlueDark,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titleEn,
                style = MaterialTheme.typography.headlineMedium,
                color = InkDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = InkMedium
            )
        }
    }
}
