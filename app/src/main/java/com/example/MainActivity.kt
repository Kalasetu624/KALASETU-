package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.KalaSetuDatabase
import com.example.data.local.KalaSetuRepository
import com.example.ui.components.ALL_EIGHT_STEPS
import com.example.ui.components.KalaSetuTopHeader
import com.example.ui.components.StepBottomNavigationBar
import com.example.ui.screens.ArchitectureReferenceDialog
import com.example.ui.screens.Step1ProgressiveLoginScreen
import com.example.ui.screens.Step2PhotoCaptureScreen
import com.example.ui.screens.Step3AiEnhancementScreen
import com.example.ui.screens.Step4VoiceToCatalogScreen
import com.example.ui.screens.Step5DynamicPricingScreen
import com.example.ui.screens.Step6MarketLinkageKycScreen
import com.example.ui.screens.Step7OneTapOnboardingScreen
import com.example.ui.screens.Step8DashboardOrdersScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.OrderDoneGreenBorder
import com.example.ui.theme.OrderDoneGreenContainer
import com.example.ui.theme.OrderDoneGreenText
import com.example.ui.theme.PureWhite
import com.example.ui.viewmodel.KalaSetuViewModel
import com.example.util.VoiceHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = KalaSetuDatabase.getInstance(applicationContext)
        val repository = KalaSetuRepository(database.kalaSetuDao())

        setContent {
            MyApplicationTheme {
                val kalaSetuViewModel: KalaSetuViewModel = viewModel(
                    factory = KalaSetuViewModel.Factory(repository)
                )
                KalaSetuAppRoot(viewModel = kalaSetuViewModel)
            }
        }
    }
}

@Composable
fun KalaSetuAppRoot(viewModel: KalaSetuViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val products by viewModel.products.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()

    val voiceHelper = remember { VoiceHelper(context) }
    DisposableEffect(Unit) {
        onDispose { voiceHelper.shutdown() }
    }

    // Load initial craft bitmap asynchronously once so Step 2 & Step 3 have a live Bitmap ready
    LaunchedEffect(Unit) {
        if (uiState.capturedBitmap == null) {
            viewModel.selectCraftPreset(context, "terracotta")
        }
    }

    // Handle system Back button for sub-steps (Steps 2..8 return to previous step)
    BackHandler(enabled = uiState.currentStep > 1) {
        viewModel.previousStep()
    }

    if (uiState.showArchitectureModal) {
        ArchitectureReferenceDialog(
            onDismiss = { viewModel.setArchitectureModalVisible(false) }
        )
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(PureWhite),
        containerColor = PureWhite,
        topBar = {
            KalaSetuTopHeader(
                currentStep = uiState.currentStep,
                onSpeakGuidance = {
                    val meta = ALL_EIGHT_STEPS.getOrNull(uiState.currentStep - 1)
                    if (meta != null) {
                        voiceHelper.speakGuidance(
                            textEn = meta.voiceGuidanceEn,
                            textHi = meta.voiceGuidanceHi,
                            preferHindi = uiState.preferredLanguage.contains("हिंदी")
                        )
                    }
                },
                onOpenArchitectureModal = { viewModel.setArchitectureModalVisible(true) },
                onSelectStep = { step -> viewModel.goToStep(step) }
            )
        },
        bottomBar = {
            StepBottomNavigationBar(
                currentStep = uiState.currentStep,
                onPrevious = { viewModel.previousStep() },
                onNext = { viewModel.nextStep() },
                onStartNewCraft = {
                    if (uiState.currentStep == 1) {
                        viewModel.goToStep(8)
                    } else {
                        viewModel.startNewProductCatalog()
                    }
                }
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(PureWhite)
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            val horizontalPad = if (maxWidth > 600.dp) 28.dp else 16.dp
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
                    .verticalScroll(scrollState)
                    .padding(horizontal = horizontalPad, vertical = 14.dp)
            ) {
                // Status feedback banner if present
                uiState.statusMessage?.let { msg ->
                    Surface(
                        color = OrderDoneGreenContainer,
                        border = BorderStroke(1.dp, OrderDoneGreenBorder),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = msg,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            color = OrderDoneGreenText,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }

                AnimatedContent(
                    targetState = uiState.currentStep,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "step_transition"
                ) { step ->
                    when (step) {
                        1 -> Step1ProgressiveLoginScreen(state = uiState, viewModel = viewModel)
                        2 -> Step2PhotoCaptureScreen(state = uiState, viewModel = viewModel)
                        3 -> Step3AiEnhancementScreen(state = uiState, viewModel = viewModel)
                        4 -> Step4VoiceToCatalogScreen(state = uiState, viewModel = viewModel)
                        5 -> Step5DynamicPricingScreen(state = uiState, viewModel = viewModel)
                        6 -> Step6MarketLinkageKycScreen(state = uiState, viewModel = viewModel)
                        7 -> Step7OneTapOnboardingScreen(state = uiState, viewModel = viewModel)
                        8 -> Step8DashboardOrdersScreen(
                            state = uiState,
                            orders = orders,
                            products = products,
                            viewModel = viewModel
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
