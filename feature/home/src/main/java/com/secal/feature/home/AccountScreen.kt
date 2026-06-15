package com.secal.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.secal.designsystem.theme.LocalSpacing
import com.secal.feature.home.R

/** Hesabım: kullanıcı menüsü (profil / satıcı / çıkış) — Trendyol "Hesabım" kalıbı. */
@Composable
fun AccountScreen(
    onProfile: () -> Unit,
    onOrders: () -> Unit,
    onSeller: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Column(modifier = modifier.fillMaxWidth().padding(vertical = spacing.md)) {
        Text(
            stringResource(R.string.account_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = spacing.md, vertical = spacing.sm),
        )
        AccountRow(icon = Icons.Outlined.Person, label = stringResource(R.string.account_profile), onClick = onProfile)
        HorizontalDivider()
        AccountRow(icon = Icons.Outlined.ReceiptLong, label = stringResource(R.string.account_orders), onClick = onOrders)
        HorizontalDivider()
        AccountRow(icon = Icons.Outlined.Storefront, label = stringResource(R.string.account_seller), onClick = onSeller)
        HorizontalDivider()
        AccountRow(
            icon = Icons.AutoMirrored.Filled.Logout,
            label = stringResource(R.string.account_signout),
            onClick = onSignOut,
            tint = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
private fun AccountRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = spacing.md, vertical = spacing.md),
        horizontalArrangement = Arrangement.spacedBy(spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge,
            color = tint,
            modifier = Modifier.weight(1f),
        )
        Icon(
            Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
