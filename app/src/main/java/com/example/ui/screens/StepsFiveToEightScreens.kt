package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ArtisanOrderEntity
import com.example.data.local.CatalogProductEntity
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
import kotlin.math.roundToInt

@Composable
fun Step5DynamicPricingScreen(
    state: KalaSetuUiState,
    viewModel: KalaSetuViewModel
) {
    val laborCost = (state.hoursTaken * state.fairHourlyWage).roundToInt()
    val totalBaseCost = state.rawMaterialCost + laborCost + state.packagingFee
    val netProfit = (state.selectedSellingPrice - state.rawMaterialCost - state.packagingFee).coerceAtLeast(10)
    val minRange = (state.suggestedSellingPrice - 20).coerceAtLeast(50)
    val maxRange = (state.suggestedSellingPrice + 30).coerceAtMost(380)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PastelStepHeaderCard(
            stepNumber = 5,
            titleEn = "Smart Dynamic Pricing (Small Range)",
            titleHi = "सही और किफायती दाम (₹50 – ₹350)",
            subtitle = "AI calculates an affordable, fast-selling price from your raw material cost and hours worked."
        )

        // Highlighted Suggested Price Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                Text(
                    text = "AI Recommended Selling Price (सुझाया गया दाम)",
                    style = MaterialTheme.typography.labelLarge,
                    color = PastelBlueDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "₹${state.selectedSellingPrice}",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = InkDark
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = PureWhite,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, PastelBlueBorder)
                    ) {
                        Text(
                            text = "Market Range: ₹$minRange – ₹$maxRange",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PastelBlueDark
                        )
                    }
                    Surface(
                        color = OrderDoneGreenContainer,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, OrderDoneGreenBorder)
                    ) {
                        Text(
                            text = "Your Earnings: ₹$netProfit",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrderDoneGreenText
                        )
                    }
                }
            }
        }

        // Sliders for Small Price Range Customization
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
                    text = "Adjust Cost & Small Price Range:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Raw Material Cost Slider (₹15 - ₹180)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Raw Material Cost (कच्चा माल)", color = InkDark, fontWeight = FontWeight.Medium)
                        Text("₹${state.rawMaterialCost}", fontWeight = FontWeight.Bold, color = PastelBlueDark)
                    }
                    Slider(
                        value = state.rawMaterialCost.toFloat(),
                        onValueChange = { viewModel.updateRawMaterialCost(it.roundToInt()) },
                        valueRange = 15f..180f,
                        colors = SliderDefaults.colors(
                            thumbColor = PastelBluePrimary,
                            activeTrackColor = PastelBluePrimary
                        ),
                        modifier = Modifier.testTag("raw_material_slider")
                    )
                }

                // Hours Taken Slider (0.5h - 4.5h)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Time Spent (मेहनत का समय)", color = InkDark, fontWeight = FontWeight.Medium)
                        Text("${state.hoursTaken} Hours (₹$laborCost)", fontWeight = FontWeight.Bold, color = PastelBlueDark)
                    }
                    Slider(
                        value = state.hoursTaken,
                        onValueChange = { viewModel.updateHoursTaken(it) },
                        valueRange = 0.5f..4.5f,
                        steps = 7,
                        colors = SliderDefaults.colors(
                            thumbColor = PastelBluePrimary,
                            activeTrackColor = PastelBluePrimary
                        )
                    )
                }

                // Final Selling Price Slider (₹50 - ₹350)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Your Selling Price (बिक्री मूल्य)", color = InkDark, fontWeight = FontWeight.Bold)
                        Text("₹${state.selectedSellingPrice}", fontWeight = FontWeight.Bold, color = OrderDoneGreen)
                    }
                    Slider(
                        value = state.selectedSellingPrice.toFloat(),
                        onValueChange = { viewModel.updateSelectedSellingPrice(it.roundToInt()) },
                        valueRange = 50f..350f,
                        colors = SliderDefaults.colors(
                            thumbColor = OrderDoneGreen,
                            activeTrackColor = OrderDoneGreen
                        ),
                        modifier = Modifier.testTag("selling_price_slider")
                    )
                }

                HorizontalDivider(color = PastelBlueBorder)

                // Transparent Breakdown Rows
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Raw Material + Eco Packaging:", fontSize = 13.sp, color = InkMedium)
                    Text("₹${state.rawMaterialCost + state.packagingFee}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Fair Artisan Craftsmanship + Profit:", fontSize = 13.sp, color = OrderDoneGreenText)
                    Text("+ ₹$netProfit", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OrderDoneGreen)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("AI Base Valuation:", fontSize = 12.sp, color = InkMedium)
                    Text("₹$totalBaseCost (Suggested ₹${state.suggestedSellingPrice})", fontSize = 12.sp, color = InkMedium)
                }
            }
        }
    }
}

