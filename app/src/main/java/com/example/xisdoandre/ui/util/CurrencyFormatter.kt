package com.example.xisdoandre.ui.util

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(value: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(value)
}
