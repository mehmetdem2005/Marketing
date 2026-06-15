package com.secal.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * SeçAl metin alanı (atom). Etiket + opsiyonel yardım/hata metni.
 *  - Hata durumunda semantics.error ile ekran okuyucuya bildirilir (WCAG 3.3.1).
 *  - Tek satır + klavye tipi (e-posta/şifre/sayı) parametrik.
 */
@Composable
fun SecalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    errorText: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
) {
    val isError = errorText != null
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .semantics { if (isError) error(errorText!!) },
        label = { Text(label) },
        enabled = enabled,
        singleLine = singleLine,
        isError = isError,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        supportingText = when {
            isError -> ({ Text(errorText!!) })
            supportingText != null -> ({ Text(supportingText) })
            else -> null
        },
    )
}