@Composable
fun Step6MarketLinkageKycScreen(
    state: KalaSetuUiState,
    viewModel: KalaSetuViewModel
) {
    val marketplaceOptions = listOf(
        Triple("ONDC Mystore", "0% Commission • Direct Govt Network", "Recommended"),
        Triple("Meesho Craft", "0% Seller Fee • Fast Small-Price Orders", "High Volume"),
        Triple("Amazon Karigar", "National Handloom & Craft Storefront", "Pan-India"),
        Triple("IndiaMART B2B", "Wholesale Bulk Gift Buyers", "Bulk Orders")
    )

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PastelStepHeaderCard(
            stepNumber = 6,
            titleEn = "Market Linkage & Simple KYC",
            titleHi = "ओएनडीसी बाज़ार और सरल केवाईसी",
            subtitle = "Select where to sell your craft. Lightweight KYC is asked only at this stage!"
        )

        // Marketplace Selection Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = PureWhite),
            border = BorderStroke(1.5.dp, PastelBlueBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Select Marketplaces (बाज़ार चुनें):",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                marketplaceOptions.forEach { (name, desc, badge) ->
                    val isChecked = state.selectedMarketplaces.contains(name)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { viewModel.toggleMarketplace(name) }
                            .testTag("marketplace_option_$name"),
                        color = if (isChecked) PastelBlueLight else PureWhite,
                        border = BorderStroke(
                            if (isChecked) 2.dp else 1.dp,
                            if (isChecked) PastelBluePrimary else PastelBlueBorder
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { viewModel.toggleMarketplace(name) },
                                colors = CheckboxDefaults.colors(checkedColor = PastelBluePrimary)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = name, fontWeight = FontWeight.Bold, color = InkDark)
                                Text(text = desc, fontSize = 12.sp, color = InkMedium)
                            }
                            Surface(
                                color = if (isChecked) OrderDoneGreenContainer else PastelBlueContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = badge,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChecked) OrderDoneGreenText else PastelBlueDark
                                )
                            }
                        }
                    }
                }
            }
        }

        // Progressive KYC Card
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = "Progressive KYC",
                        tint = PastelBluePrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Progressive KYC & Bank Payout (केवाईसी)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = state.artisanPehchanId,
                    onValueChange = { viewModel.updateArtisanPehchanId(it) },
                    label = { Text("Artisan Pehchan / Udyam ID (पहचान कार्ड)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = state.upiId,
                    onValueChange = { viewModel.updateUpiId(it) },
                    label = { Text("UPI ID for Direct Order Payout (यूपीआई आईडी)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PastelBlueLight)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Small Artisan GST Exemption (< ₹40L)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = InkDark
                        )
                        Text(
                            text = "No GST certificate needed for small rural handicrafts",
                            fontSize = 12.sp,
                            color = InkMedium
                        )
                    }
                    Switch(
                        checked = state.isGstExemptSmallArtisan,
                        onCheckedChange = { viewModel.toggleGstExempt(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PureWhite,
                            checkedTrackColor = OrderDoneGreen
                        )
                    )
                }

                if (!state.isGstExemptSmallArtisan) {
                    OutlinedTextField(
                        value = state.gstNumber,
                        onValueChange = { viewModel.updateGstNumber(it) },
                        label = { Text("Optional GSTIN Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Step7OneTapOnboardingScreen(
    state: KalaSetuUiState,
    viewModel: KalaSetuViewModel
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PastelStepHeaderCard(
            stepNumber = 7,
            titleEn = "One-Tap ONDC Onboarding",
            titleHi = "एक-टैप में ओएनडीसी पर लाइव करें",
            subtitle = "Auto-creates your seller catalog with AI studio photo, bilingual SEO text, and ₹${state.selectedSellingPrice} price."
        )

        // Ready-to-Publish E-Commerce Catalog Preview Card
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    val previewBmp = state.enhancedBitmap ?: state.capturedBitmap
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(PureWhite)
                            .border(1.5.dp, PastelBlueBorder, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (previewBmp != null) {
                            Image(
                                bitmap = previewBmp.asImageBitmap(),
                                contentDescription = "Catalog Thumbnail",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_craft_terracotta_1790431512586),
                                contentDescription = "Default Craft Thumbnail",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Surface(
                            color = PastelBlueLight,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = state.craftCategory,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PastelBlueDark
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.catalogTitleEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = InkDark
                        )
                        Text(
                            text = state.catalogTitleHi,
                            style = MaterialTheme.typography.bodyMedium,
                            color = InkMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Selling Price: ₹${state.selectedSellingPrice} • Stock: ${state.initialStockCount} units",
                            fontWeight = FontWeight.Bold,
                            color = OrderDoneGreen,
                            fontSize = 14.sp
                        )
                    }
                }

                HorizontalDivider(color = PastelBlueBorder)

                Text(
                    text = "Auto-Filled Marketplaces: ${state.selectedMarketplaces.joinToString(", ")}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PastelBlueDark
                )
                Text(
                    text = "Payout UPI: ${state.upiId} • Artisan ID: ${state.artisanPehchanId}",
                    fontSize = 12.sp,
                    color = InkMedium
                )

                // Stock Quantity Adjuster before publishing
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PastelBlueLight)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Available Stock Quantity (स्टॉक संख्या):",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.updateInitialStockCount(state.initialStockCount - 1) }
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease Stock")
                        }
                        Text(
                            text = "${state.initialStockCount}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        IconButton(
                            onClick = { viewModel.updateInitialStockCount(state.initialStockCount + 1) }
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Increase Stock")
                        }
                    }
                }

                // 1-Tap ONDC Publish Button
                Button(
                    onClick = { viewModel.publishToOndcAndSaveCatalog() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("one_tap_publish_ondc_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isPublishedSuccess) OrderDoneGreen else PastelBluePrimary,
                        contentColor = PureWhite
                    )
                ) {
                    if (state.isPublishingOndc) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = PureWhite,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Syncing Catalog to ONDC Network...", fontWeight = FontWeight.Bold)
                    } else if (state.isPublishedSuccess) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Published")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Listing Live on ONDC! (${state.publishedListingId})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    } else {
                        Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = "Publish")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "1-Tap Publish to ONDC (अभी लाइव करें)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                if (state.isPublishedSuccess) {
                    Surface(
                        color = OrderDoneGreenContainer,
                        border = BorderStroke(1.5.dp, OrderDoneGreen),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = OrderDoneGreen,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Product Live & First Buyer Order Received!",
                                    fontWeight = FontWeight.Bold,
                                    color = OrderDoneGreenText
                                )
                                Text(
                                    text = "Tap 'Next: Orders' below to manage your orders and mark them Order Done in green.",
                                    fontSize = 12.sp,
                                    color = OrderDoneGreenText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Step8DashboardOrdersScreen(
    state: KalaSetuUiState,
    orders: List<ArtisanOrderEntity>,
    products: List<CatalogProductEntity>,
    viewModel: KalaSetuViewModel
) {
    val doneCount = orders.count { it.isOrderDone }
    val pendingCount = orders.count { !it.isOrderDone }
    val totalEarnings = orders.filter { it.isOrderDone }.sumOf { it.totalAmount }

    val filteredOrders = when (state.orderFilter) {
        "PENDING" -> orders.filter { !it.isOrderDone }
        "DONE" -> orders.filter { it.isOrderDone }
        else -> orders
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PastelStepHeaderCard(
            stepNumber = 8,
            titleEn = "Artisan Orders & Inventory",
            titleHi = "ऑर्डर ट्रैकिंग और स्टॉक डैशबोर्ड",
            subtitle = "Simple order management. Completed orders are highlighted in Green ('Order Done')."
        )

        // Summary KPI Cards (Pastel Blue + Green Order Done)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PastelBlueLight),
                border = BorderStroke(1.5.dp, PastelBlueBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("New Orders", fontSize = 12.sp, color = InkMedium, fontWeight = FontWeight.SemiBold)
                    Text("$pendingCount Pending", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PastelBlueDark)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = OrderDoneGreenContainer),
                border = BorderStroke(1.5.dp, OrderDoneGreenBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Order Done", fontSize = 12.sp, color = OrderDoneGreenText, fontWeight = FontWeight.SemiBold)
                    Text("$doneCount Done ✓", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = OrderDoneGreen)
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.5.dp, PastelBlueBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Earned", fontSize = 12.sp, color = InkMedium, fontWeight = FontWeight.SemiBold)
                    Text("₹$totalEarnings", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = InkDark)
                }
            }
        }

        // Order Filter Tabs
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                "ALL" to "All Orders (${orders.size})",
                "PENDING" to "Pending ($pendingCount)",
                "DONE" to "Order Done ($doneCount)"
            ).forEach { (key, label) ->
                val selected = state.orderFilter == key
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.setOrderFilter(key) },
                    label = { Text(label, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = if (key == "DONE") OrderDoneGreenContainer else PastelBlueContainer,
                        selectedLabelColor = if (key == "DONE") OrderDoneGreenText else PastelBlueDark
                    )
                )
            }
        }

        // Orders Section
        Text(
            text = "Customer Orders (ग्राहकों के ऑर्डर)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        filteredOrders.forEach { order ->
            val isDone = order.isOrderDone
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("order_card_${order.orderCode}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDone) OrderDoneGreenContainer.copy(alpha = 0.38f) else PureWhite
                ),
                border = BorderStroke(
                    width = if (isDone) 2.dp else 1.5.dp,
                    color = if (isDone) OrderDoneGreen else PastelBlueBorder
                )
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = PastelBlueLight,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${order.orderCode} • ${order.marketplace}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PastelBlueDark
                                )
                            }
                        }

                        // Explicit Green "Order Done" Status Pill vs Pastel Blue "Pending" Pill
                        Surface(
                            color = if (isDone) OrderDoneGreen else PastelBlueContainer,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isDone) Icons.Default.Check else Icons.Default.Schedule,
                                    contentDescription = if (isDone) "Order Done" else "Order Pending",
                                    tint = if (isDone) PureWhite else PastelBlueDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isDone) "Order Done (पूरा हुआ)" else "Pending Dispatch",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDone) PureWhite else PastelBlueDark
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = order.productName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = InkDark
                            )
                            Text(
                                text = "${order.productNameHi} • Qty: ${order.quantity} • Buyer: ${order.buyerCity}",
                                fontSize = 12.sp,
                                color = InkMedium
                            )
                        }
                        Text(
                            text = "₹${order.totalAmount}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDone) OrderDoneGreen else PastelBlueDark
                        )
                    }

                    // Prominent Green "Order Done" Action Button
                    Button(
                        onClick = { viewModel.toggleOrderDoneStatus(order) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("toggle_order_done_${order.orderCode}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDone) OrderDoneGreen else PastelBluePrimary,
                            contentColor = PureWhite
                        )
                    ) {
                        Icon(
                            imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.LocalShipping,
                            contentDescription = "Order Status Action",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isDone) {
                                "Order Done ✓ (ऑर्डर पूरा हो गया - हरे रंग में)"
                            } else {
                                "Mark Order Done (ऑर्डर पूरा करें)"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Inventory Management Section (Room DB)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = "Inventory",
                tint = PastelBluePrimary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Live Catalog & Stock (आपका कैटलॉग और स्टॉक)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        products.forEach { product ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                border = BorderStroke(1.dp, PastelBlueBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val imgRes = when (product.presetImageKey) {
                        "pottery" -> R.drawable.img_craft_pottery_1790431500975
                        "bamboo" -> R.drawable.img_craft_bamboo_1790431524732
                        else -> R.drawable.img_craft_terracotta_1790431512586
                    }
                    Image(
                        painter = painterResource(id = imgRes),
                        contentDescription = product.titleEn,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, PastelBlueBorder, RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.titleEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = InkDark,
                            maxLines = 1
                        )
                        Text(
                            text = "Price: ₹${product.sellingPrice} • Cost: ₹${product.rawMaterialCost}",
                            fontSize = 12.sp,
                            color = OrderDoneGreenText,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = product.marketplaces,
                            fontSize = 11.sp,
                            color = InkMedium
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(PastelBlueLight)
                            .padding(horizontal = 4.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.updateInventoryStock(product, -1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease Stock",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Text(
                            text = "${product.stockCount}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = PastelBlueDark
                        )
                        IconButton(
                            onClick = { viewModel.updateInventoryStock(product, 1) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase Stock",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ArchitectureReferenceDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhite,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ID26090 Architecture & FastAPI Spec",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PastelBlueDark
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "1. Micro-Architecture Directory Tree",
                    fontWeight = FontWeight.Bold,
                    color = InkDark
                )
                Surface(
                    color = PastelBlueLight,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
kalasetu-id26090/
├── backend-fastapi/
│   ├── app/
│   │   ├── main.py
│   │   ├── api/v1/image_enhance.py
│   │   ├── api/v1/voice_catalog.py
│   │   ├── services/rembg_opencv.py
│   │   ├── services/pricing_engine.py
│   │   └── models/catalog_db.py
│   └── requirements.txt
└── mobile-app/ (Android Compose & Expo Spec)
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = InkDark,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Text(
                    text = "2. Python Backend (requirements.txt)",
                    fontWeight = FontWeight.Bold,
                    color = InkDark
                )
                Surface(
                    color = PastelBlueLight,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
fastapi==0.115.0
uvicorn[standard]==0.30.6
rembg==2.0.59
opencv-python-headless==4.10.0.84
numpy==1.26.4
pillow==10.4.0
python-multipart==0.0.9
sqlalchemy==2.0.35
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        color = InkDark,
                        modifier = Modifier.padding(10.dp)
                    )
                }

                Text(
                    text = "3. FastAPI Image Enhancement (Steps 2 & 3: rembg + OpenCV CLAHE)",
                    fontWeight = FontWeight.Bold,
                    color = InkDark
                )
                Surface(
                    color = PastelBlueLight,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = """
from fastapi import FastAPI, File, UploadFile, HTTPException
from rembg import remove
import cv2, numpy as np, io
from PIL import Image

app = FastAPI(title="KalaSetu Artisan AI")

@app.post("/api/v1/enhance-image")
async def enhance_craft_image(file: UploadFile = File(...)):
    raw = await file.read()
    no_bg_bytes = remove(raw) # rembg background removal
    pil_img = Image.open(io.BytesIO(no_bg_bytes)).convert("RGBA")
    white_bg = Image.new("RGBA", pil_img.size, (255, 255, 255, 255))
    composite = Image.alpha_composite(white_bg, pil_img).convert("RGB")
    
    # OpenCV LAB Histogram Equalization (CLAHE)
    bgr = cv2.cvtColor(np.array(composite), cv2.COLOR_RGB2BGR)
    lab = cv2.cvtColor(bgr, cv2.COLOR_BGR2LAB)
    l, a, b = cv2.split(lab)
    clahe = cv2.createCLAHE(clipLimit=2.0, tileGridSize=(8, 8))
    l_eq = clahe.apply(l)
    enhanced = cv2.cvtColor(cv2.merge((l_eq, a, b)), cv2.COLOR_LAB2BGR)
    return {"status": "ok", "bg": "#FFFFFF", "pipeline": "rembg+CLAHE"}
                        """.trimIndent(),
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = InkDark,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PastelBluePrimary)
            ) {
                Text("Close Reference", color = PureWhite)
            }
        }
    )
}
