package com.koyden.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextDecoration
import java.text.NumberFormat
import java.util.Locale

/**
 * Fiyat etiketi (molekül). Türkçe yerel biçim (₺), opsiyonel üstü-çizili eski fiyat.
 * Tutar **kuruş (minor unit)** olarak alınır → kayan nokta hatası önlenir.
 * Erişilebilirlik: tek metin olarak okunur ("129,90 lira, indirimli").
 */
@Composable
fun PriceTag(
    amountMinor: Long,
    modifier: Modifier = Modifier,
    originalAmountMinor: Long? = null,
    currencyCode: String = "TRY",
) {
    val locale = Locale("tr", "TR")
    val fmt = NumberFormat.getCurrencyInstance(locale).apply {
        currency = java.util.Currency.getInstance(currencyCode)
    }
    val price = fmt.format(amountMinor / 100.0)
    val original = originalAmountMinor?.let { fmt.format(it / 100.0) }
    val discounted = original != null && originalAmountMinor!! > amountMinor

    val a11y = buildString {
        append(price)
        if (discounted) append(", indirimli, eski fiyat $original")
    }

    Row(modifier = modifier.clearAndSetSemantics { contentDescription = a11y }) {
        Text(
            text = price,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
        )
        if (discounted) {
            Text(
                text = original!!,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textDecoration = TextDecoration.LineThrough,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
