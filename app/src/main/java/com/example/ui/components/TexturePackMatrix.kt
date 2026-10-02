package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TrackDictionary
import com.example.ui.theme.ImmersiveCardBorder
import com.example.ui.theme.ImmersiveCardBorderSubtle
import com.example.ui.theme.ImmersiveLilac
import com.example.ui.theme.ImmersiveLilacContainer
import com.example.ui.theme.ImmersiveMint
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.TextTertiary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TexturePackMatrix(
    currentPack: String,
    onApplyPack: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    normalPacks: List<String> = TrackDictionary.NORMAL_PACKS,
    rainPacks: List<String> = TrackDictionary.RAIN_PACKS,
    dayPacks: Set<String> = TrackDictionary.DAY_PACKS_FOR_RAIN_CHECK
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("texture_pack_matrix"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurface),
        border = BorderStroke(1.dp, ImmersiveCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TEXTURE PACK",
                    color = TextTertiary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 1.5.sp
                )
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    tint = ImmersiveLilac,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Normal Packs (Время суток)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFFFB74D),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Основной цикл времени суток (${normalPacks.size} паков)",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                normalPacks.forEach { pack ->
                    val isActive = pack.equals(currentPack, ignoreCase = true)
                    val isDayPack = dayPacks.contains(pack)
                    PackChip(
                        packName = pack,
                        isActive = isActive,
                        isSpecial = isDayPack,
                        specialLabel = if (isDayPack) "20% ⛈" else null,
                        accentColor = if (isDayPack) Color(0xFFFFB74D) else ImmersiveLilac,
                        onClick = { onApplyPack(pack, false) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 2. Rain Packs (Погода)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Thunderstorm,
                    contentDescription = null,
                    tint = ImmersiveMint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Дождевой цикл погоды (${rainPacks.size} паков)",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                rainPacks.forEach { rainPack ->
                    val isActive = rainPack.equals(currentPack, ignoreCase = true)
                    PackChip(
                        packName = rainPack,
                        isActive = isActive,
                        isSpecial = false,
                        specialLabel = null,
                        accentColor = ImmersiveMint,
                        onClick = { onApplyPack(rainPack, true) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PackChip(
    packName: String,
    isActive: Boolean,
    isSpecial: Boolean,
    specialLabel: String?,
    accentColor: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isActive) ImmersiveLilacContainer else ImmersiveSurfaceVariant,
        border = BorderStroke(
            1.dp,
            if (isActive) accentColor else if (isSpecial) accentColor.copy(alpha = 0.5f) else ImmersiveCardBorderSubtle
        ),
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("pack_chip_$packName")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = packName,
                color = if (isActive) accentColor else TextPrimary,
                fontSize = 11.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                fontFamily = FontFamily.Monospace
            )
            if (specialLabel != null && !isActive) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = specialLabel,
                    color = accentColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

